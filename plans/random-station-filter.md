# 랜덤역 필터링 기능 추가 계획 (API 연동 방식)

## 개요
서버 API를 통해 필터 조건을 전달하고, 서버에서 필터링된 역 중 무작위 1개를 반환하는 구조로 개편합니다.
- **개발 범위**: 프론트엔드 UI 및 API 연동 (백엔드 로직은 별도 개발)
- **필터 조건**: 시/도, 구/군, 라인 (위치 필터 제외)

---

## 페이지 흐름
1. 홈 `LuckyDraw` 컴포넌트 버튼 클릭
2. `/stations/random` 페이지 이동 -> **필터 선택 패널 표시** (초기 룰렛 숨김)
3. 드롭다운으로 지역(시/도, 구/군), 라인 선택
4. `뽑기` 버튼 클릭 -> 선택한 필터값을 쿼리 파라미터로 포함하여 서버 API 호출
5. API 응답 수신 후 룰렛 애니메이션 실행 -> 결과 표시

---

## 📋 합의 필요한 API 스펙 (백엔드 구현 요구사항)

프론트엔드 구현을 위해 아래 두 가지 API 스펙이 필요합니다. (백엔드 개발 시 참고)

### 1. 필터 옵션 조회 API
필터 드롭다운 구성을 위한 지역/라인 목록이 필요합니다.

**요청**: `GET /api/v1/stations/filters`
**응답 예시**:
```json
{
  "result": "SUCCESS",
  "data": {
    "cities": ["서울특별시", "경기도", "인천광역시"],
    "districts": {
      "서울특별시": ["강남구", "서초구", "송파구"],
      "경기도": ["수원시", "성남시"]
    },
    "lines": ["1호선", "2호선", "수인분당선"]
  }
}
```

### 2.랜덤 역 뽑기 API (수정)
기존 API에 필터링 쿼리 파라미터를 추가할 수 있어야 합니다.

**요청**: `GET /api/v1/stations/random?city={시/도}&district={구/군}&line={라인}`
- 모든 파라미터는 선택적(Optional)이어야 합니다.
- 조건에 맞는 역이 없을 경우 404 에러 또는 특정 에러 코드를 반환해야 합니다.

---

## 🛠 To-Do List (프론트엔드 작업)

### Phase 1. 공통 컴포넌트
- [ ] `src/components/common/Select.tsx` 신규 생성
  - 프로젝트 디자인 시스템(primary color, slate 계열) 스타일 적용

### Phase 2. API 서비스 계층 확장
- [ ] `src/services/station.ts` 수정
  - `StationFilter` 타입 정의 (`city`, `district`, `line`)
  - `FilterOptions` 타입 정의 (`cities`, `districts`, `lines`)
  - `getStationFilters()` 함수 구현 (필터 옵션 조회 API 호출)
  - `getRandomStation(filter?: StationFilter)` 함수 수정 (쿼리 파라미터 추가)

### Phase 3. 필터 UI 컴포넌트
- [ ] `src/components/features/StationFilterPanel.tsx` 신규 생성
  - 시/도, 구/군, 라인 Select 3개 포함
  - `getStationFilters()` API 결과로 상태 초기화
  - 시/도 선택 시 구/군 리스트 업데이트
  - "🎲 랜덤으로 뽑기" 버튼 클릭 시 부모 컴포넌트에 선택값 전달

### Phase 4. 랜덤 뽑기 페이지 개편
- [ ] `src/app/stations/random/page.tsx` 전면 수정
  - 페이지 진입 시 필터 패널 노출 (`idle` 상태)
  - 필터 선택 후 "뽑기" 클릭 시 `getRandomStation(선택 필터)` API 호출 (`fetching` 상태)
  - API 완료 시 룰렛 회전 애니메이션 (`spinning` 상태)
  - 룰렛 종료 시 결과(역명, 라인 배지 등) 노출 (`result` 상태)
  - 결과 없음 에러 핸들링 (안내 메시지 표시)
  - "다시 뽑기" (동일 필터 재요청), "필터 변경" (패널 복귀) 지원
