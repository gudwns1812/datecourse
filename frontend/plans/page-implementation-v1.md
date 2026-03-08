# 프론트엔드 페이지 및 API 연동 구현 계획 (v1.0)

이 계획은 `api/backend-api/` 명세를 바탕으로 '어디역?' 서비스의 주요 페이지(메인, 로그인, 회원가입, 랜덤 결과)를 구현하고 API를 연동하는 상세 단계를 정의합니다.

## 1. 구현 목표 페이지 및 경로
1.  **메인 페이지 (`/`)**: 서비스 소개 및 '랜덤 뽑기' 시작 버튼 (로그인 상태에 따라 버튼 동작 변경).
2.  **로그인 페이지 (`/login`)**: `loginId`, `password` 입력 및 인증 처리.
3.  **회원가입 페이지 (`/signup`)**: 사용자 정보 입력 및 가입 요청.
4.  **랜덤 결과 페이지 (`/stations/random`)**: 뽑기 애니메이션 후 실제 백엔드 데이터를 표시하는 페이지.

## 2. API 연동 상세 (backend-api 참조)

### 2.1 인증 (Member API)
- **POST `/v1/auth/signup`**: 회원가입. 성공 시 `/login`으로 이동.
- **POST `/v1/auth/login`**: 로그인. 성공 시 세션 유지 및 메인(`/`)으로 이동.
- **상태 관리**: 로그인 여부를 전역 상태(Zustand 또는 Context)로 관리하여 헤더 및 버튼 UI 변경.

### 2.2 역 정보 (Station API)
- **GET `/v1/stations/random`**: 무작위 역 조회.
  - **응답 데이터 구조**: `{ lineNumbers: string[], stationName: string, stationAddress: string }`
  - **검증**: 인증된 세션이 없을 경우 `/login`으로 리다이렉트 처리.

## 3. 페이지별 구현 상세 계획

### 3.1 회원가입 (`/signup`)
- **UI**: `SKILL.md` 가이드에 따른 입력 폼 구현 (이름, 아이디, 비밀번호, 이메일, 성별, 전화번호).
- **검증**: 모든 필드 입력 여부 및 이메일/전화번호 형식 체크.
- **API 연동**: `memberService.signup()` 호출 및 결과 처리.

### 3.2 로그인 (`/login`)
- **UI**: 심플한 로그인 폼. '아직 회원이 아니신가요? 가입하기' 링크 제공.
- **API 연동**: `memberService.login()` 호출. 성공 시 사용자 이름을 저장하고 홈으로 이동.

### 3.3 랜덤 결과 페이지 (`/stations/random`)
- **애니메이션**: 기존 `LuckyDraw` 컴포넌트의 스피닝 효과를 활용.
- **데이터 로딩**: 페이지 진입 시 `stationService.getRandomStation()` 호출.
- **UI 구성**:
  - 뽑힌 역의 이름과 호선 배지 표시.
  - 상세 주소 정보 및 '다시 뽑기' 버튼 제공.
  - 주변 추천 장소(Eat/See/Do) 섹션 (현재는 정적 데이터 또는 유추된 데이터로 구성).

## 4. 구현 및 테스트 단계 (Execution Steps)

1.  **Step 1: API 서비스 레이어 구축**: `src/services/`에 `auth.ts`, `station.ts` 작성 (Axios 인터셉터를 통한 세션 관리).
2.  **Step 2: 전역 상태 관리**: 로그인 사용자 정보를 위한 `useAuthStore` 구축.
3.  **Step 3: 회원가입 및 로그인 페이지 구현**: 폼 핸들링 및 API 연동 완료.
4.  **Step 4: 메인 및 결과 페이지 연동**: `LuckyDraw` 클릭 시 인증 체크 후 `/stations/random`으로 전환 및 API 호출.
5.  **Step 5: 통합 테스트**: 가입 -> 로그인 -> 역 뽑기 전체 시나리오 검증.

---
*모든 구현은 `frontend/design/SKILL.md`의 디자인 시스템을 엄격히 준수합니다.*
