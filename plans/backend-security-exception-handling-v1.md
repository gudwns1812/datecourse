# 백엔드 보안 예외 처리 개선 계획 (v1.0)

현재 Spring Security가 적용되어 있으나, 인증/인가 오류 발생 시 일관된 JSON 응답을 반환하는 설정이 부족합니다. 이를 개선하여 프론트엔드에서 명확한 에러 처리를 할 수 있도록 합니다.

## 1. 현재 문제점
- 인증되지 않은 사용자가 보호된 리소스(`anyRequest().authenticated()`)에 접근할 때, Spring Security의 기본 동작에 의해 403 Forbidden 또는 로그인 페이지 리다이렉션 등이 발생할 수 있습니다 (현재 `formLogin` 등이 비활성화되어 있어 기본 403 응답 가능성 높음).
- 기존 `ErrorType.UNAUTHORIZED_USER`를 활용한 커스텀 에러 응답 구조와 일치하지 않습니다.

## 2. 개선 방안

### 2.1 커스텀 AuthenticationEntryPoint 구현
- 인증되지 않은 사용자가 접근 시 `401 Unauthorized` 상태 코드와 함께 `ApiResponse.error(UNAUTHORIZED_USER)` 형태의 JSON을 반환하도록 합니다.

### 2.2 커스텀 AccessDeniedHandler 구현 (필요 시)
- 권한이 부족한 사용자가 접근 시 `403 Forbidden` 상태 코드와 함께 적절한 에러 메시지를 반환하도록 합니다 (현재는 역할 구분이 없으나 확장을 위해 고려).

### 2.3 SecurityConfig 업데이트
- 구현한 핸들러들을 `SecurityFilterChain`에 등록합니다.

## 3. 상세 작업 단계

1.  **`com.datecourse.support.auth` 패키지에 핸들러 클래스 생성**:
    - `CustomAuthenticationEntryPoint`: `AuthenticationEntryPoint` 구현. `ObjectMapper`를 사용하여 `ApiResponse`를 JSON으로 변환 후 응답.
2.  **`SecurityConfig` 수정**:
    - `exceptionHandling` 설정을 추가하여 생성한 EntryPoint 등록.

---
*모든 변경 사항은 기존 `ApiControllerAdvice`의 에러 응답 구조와 일관성을 유지합니다.*
