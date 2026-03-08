# 작업 계획서: Spring REST Docs 기반 API 명세서 생성 (backend-api-docs-v1)

이 계획은 `backend/BACKEND.md` 지침에 따라 `MockMvc`와 Spring REST Docs를 결합하여 백엔드 API 명세서를 자동 생성하는 것을 목표로 합니다.

## 1. 개요
- **작업 목적**: 주요 API(`Login`, `RandomStation`)에 대한 REST Docs 테스트 코드를 작성하고, 빌드 시 `root/api/backend-api/index.html` 문서가 자동 생성되도록 함.
- **주요 변경 사항**: REST Docs용 공통 테스트 설정 추가, 컨트롤러 테스트 코드 작성, `.adoc` 템플릿 작성.

## 2. 세부 작업 단계

### 1단계: REST Docs 공통 설정 및 문서 템플릿 생성
- **Base 테스트 클래스 또는 설정**: `MockMvc`와 `RestDocumentationResultHandler`를 연동하는 공통 설정을 검토합니다.
- **`src/docs/asciidoc/index.adoc` 작성**: 생성된 스니펫들을 통합하여 하나의 HTML 문서로 묶어줄 메인 템플릿 파일을 생성합니다.

### 2단계: 컨트롤러 테스트 구현 (TDD 및 문서화)
- **`LoginApiControllerTest`**: 로그인 및 회원가입 API에 대해 `MockMvc`와 REST Docs를 적용한 테스트 코드를 작성합니다.
    - 성공 케이스 및 주요 예외 케이스(400 등) 포함.
- **`RandomStationControllerTest`**: 무작위 역 조회 API에 대해 테스트 코드를 작성합니다.

### 3단계: 빌드 및 문서 배포 검증
- `./gradlew :backend:clean :backend:build` 실행.
- `backend/build/docs/asciidoc/index.html` 생성 확인.
- `root/api/backend-api/index.html`로 정상 복사되었는지 최종 확인.

## 3. 예상 결과물
- `backend/src/docs/asciidoc/index.adoc` 메인 문서 템플릿.
- REST Docs가 적용된 컨트롤러 테스트 코드들.
- `root/api/backend-api/index.html` (최종 API 명세서).

## 4. 커밋 예정 메시지 (승인 후 진행)
- `test: [backend] Spring REST Docs를 이용한 API 명세서 자동 생성 테스트 추가`
