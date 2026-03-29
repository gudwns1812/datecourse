# place 태그 외부조회 자동 적재 가이드

## 목적

- `place` 데이터를 기준으로 `tag`, `place_tag`를 자동 생성합니다.
- 자유 입력 태그를 만들지 않고 표준 태그 집합으로 수렴시킵니다.

## 동작 방식

스크립트는 먼저 카카오 Local API를 조회하고, 그 결과를 바탕으로 태그를 추론합니다.

조회에 사용하는 값은 아래와 같습니다.

- `place.name`
- `place.location` 좌표
- `place.kakao_place_id`
- `place.category`
- `place.description`
- `place.detail`
- `place.city`
- `place.district`

기본 동작은 아래 순서입니다.

1. `place.name`과 좌표로 카카오 키워드 검색
2. `kakao_place_id`가 있으면 검색 결과 중 같은 ID를 우선 선택
3. 없으면 이름 유사도와 거리 기준으로 가장 그럴듯한 결과 선택
4. `category_name`, `category_group_name`, 주소 텍스트를 기반으로 표준 태그 생성
5. 필요하면 `--fallback-internal`로 내부 텍스트 규칙도 보조 사용

## 준비

`psycopg`가 필요합니다.

```bash
pip install "psycopg[binary]"
```

DB 접속 정보는 아래 중 하나로 전달합니다.

- `--dsn`
- `DATABASE_URL`
- `POSTGRES_DSN`
- `DB_URL`

카카오 REST API 키는 아래 중 하나로 전달합니다.

- `--kakao-api-key`
- `KAKAO_REST_API_KEY`
- `KAKAO_API_KEY`

예시:

```bash
export DATABASE_URL="postgresql://USER:PASSWORD@HOST:5432/DBNAME"
export KAKAO_REST_API_KEY="카카오REST키"
```

## PyCharm 실행 설정

PyCharm Run Configuration의 `Environment variables`에 아래처럼 넣으면 됩니다.

```text
DATABASE_URL=postgresql://USER:PASSWORD@HOST:5432/DBNAME;KAKAO_REST_API_KEY=카카오REST키
```

Script path:

```text
/Users/hj.park/projects/datecourse/scripts/place_tag_backfill.py
```

Parameters 예시:

```text
--limit 30
```

## 사용법

우선 dry-run으로 결과를 확인합니다.

```bash
python3 scripts/place_tag_backfill.py --limit 30
```

특정 place만 테스트할 수도 있습니다.

```bash
python3 scripts/place_tag_backfill.py --place-id 101
```

반경이나 검색 개수를 조절할 수 있습니다.

```bash
python3 scripts/place_tag_backfill.py --limit 30 --radius 500 --size 15
```

실제 반영은 `--apply`를 사용합니다.

```bash
python3 scripts/place_tag_backfill.py --limit 200 --apply
```

이미 태그가 있는 place도 다시 보고 싶으면:

```bash
python3 scripts/place_tag_backfill.py --include-tagged --limit 50
```

카카오 결과가 빈약할 때 내부 텍스트 규칙을 보조로 쓰고 싶으면:

```bash
python3 scripts/place_tag_backfill.py --limit 50 --fallback-internal
```

## 출력 해석

- `[MATCH]`: 태그가 추론된 place
- `[MISS]`: 태그를 찾지 못한 place
- `source=`: `kakao` 또는 `internal-fallback`
- `kakao_id`, `kakao_name`: 매칭된 카카오 장소 정보
- `reasons=`: 어떤 키워드 규칙으로 태그가 붙었는지 표시

## 운영 팁

- 먼저 `--limit 30` 정도로 결과 품질을 봅니다.
- `MISS`가 많은 장소는 이름이 너무 일반적이거나 좌표가 부정확할 수 있습니다.
- 결과 품질이 아쉬우면 `--radius`를 늘리거나 `TAG_RULES` 키워드를 보강합니다.
- 태그 체계가 확정되면 `--apply`로 실제 반영합니다.
