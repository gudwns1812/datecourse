# 작업 계획서: 백엔드 개발 환경 및 기본 설정 최신화 (backend-setup-v1)

이 계획은 `GEMINI.md`와 `backend/BACKEND.md`에 명시된 백엔드 개발 표준을 준수하기 위한 환경 설정 및 기존 코드 개선을 목표로 합니다.

## 1. 개요
- **작업 목적**: Java 21로의 버전 일치 및 Spring REST Docs 기반의 문서화 환경 구축.
- **주요 변경 사항**: Gradle 설정 변경, REST Docs 의존성 추가, 엔티티 설계 원칙(`@Setter` 금지, 정적 팩토리 메서드) 준수 점검 및 수정.

## 2. 세부 작업 단계

### 1단계: Gradle 설정 최신화 (`backend/build.gradle`)
- **Java 버전 변경**: `JavaLanguageVersion.of(25)` -> `JavaLanguageVersion.of(21)`.
- **Spring REST Docs 플러그인 추가**: `org.asciidoctor.jvm.convert` 플러그인 적용.
- **의존성 추가**: `spring-restdocs-mockmvc` (testImplementation).
- **테스트 및 스니펫 설정**: `test` 태스크 시 스니펫 생성 경로 지정.
- **문서 복사 태스크 구현**: `copyDocument` 태스크를 작성하여 빌드 후 `root/api/backend-api/index.html`로 문서가 자동 복사되도록 설정.

### 2단계: 기존 엔티티 검토 및 리팩토링 (`Member.java`, `Station.java` 등)
- **`@Setter` 제거**: 엔티티 내의 `@Setter`를 모두 제거하고, 필요한 경우 비즈니스적 의미를 담은 메서드로 대체.
- **정적 팩토리 메서드 도입**: `of()` 또는 `create()`와 같은 정적 팩토리 메서드를 추가하여 객체 생성 시 일관성 확보.
- **기존 테스트 코드 수정**: 버전 변경 및 엔티티 생성 방식 변화에 따른 영향도 파악 및 테스트 코드 업데이트.

### 3단계: 검증 (Validation)
- `./gradlew :backend:clean :backend:build` 명령어를 통해 전체 빌드 및 테스트 통과 여부 확인.
- `root/api/backend-api/index.html` 파일이 정상적으로 생성 및 복사되는지 확인.

## 3. 예상 결과물
- Java 21 기반의 백엔드 모듈.
- Spring REST Docs가 적용된 빌드 시스템.
- `BACKEND.md`를 준수하는 초기 엔티티 구조.

## 4. 커밋 예정 메시지
- `chore: [backend] Java 21 버전 변경 및 Spring REST Docs 환경 설정 추가`
- `refactor: [backend] 엔티티 설계 원칙 준수(Setter 제거 및 정적 팩토리 메서드 도입)`
