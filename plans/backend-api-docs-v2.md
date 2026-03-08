# 작업 계획서: 도메인별 마크다운 API 명세서 자동 생성 (backend-api-docs-v2)

이 계획은 `backend/BACKEND.md` 지침을 기반으로 하되, AI 가독성과 유지보수성을 위해 **도메인별로 분리된 마크다운(`.md`) 명세서**를 자동으로 생성하는 것을 목표로 합니다.

## 1. 개요
- **작업 목적**: 테스트 코드를 통해 도메인별(Member, Station 등) API 명세 마크다운 파일을 생성하고, 이를 `root/api/backend/` 경로에 배포함.
- **주요 변경 사항**:
    - Spring REST Docs의 출력 형식을 마크다운으로 설정하거나 변환 로직 추가.
    - 도메인별 통합 마크다운 템플릿 작성.
    - Gradle의 `copyDocument` 태스크를 도메인별 분리 저장 방식으로 고도화.

## 2. 세부 작업 단계

### 1단계: 마크다운 생성을 위한 Gradle 및 환경 설정
- **Asciidoctor 설정 변경**: 기본 HTML 출력 대신 마크다운으로 변환하여 출력하도록 설정하거나, 마크다운 스니펫을 직접 생성하는 라이브러리 검토. (또는 `.adoc` 합본을 `.md`로 변환)
- **출력 경로 정의**: `root/api/backend/{domain-name}.md` 형식으로 저장되도록 설정.

### 2단계: 도메인별 마크다운 템플릿 작성
- `backend/src/docs/asciidoc/` 내에 도메인별 메인 템플릿 생성:
    - `member.adoc` (또는 `.md`)
    - `station.adoc` (또는 `.md`)

### 3단계: 도메인별 컨트롤러 테스트 구현 (TDD 준수)
- **`Member` 관련 API 테스트 (`LoginApiControllerTest` 등)**:
    - 회원가입, 로그인 API 문서화 테스트 작성.
    - 출력 스니펫들이 `member` 그룹으로 묶이도록 설정.
- **`Station` 관련 API 테스트 (`RandomStationControllerTest`)**:
    - 무작위 역 조회 API 문서화 테스트 작성.
    - 출력 스니펫들이 `station` 그룹으로 묶이도록 설정.

### 4단계: 빌드 및 자동 배포 검증
- `./gradlew :backend:clean :backend:build` 실행.
- 최종적으로 아래 파일들이 생성되는지 확인:
    - `root/api/backend/member.md`
    - `root/api/backend/station.md`

## 3. 예상 결과물
- 도메인별로 깔끔하게 정리된 마크다운 API 명세서 세트.
- AI가 읽기 최적화된 구조의 문서 저장소 (`root/api/backend/`).

## 4. 커밋 예정 메시지 (승인 후 진행)
- `test: [backend] 도메인별 마크다운 API 명세서 자동 생성 기능 및 테스트 추가`
