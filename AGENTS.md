# AGENTS.md

Datecourse 모노레포에서 Codex가 일관된 결과를 내기 위한 실행 규칙 문서입니다.

## 1) 적용 범위와 우선순위

- 이 문서는 저장소 전체에 적용합니다.
- 모듈별 상세 규칙은 아래 문서를 함께 준수합니다.
  - 백엔드: `backend/BACKEND.md`
  - 프론트엔드: `frontend/FRONTEND.md`
  - 공통 지침: `GEMINI.md`
- 충돌 시 우선순위:
  1. 사용자의 현재 요청
  2. 이 문서(AGENTS.md)
  3. 모듈별 문서(`BACKEND.md`, `FRONTEND.md`)
  4. 기타 일반 규칙

## 2) 모노레포 작업 원칙

- 백엔드와 프론트엔드는 독립 모듈로 취급합니다.
- 한 작업에서 양쪽을 동시에 바꿀 때는 인터페이스(API 계약)부터 고정하고 구현합니다.
- 병렬 작업 시 파일 소유권을 분리합니다.
  - Backend 소유: `backend/**`, `api/backend-api/**`
  - Frontend 소유: `frontend/**`
- 생성 산출물/캐시(`frontend/.next`, `build/**` 등)는 기능 변경 커밋 대상에서 제외합니다.

## 3) 공통 개발 규칙

- 문서, 주석, 커밋 메시지는 한글 우선으로 작성합니다.
- 작업 단위가 크면 `plans/`에 계획 문서를 먼저 작성하고 진행합니다.
- 기존 코드 스타일/네이밍/구조를 우선 따르고, 필요한 경우에만 점진적으로 개선합니다.
- 불필요한 선제 구현(미리 기능 만들기)을 금지합니다.
- 변경은 작은 단위로 나누고, 각 단위마다 검증 결과를 남깁니다.

## 4) 백엔드 규칙 (Spring Boot 3.4.3 / Java 21)

### 아키텍처

- 계층 흐름은 `Controller -> Service -> Implement -> Repository`를 기본으로 사용합니다.
- 목적:
  - `Service`에는 비즈니스 시나리오가 한눈에 보이게 유지
  - 상세 쿼리 조합/구체 구현은 `Implement` 레이어로 분리
- `SubwayStationReader` 같은 중간 추상화/구현 분리 패턴을 권장합니다.

#### 레이어 참조 규칙

- 공통 규칙: 2단계 아래 레이어 직접 참조 금지
  - `Controller`는 `Implement`/`Repository` 직접 참조 금지
  - `Service`는 `Repository` 직접 참조 금지
- 원칙적으로 자기 레이어 내부 직접 참조를 금지합니다.
- 예외적으로 `Implement` 레이어만 자기 레이어 참조를 허용합니다.
  - 단, 순환 참조(양방향 의존)는 금지합니다.
  - 필요 시 역할을 더 작은 단위 클래스로 분리합니다.

### API/응답

- API 응답은 `ApiResponse<T>` 래퍼를 사용합니다.
- 에러는 `CoreException` + `ApiControllerAdvice` 규약을 따릅니다.
- 새 엔드포인트는 `api/v1/**` 네이밍 규칙을 따릅니다.

### 엔티티/영속성

- 엔티티에 `@Setter`를 사용하지 않습니다.
- 생성은 정적 팩토리/빌더 중심으로 작성합니다.
- Soft delete는 현재 코드베이스 관례를 따릅니다.
  - `@SQLDelete` + `@SQLRestriction("deleted_at is null")`
- 시퀀스 전략 변경 시 DB 시퀀스 이름/`@SequenceGenerator` 정합성을 반드시 확인합니다.

### 보안/인증

- Security 설정은 `SecurityConfig` 중심으로 변경합니다.
- 인증 예외 응답 포맷은 `CustomAuthenticationEntryPoint` 규약을 유지합니다.
- 세션 기반 인증 전제(`withCredentials`)를 깨는 변경을 금지합니다.

## 5) 프론트엔드 규칙 (Next.js App Router / TypeScript / Tailwind)

### 구조

- 라우트: `src/app/**`
- 공통 컴포넌트: `src/components/common/**`
- 레이아웃 컴포넌트: `src/components/layout/**`
- 기능 컴포넌트: `src/components/features/**`
- API 클라이언트: `src/services/**`
- 상태: `src/store/**`

### 인증 상태 관리

- 전역 인증 소스는 `useAuthStore` + `AuthSync`로 유지합니다.
- 보호 페이지 가드는 `isLoggedIn`만 보지 말고 `authChecked`를 함께 사용합니다.
- `api.ts` 401 인터셉터 규칙을 우회하지 않습니다.
  - `api/v1/auth/me` 요청의 401은 상태 동기화 중심
  - 일반 401은 비인증 상태 처리 + 로그인 이동

### UI/디자인

- 디자인 기준은 `frontend/design/SKILL.md`를 우선 참조합니다.
- Tailwind 유틸리티 사용을 기본으로 하고, 인라인 스타일은 최소화합니다.
- 아이콘은 Material Symbols Outlined 규칙을 유지합니다.

### API 연동

- 백엔드 명세는 `api/backend-api/**`를 먼저 확인하고 구현합니다.
- API 통신은 `services/`에 모아 관리합니다(페이지/컴포넌트 직접 axios 호출 지양).

## 6) 테스트/검증 규칙

### 백엔드

- 기본: 실패 테스트를 먼저 작성(TDD).
- 테스트 스택: JUnit5 + AssertJ.
- 검증 명령:
  - `./gradlew :backend:test`
  - 문서 포함 검증 시 `./gradlew :backend:build`
- API 문서 산출물은 `api/backend-api/` 동기화 상태를 확인합니다.

### 프론트엔드

- 최소 검증:
  - `cd frontend && npm run lint`
- 기능 영향이 크면 `npm run build`까지 확인합니다.

## 7) 코드 리뷰 기준

모든 변경은 아래 우선순위로 자체 리뷰합니다.

1. Blocking 이슈 (런타임 오류, 보안, 데이터 손상, 명백한 회귀)
2. 일관성 (기존 패턴/규약 위반 여부)
3. 유지보수성 (복잡도, 결합도, 책임 분리)
4. 네이밍 품질 (변수/함수/클래스 의도 전달력)
5. 객체지향 설계 품질 (응집도, 책임, 패턴 적합성)

## 8) 금지/주의 사항

- 무관한 파일 포맷팅/대규모 정리는 요청 없이는 수행하지 않습니다.
- 프론트에서 백엔드 내부 구현을 가정한 하드코딩을 금지합니다.
- 백엔드에서 프론트 UI 요구사항을 추측해 API 계약을 임의 변경하지 않습니다.
- 사용자가 요청하지 않은 파괴적 Git 명령을 금지합니다.

## 9) 실무 운영 팁 (Codex)

- 작업 시작 시 영향 범위를 먼저 확정하고, 변경 파일 목록을 명시합니다.
- 백엔드/프론트 병렬 작업이 필요하면 역할을 분리한 워커를 즉시 생성해 진행합니다.
- 완료 보고에는 반드시 포함합니다.
  - 무엇을 변경했는지
  - 왜 변경했는지
  - 어떤 검증을 통과했는지
  - 남은 리스크/후속 과제
