# 작업 계획서: 세션 저장소 전환 및 Spring Security 도입 (backend-security-refactoring-v1)

이 계획은 백엔드의 세션 관리 인프라를 확장성 있게 개선하고(JDBC 세션), 보안 아키텍처를 표준 방식(Spring Security)으로 전환하는 것을 목표로 합니다.

## 1. 개요
- **작업 목적**: 
    - 인메모리 세션을 **Spring Session JDBC**로 전환하여 서버 재시작 시에도 세션 유지 및 확장성 확보.
    - 기존 `LoginInterceptor`를 **Spring Security**로 대체하여 보안 설정의 표준화 및 세밀한 권한 제어 기반 마련.
- **주요 변경 사항**:
    - `build.gradle` 의존성 추가 (Spring Session JDBC, Spring Security).
    - `application.yml` 세션 저장소 설정 및 스키마 자동 생성 설정.
    - `SecurityConfig` 클래스 생성 및 인증/인가 필터 체인 구성.
    - **환경별 CSRF 설정**: 
        - **`local` 환경**: 개발 편의를 위해 CSRF 비활성화.
        - **그 외 환경**: `CookieCsrfTokenRepository`를 사용하여 CSRF 보호 활성화.
    - `LoginApiController`의 로그인/로그아웃 로직을 Spring Security 방식으로 연동.

## 2. 세부 작업 단계

### 1단계: 의존성 및 환경 설정 최신화
- **의존성 추가**: `spring-session-jdbc`, `spring-boot-starter-security` 추가.
- **설정 추가**: `application.yml`에 `spring.session.store-type: jdbc` 및 세션 테이블 생성 관련 설정 추가.

### 2단계: Spring Security 구성 및 인터셉터 제거
- **`SecurityConfig` 작성**:
    - **프로필별 CSRF 제어**: `Environment`를 주입받아 `local` 프로필 확인 후 CSRF 활성/비활성 결정.
    - `/v1/auth/signup`, `/v1/auth/login` 등 화이트리스트 경로 허용.
    - 그 외 `/v1/**` 경로는 인증 필수 설정.
- **`WebConfig` 수정**: 기존 `LoginInterceptor` 등록 해제.
- **`UserDetailsService` 구현**: `MemberRepository`를 연동하여 Spring Security용 사용자 정보 로드 로직 구현.

### 3단계: 컨트롤러 및 서비스 리팩토링 (TDD 준수)
- **`LoginApiController` 수정**: 
    - 수동으로 세션을 제어하던 코드를 Spring Security의 `AuthenticationManager` 또는 기본 폼 로그인/핸들러 방식으로 연동.
- **테스트 코드 수정**:
    - `@WithMockUser` 및 `SecurityMockMvcConfigurers.springSecurity()`를 사용하여 보안 환경에서의 API 테스트 및 REST Docs 스니펫 갱신.

### 4단계: 검증 및 문서 업데이트
- `./gradlew :backend:clean :backend:build` 실행하여 전체 테스트 통과 확인.
- `root/api/backend-api/` 내의 명세서 마크다운 파일 갱신 여부 확인.

## 3. 예상 결과물
- DB(MySQL/H2)에 세션 데이터가 저장되는 구조.
- 환경에 따라 유연하게 작동하는 Spring Security 인증/인가 환경.
- 보안 아키텍처가 반영된 최신화된 API 명세서.

## 4. 커밋 예정 메시지 (승인 후 진행)
- `feat: [backend] Spring Session JDBC 도입 및 DB 기반 세션 관리 설정`
- `feat: [backend] Spring Security 도입 및 환경별(Local/Prod) CSRF 설정 차등 적용`
