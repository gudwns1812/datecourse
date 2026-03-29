# place 태그 자동 적재 계획

## 목적

- `place` 데이터는 존재하지만 `tag`, `place_tag`가 비어 있는 상태를 자동화 스크립트로 보완합니다.
- 장소명, 좌표, `kakao_place_id`를 바탕으로 외부 API 결과를 조회하고 표준 태그를 안전하게 적재합니다.

## 변경 범위

- `/Users/hj.park/projects/datecourse/scripts/place_tag_backfill.py`
- `/Users/hj.park/projects/datecourse/docs/place-tag-backfill.md`

## 작업 단계

### 1. 외부 장소 조회 규칙 정의

- `place.name`과 `location` 좌표를 사용해 외부 장소 검색을 수행합니다.
- `kakao_place_id`가 있으면 외부 결과와 우선 매칭합니다.
- 외부 응답의 카테고리와 텍스트를 표준 태그 집합으로 수렴시킵니다.

### 2. DB 적재 스크립트 구현

- 활성(`deleted_at is null`) `place`를 조회합니다.
- 이미 연결된 `place_tag`는 건너뜁니다.
- 외부 API로 추론된 태그를 `tag`에 upsert하고 `place_tag`에 중복 없이 연결합니다.
- `--limit`, `--place-id`, `--include-tagged`, `--apply`, `--radius` 옵션을 제공합니다.

### 3. 검토 보조 출력 제공

- 태그를 하나도 찾지 못한 `place`를 따로 출력합니다.
- 어떤 외부 결과와 매칭되었는지, 어떤 규칙으로 태그가 붙었는지 콘솔에서 확인할 수 있게 합니다.

## 검증 계획

- `python3 -m py_compile scripts/place_tag_backfill.py`
- `python3 scripts/place_tag_backfill.py --help`
- 실제 DB 반영 전 기본 dry-run으로 결과를 검토합니다.

## 주의 사항

- 자동 생성 태그는 자유 텍스트가 아니라 표준 태그 집합으로 제한합니다.
- `tag.name` 중복과 `place_tag(place_id, tag_id)` 중복은 모두 안전하게 처리합니다.
- 외부 API 키와 DB 접속 정보는 코드에 하드코딩하지 않고 실행 환경 변수로 주입합니다.
- 외부 검색 결과가 부정확한 장소는 내부 규칙 기반 fallback을 선택적으로 사용합니다.
