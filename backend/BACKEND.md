# BACKEND.md - 백엔드 개발 지침

## 🏗 아키텍처 원칙

- **계층형 아키텍처 (Layered Architecture)**:
    - `Controller -> Service -> Repository` 순서의 단방향 흐름을 엄격히 준수합니다.
    - 상위 계층이 하위 계층을 의존하며, 역방향 참조나 계층 건너뛰기는 금지합니다.

## 🧪 테스트 및 품질

- **TDD (Test Driven Development)**:
    - 모든 기능 구현 전, 실패하는 테스트 코드를 먼저 작성하십시오.
    - JUnit 5와 AssertJ를 사용하여 단위 테스트를 구성합니다.
- **Clean Code**: 메서드는 하나의 일만 하며, 20줄을 넘지 않도록 분리합니다.

## 🛠 기술 스택 세부사항

- Java 21 / Spring Boot 3.4.3 / JPA (Hibernate) / Spring REST Docs
- Entity 작성 시 `@Setter` 사용 금지, 정적 팩토리 메서드 사용 권장.

## 📚 API 문서화 (API Documentation)

- **Spring Rest Docs 사용**: 모든 Controller 테스트는 `MockMvc`와 Spring Rest Docs를 결합하여 API 명세서를 자동 생성해야 합니다.
- **테스트 기반 문서**: 성공하는 테스트 케이스뿐만 아니라, 주요 예외 상황(400, 404 등)에 대한 스니펫도 반드시 포함합니다.
- **산출물 관리 규칙**:
    - 빌드 시 생성된 `.adoc` 또는 `.html` 명세서는 프로젝트 루트의 `api/{api-name}/` 경로로 배포되어야 합니다.
    - (예: `backend` 모듈의 유저 API -> `root/api/user-api/index.html`)

### 🛠 실행 지침 (Implementation)

1. 테스트 클래스에 `@AutoConfigureRestDocs`를 사용하십시오.
2. 테스트 작성시에 최대한 Mock 테스트(스프링 컨테이너를 띄우는 테스트)를 쓰지말고 최대한 스텁으로 해결한다. 단. 외부 라이브러리를 쓰면서 너무 스텁을 만들기 지저분해지면 Mock 테스트를 허용한다.
3. `build.gradle`의 `copyDocument` 태스크를 설정하여, 빌드 완료 후 `build/docs/asciidoc`의 결과물을 루트의 `api/` 폴더로 복사하도록 구성합니다.
4. AI는 새로운 API 개발 시, 해당 API에 대응하는 `RestDocs`용 테스트 코드를 반드시 제안해야 합니다.
