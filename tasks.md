# 보안 리팩토링 작업 체크리스트 (Security & Session)

## 🏁 1단계: 의존성 및 환경 설정 (`Done` 여부: [x])
- [x] `backend/build.gradle`에 Spring Security 및 Spring Session JDBC 의존성 추가
- [x] `application.yml`에 JDBC 세션 저장소 설정 추가 (`spring.session.store-type: jdbc`)
- [x] 세션 테이블 자동 생성을 위한 `jdbc.initialize-schema: always` 설정 (로컬 전용)

## 🛡 2단계: Spring Security 아키텍처 구축 (`Done` 여부: [x])
- [x] `SecurityConfig` 클래스 생성 및 기본 보안 필터 체인 구성
- [x] 프로필 기반(Local vs Prod) CSRF 활성/비활성 로직 구현
- [x] `UserDetails` 및 `UserDetailsService` 인터페이스 구현 (Member 엔티티 연동)
- [x] `WebConfig`에서 기존 `LoginInterceptor` 등록 코드 제거

## 🔄 3단계: 인증 로직 마이그레이션 (`Done` 여부: [x])
- [x] `LoginApiController`의 수동 세션 제어 로직을 Security 인증 로직으로 전환
- [x] `LoginService`의 리턴 타입 및 인증 검증 방식 조정 (Password 암호화 적용)
- [x] `LoginArgumentResolver`가 Security의 `Authentication`을 참조하도록 수정
- [x] `MemberInit` 초기 데이터 비밀번호 암호화 적용

## 🧪 4단계: 테스트 및 검증 (`Done` 여부: [x])
- [x] 컨트롤러 테스트에 `@WithMockUser` 및 Security MockMvc 설정 적용
- [x] `RandomStationControllerTest` 등 기존 테스트의 인증 통과 여부 재검토
- [x] 빌드 및 REST Docs 스니펫 갱신 확인
- [x] 최종 마크다운 명세서 업데이트

## 🚀 5단계: 마무리 (`Done` 여부: [x])
- [x] 전체 빌드 (`./gradlew clean build`) 수행
- [x] 작업 완료 보고 및 커밋 승인 요청
