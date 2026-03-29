#!/usr/bin/env python3
"""place 데이터를 기반으로 외부 API를 조회해 tag, place_tag를 자동 적재한다."""

from __future__ import annotations

import argparse
import json
import os
import re
import sys
import unicodedata
import urllib.error
import urllib.parse
import urllib.request
from dataclasses import dataclass, field
from typing import Dict, List, Sequence, Tuple

try:
    import psycopg
    from psycopg.rows import dict_row

    DRIVER = "psycopg"
except ImportError:  # pragma: no cover - 로컬 환경 의존
    psycopg = None
    dict_row = None
    DRIVER = ""


DEFAULT_DSN_ENV_KEYS = ("DATABASE_URL", "POSTGRES_DSN", "DB_URL")
DEFAULT_KAKAO_ENV_KEYS = ("KAKAO_REST_API_KEY", "KAKAO_API_KEY")
KAKAO_KEYWORD_SEARCH_URL = "https://dapi.kakao.com/v2/local/search/keyword.json"

TAG_RULES: Dict[str, Sequence[str]] = {
    "카페": ("카페", "coffee", "커피", "에스프레소", "로스터리"),
    "디저트": ("디저트", "케이크", "빙수", "마카롱", "베이커리", "도넛", "쿠키"),
    "브런치": ("브런치", "샌드위치", "팬케이크", "브렉퍼스트"),
    "한식": ("한식", "국밥", "백반", "찌개", "비빔밥", "갈비", "삼겹살", "보쌈", "족발"),
    "중식": ("중식", "짜장", "짬뽕", "마라", "훠궈", "딤섬", "양꼬치"),
    "일식": ("일식", "스시", "초밥", "사시미", "라멘", "우동", "돈카츠", "이자카야"),
    "양식": ("양식", "파스타", "리조또", "스테이크", "피자", "이탈리안", "프렌치"),
    "분식": ("분식", "떡볶이", "김밥", "순대", "튀김"),
    "술집": ("와인", "와인바", "하이볼", "칵테일", "펍", "바", "술집", "비스트로", "이자카야"),
    "고기": ("고기", "삼겹살", "소고기", "한우", "갈비", "바비큐", "곱창", "양고기"),
    "해산물": ("해산물", "회", "조개", "오마카세", "참치", "해물", "숙성회"),
    "전시": ("전시", "갤러리", "미술관", "박물관", "아트", "전시회"),
    "문화공간": ("공연", "연극", "서점", "북카페", "문화", "복합문화", "클래스"),
    "산책": ("공원", "산책", "둘레길", "한강", "수변", "숲길", "정원"),
    "쇼핑": ("쇼핑", "편집샵", "백화점", "아울렛", "플래그십", "소품샵"),
    "뷰맛집": ("루프탑", "야경", "전망", "뷰", "리버뷰", "시티뷰", "남산뷰"),
    "데이트": ("데이트", "감성", "분위기", "기념일", "로맨틱"),
    "조용한": ("조용", "한적", "프라이빗", "아늑"),
}


@dataclass
class InferenceResult:
    tags: List[str] = field(default_factory=list)
    reasons: Dict[str, List[str]] = field(default_factory=dict)
    source: str = "internal"

    def add(self, tag: str, reason: str) -> None:
        if tag not in self.tags:
            self.tags.append(tag)
        self.reasons.setdefault(tag, []).append(reason)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="place 테이블을 스캔하여 tag, place_tag를 자동 적재합니다."
    )
    parser.add_argument("--dsn", help="PostgreSQL 접속 DSN. 미지정 시 환경 변수를 사용합니다.")
    parser.add_argument(
        "--kakao-api-key",
        help="카카오 REST API 키. 미지정 시 KAKAO_REST_API_KEY 또는 KAKAO_API_KEY를 사용합니다.",
    )
    parser.add_argument("--limit", type=int, default=100, help="처리할 place 최대 개수")
    parser.add_argument("--place-id", type=int, help="특정 place 하나만 처리")
    parser.add_argument(
        "--radius",
        type=int,
        default=300,
        help="카카오 장소 검색 반경(m). location 좌표가 있을 때 사용합니다.",
    )
    parser.add_argument(
        "--size",
        type=int,
        default=10,
        help="카카오 장소 검색 결과 개수",
    )
    parser.add_argument(
        "--include-tagged",
        action="store_true",
        help="이미 태그가 있는 place도 다시 스캔합니다.",
    )
    parser.add_argument(
        "--apply",
        action="store_true",
        help="실제로 tag, place_tag를 INSERT/UPSERT 합니다. 기본값은 dry-run 입니다.",
    )
    parser.add_argument(
        "--fallback-internal",
        action="store_true",
        help="카카오 검색 결과가 없을 때 내부 텍스트 규칙으로 한 번 더 추론합니다.",
    )
    return parser.parse_args()


def normalize_text(value: object) -> str:
    if value is None:
        return ""

    normalized = unicodedata.normalize("NFKC", str(value)).lower().strip()
    normalized = re.sub(r"[^0-9a-zA-Z가-힣\s]", " ", normalized)
    normalized = re.sub(r"\s+", " ", normalized)
    return normalized


def contains_keyword(text: str, keyword: str) -> bool:
    keyword_text = normalize_text(keyword)
    if not keyword_text:
        return False
    return keyword_text in text


def infer_tags_from_text(place: dict) -> InferenceResult:
    result = InferenceResult()

    category_text = normalize_text(place.get("category"))
    searchable_parts = [
        place.get("name"),
        place.get("category"),
        place.get("description"),
        place.get("detail"),
        place.get("district"),
        place.get("city"),
    ]
    combined_text = normalize_text(" ".join(str(part or "") for part in searchable_parts))

    for tag_name, keywords in TAG_RULES.items():
        for keyword in keywords:
            if contains_keyword(category_text, keyword):
                result.add(tag_name, f"category:{keyword}")
            elif contains_keyword(combined_text, keyword):
                result.add(tag_name, f"text:{keyword}")

    if "카페" in result.tags and "디저트" not in result.tags:
        if any(contains_keyword(combined_text, word) for word in ("디저트", "케이크", "베이커리")):
            result.add("디저트", "derived:카페_디저트")

    if "술집" in result.tags and "데이트" not in result.tags:
        if any(contains_keyword(combined_text, word) for word in ("와인", "칵테일", "분위기", "기념일")):
            result.add("데이트", "derived:술집_데이트")

    if "전시" in result.tags and "데이트" not in result.tags:
        result.add("데이트", "derived:전시_데이트")

    if "산책" in result.tags and "데이트" not in result.tags:
        result.add("데이트", "derived:산책_데이트")

    return result


def resolve_kakao_api_key(cli_key: str | None) -> str:
    if cli_key:
        return cli_key

    for env_key in DEFAULT_KAKAO_ENV_KEYS:
        env_value = os.getenv(env_key)
        if env_value:
            return env_value

    raise ValueError(
        "카카오 API 키를 찾을 수 없습니다. --kakao-api-key 또는 KAKAO_REST_API_KEY/KAKAO_API_KEY를 설정해주세요."
    )


def fetch_kakao_places(
    api_key: str,
    query: str,
    longitude: float | None,
    latitude: float | None,
    radius: int,
    size: int,
) -> List[dict]:
    params = {"query": query, "size": size}
    if longitude is not None and latitude is not None:
        params["x"] = longitude
        params["y"] = latitude
        params["radius"] = radius
        params["sort"] = "distance"

    url = f"{KAKAO_KEYWORD_SEARCH_URL}?{urllib.parse.urlencode(params)}"
    request = urllib.request.Request(
        url,
        headers={"Authorization": f"KakaoAK {api_key}"},
        method="GET",
    )

    try:
        with urllib.request.urlopen(request, timeout=10) as response:
            payload = json.loads(response.read().decode("utf-8"))
            return list(payload.get("documents", []))
    except urllib.error.HTTPError as exc:
        raise RuntimeError(f"카카오 API 호출 실패: HTTP {exc.code}") from exc
    except urllib.error.URLError as exc:
        raise RuntimeError(f"카카오 API 호출 실패: {exc.reason}") from exc


def name_similarity_score(left: str, right: str) -> int:
    left_normalized = normalize_text(left)
    right_normalized = normalize_text(right)

    if not left_normalized or not right_normalized:
        return 0
    if left_normalized == right_normalized:
        return 100
    if left_normalized in right_normalized or right_normalized in left_normalized:
        return 70

    left_tokens = set(left_normalized.split())
    right_tokens = set(right_normalized.split())
    if not left_tokens or not right_tokens:
        return 0

    common_tokens = left_tokens & right_tokens
    return min(len(common_tokens) * 20, 60)


def choose_best_kakao_match(place: dict, documents: Sequence[dict]) -> dict | None:
    if not documents:
        return None

    kakao_place_id = place.get("kakao_place_id")
    if kakao_place_id is not None:
        kakao_place_id_str = str(kakao_place_id)
        for document in documents:
            if document.get("id") == kakao_place_id_str:
                return document

    best_document = None
    best_score = -1
    place_name = str(place.get("name") or "")
    place_category = str(place.get("category") or "")

    for document in documents:
        score = 0
        score += name_similarity_score(place_name, str(document.get("place_name") or ""))
        score += name_similarity_score(place_category, str(document.get("category_name") or "")) // 2

        distance_text = str(document.get("distance") or "").strip()
        if distance_text.isdigit():
            distance = int(distance_text)
            if distance <= 50:
                score += 40
            elif distance <= 150:
                score += 25
            elif distance <= 300:
                score += 10

        if score > best_score:
            best_score = score
            best_document = document

    return best_document


def infer_tags_from_kakao(place: dict, document: dict) -> InferenceResult:
    result = InferenceResult(source="kakao")
    searchable_parts = [
        document.get("place_name"),
        document.get("category_name"),
        document.get("category_group_name"),
        document.get("address_name"),
        document.get("road_address_name"),
        place.get("description"),
        place.get("detail"),
    ]
    combined_text = normalize_text(" ".join(str(part or "") for part in searchable_parts))
    category_text = normalize_text(document.get("category_name"))

    for tag_name, keywords in TAG_RULES.items():
        for keyword in keywords:
            if contains_keyword(category_text, keyword):
                result.add(tag_name, f"kakao-category:{keyword}")
            elif contains_keyword(combined_text, keyword):
                result.add(tag_name, f"kakao-text:{keyword}")

    category_group_name = normalize_text(document.get("category_group_name"))
    if contains_keyword(category_group_name, "음식점") and "데이트" not in result.tags:
        if any(tag in result.tags for tag in ("양식", "일식", "중식", "한식", "브런치", "술집")):
            result.add("데이트", "derived:kakao_food_place")

    if contains_keyword(category_group_name, "카페") and "카페" not in result.tags:
        result.add("카페", "derived:kakao_group_카페")

    return result


def infer_tags(
    place: dict,
    kakao_api_key: str,
    radius: int,
    size: int,
    fallback_internal: bool,
) -> Tuple[InferenceResult, dict | None]:
    longitude = place.get("longitude")
    latitude = place.get("latitude")
    documents = fetch_kakao_places(
        api_key=kakao_api_key,
        query=str(place.get("name") or ""),
        longitude=float(longitude) if longitude is not None else None,
        latitude=float(latitude) if latitude is not None else None,
        radius=radius,
        size=size,
    )

    document = choose_best_kakao_match(place, documents)
    if document is not None:
        kakao_result = infer_tags_from_kakao(place, document)
        if kakao_result.tags:
            return kakao_result, document

    internal_result = infer_tags_from_text(place)
    if fallback_internal and internal_result.tags:
        internal_result.source = "internal-fallback"
        return internal_result, document

    empty_result = InferenceResult(source="kakao")
    return empty_result, document


def resolve_dsn(cli_dsn: str | None) -> str:
    if cli_dsn:
        return cli_dsn

    for env_key in DEFAULT_DSN_ENV_KEYS:
        env_value = os.getenv(env_key)
        if env_value:
            return env_value

    raise ValueError(
        "DSN을 찾을 수 없습니다. --dsn 옵션 또는 DATABASE_URL/POSTGRES_DSN/DB_URL 환경 변수를 설정해주세요."
    )


def connect_db(dsn: str):
    if DRIVER != "psycopg" or psycopg is None:
        raise RuntimeError("psycopg가 필요합니다. 예: pip install psycopg[binary]")
    return psycopg.connect(dsn, row_factory=dict_row)


def fetch_places(
    conn,
    limit: int,
    place_id: int | None,
    include_tagged: bool,
) -> List[dict]:
    conditions = ["p.deleted_at IS NULL"]
    params: List[object] = []

    if place_id is not None:
        conditions.append("p.id = %s")
        params.append(place_id)

    if not include_tagged:
        conditions.append(
            """
            NOT EXISTS (
                SELECT 1
                FROM place_tag pt
                WHERE pt.place_id = p.id
                  AND pt.deleted_at IS NULL
            )
            """.strip()
        )

    params.append(limit)

    query = f"""
        SELECT
            p.id,
            p.station_id,
            p.name,
            p.category,
            p.description,
            p.city,
            p.district,
            p.detail,
            p.kakao_place_id,
            ST_X(p.location) AS longitude,
            ST_Y(p.location) AS latitude
        FROM place p
        WHERE {" AND ".join(conditions)}
        ORDER BY p.id
        LIMIT %s
    """

    with conn.cursor() as cur:
        cur.execute(query, params)
        return list(cur.fetchall())


def upsert_tag(cur, tag_name: str) -> int:
    cur.execute(
        """
        INSERT INTO tag (name)
        VALUES (%s)
        ON CONFLICT (name) WHERE deleted_at IS NULL
        DO UPDATE SET last_modified_at = CURRENT_TIMESTAMP
        RETURNING id
        """,
        (tag_name,),
    )
    row = cur.fetchone()
    if row is None:
        raise RuntimeError(f"tag upsert 실패: {tag_name}")
    return int(row["id"])


def insert_place_tag(cur, place_id: int, tag_id: int) -> None:
    cur.execute(
        """
        INSERT INTO place_tag (place_id, tag_id)
        VALUES (%s, %s)
        ON CONFLICT (place_id, tag_id) WHERE deleted_at IS NULL
        DO NOTHING
        """,
        (place_id, tag_id),
    )


def format_reasons(reasons: Dict[str, List[str]]) -> str:
    formatted = []
    for tag_name, reason_list in reasons.items():
        reason_text = ", ".join(reason_list)
        formatted.append(f"{tag_name}[{reason_text}]")
    return " | ".join(formatted)


def print_preview(place: dict, inference: InferenceResult, matched_document: dict | None) -> None:
    place_name = place.get("name", "")
    category = place.get("category", "")
    detail = place.get("detail", "")
    matched_id = matched_document.get("id") if matched_document else "-"
    matched_name = matched_document.get("place_name") if matched_document else "-"
    if inference.tags:
        print(
            f"[MATCH] place_id={place['id']} name={place_name} category={category} "
            f"tags={','.join(inference.tags)} source={inference.source} "
            f"kakao_id={matched_id} kakao_name={matched_name}"
        )
        print(f"        reasons={format_reasons(inference.reasons)}")
    else:
        print(
            f"[MISS]  place_id={place['id']} name={place_name} category={category} "
            f"detail={detail} source={inference.source} kakao_id={matched_id}"
        )


def apply_inference(
    conn,
    places: Sequence[dict],
    apply_changes: bool,
    kakao_api_key: str,
    radius: int,
    size: int,
    fallback_internal: bool,
) -> Tuple[int, int, int]:
    processed = 0
    inserted_links = 0
    misses = 0

    with conn.cursor() as cur:
        for place in places:
            inference, matched_document = infer_tags(
                place=place,
                kakao_api_key=kakao_api_key,
                radius=radius,
                size=size,
                fallback_internal=fallback_internal,
            )
            print_preview(place, inference, matched_document)

            if not inference.tags:
                misses += 1
                continue

            processed += 1
            if not apply_changes:
                continue

            for tag_name in inference.tags:
                tag_id = upsert_tag(cur, tag_name)
                insert_place_tag(cur, int(place["id"]), tag_id)
                inserted_links += 1

    return processed, inserted_links, misses


def main() -> int:
    args = parse_args()

    try:
        dsn = resolve_dsn(args.dsn)
        kakao_api_key = resolve_kakao_api_key(args.kakao_api_key)
        conn = connect_db(dsn)
    except Exception as exc:  # pragma: no cover - 실행 환경 의존
        print(f"[ERROR] 초기화 실패: {exc}", file=sys.stderr)
        return 1

    try:
        places = fetch_places(
            conn=conn,
            limit=args.limit,
            place_id=args.place_id,
            include_tagged=args.include_tagged,
        )

        if not places:
            print("[INFO] 처리 대상 place가 없습니다.")
            return 0

        processed, inserted_links, misses = apply_inference(
            conn=conn,
            places=places,
            apply_changes=args.apply,
            kakao_api_key=kakao_api_key,
            radius=args.radius,
            size=args.size,
            fallback_internal=args.fallback_internal,
        )

        if args.apply:
            conn.commit()
        else:
            conn.rollback()

        print(
            "[SUMMARY] "
            f"대상={len(places)}건 "
            f"태그추론성공={processed}건 "
            f"미분류={misses}건 "
            f"링크시도={inserted_links}건 "
            f"mode={'apply' if args.apply else 'dry-run'}"
        )
        return 0
    except Exception as exc:  # pragma: no cover - 실행 환경 의존
        conn.rollback()
        print(f"[ERROR] 처리 실패: {exc}", file=sys.stderr)
        return 1
    finally:
        conn.close()


if __name__ == "__main__":
    raise SystemExit(main())
