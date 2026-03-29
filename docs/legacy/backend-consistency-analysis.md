# 백엔드 코드 통일성 분석 보고서

## 1. 컨트롤러 계층: 입력 검증 및 일관성

- **유효성 검사(@Valid) 누락**: `LoginApiController.signup` 메서드에서 `RegisterForm`을 인자로 받지만, `@Valid` 어노테이션이 누락되어 있습니다. 이로 인해
  `RegisterForm` 내부의 제약 조건(@NotEmpty, @Size 등)이 실제 컨트롤러에서 작동하지 않습니다.
- **예외 처리 방식의 파편화**: `ApiControllerAdvice`에 `MethodArgumentNotValidException`에 대한 전용 핸들러가 없어, 검증 실패 시 클라이언트에게 전달할 표준화된
  에러 응답이 부족합니다.

## 2. 서비스 계층: 트랜잭션 및 예외 전략

- **트랜잭션 어노테이션 누락**: `LoginService`는 `@Transactional(readOnly = true)`를 적절히 사용하고 있으나, `DateCourseService`에는 트랜잭션 설정이 완전히
  누락되어 있습니다. 일관성을 위해 모든 서비스 클래스 레벨에 읽기 전용 트랜잭션을 설정하는 것이 권장됩니다.
- **부적절한 에러 타입 사용**: `LoginService.saveMember`에서 중복 아이디 체크 시 `ErrorType.INTERNAL_SERVER_ERROR`를 던지고 있습니다. 이는 시스템 오류가 아닌
  비즈니스 로직 예외이므로, `ErrorType.DUPLICATE_LOGIN_ID`와 같은 명확한 에러 타입을 정의해야 합니다.

## 3. DTO 및 패키지 구조

- **DTO 구현 방식 혼용**: `LoginForm`과 `StationResponseDto`는 Java `record`를 사용하는 반면, `RegisterForm`은 Lombok의 `@Data` 클래스를 사용하고
  있습니다. 불변성을 위해 DTO 구현 방식을 `record`로 통일하는 것이 좋습니다.
- **파편화된 패키지 구조**: DTO들이 `web.controller.dto`와 `service.dto`에 흩어져 있습니다. DTO의 위치(예: Web 계층)를 표준화하여 유지보수성을 높일 필요가 있습니다.

## 4. 도메인(엔티티) 계층: 구현 패턴

- **엔티티 구현의 불일치**: `Member`는 JPA 엔티티로서 Lombok의 `@Builder`를 사용하는 반면, `Station`은 수동 생성자를 사용하는 일반 클래스입니다.
- **Setter 사용 지양**: 컨트롤러에서 `form.setProviderId(providerId)`와 같이 Setter를 사용하여 요청 객체의 상태를 변경하고 있습니다. 이를 서비스 로직 내부로 옮기거나 빌더
  패턴 등을 사용하여 객체의 일관성을 유지하는 것이 바람직합니다.

---

## 개선 제안

1. **검증 활성화**: 모든 컨트롤러 입력 DTO에 `@Valid`를 추가하고 검증 에러를 전역적으로 처리하세요.
2. **트랜잭션 표준화**: 모든 서비스 클래스 상단에 `@Transactional(readOnly = true)`가 포함되도록 하세요.
3. **DTO를 Record로 통일**: 기존 `@Data` 기반 DTO들을 가능한 한 `record` 타입으로 전환하세요.
4. **에러 타입 세분화**: 일반적인 서버 에러 대신 비즈니스 예외 상황에 맞는 세부 `ErrorType`을 정의하여 사용하세요.
