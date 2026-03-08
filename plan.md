# 프로젝트 폴더 구조 변경 계획

이 계획은 기존의 Spring Boot 백엔드 코드를 `backend` 디렉토리로 이동하고, 새로운 프론트엔드 프로젝트를 위한 `frontend` 디렉토리를 생성하여 프론트엔드와 백엔드를 분리하는 것을 목표로 합니다.

## 1. 현재 구조 분석
현재 루트 디렉토리는 Spring Boot(Gradle) 프로젝트 구성 요소들로 채워져 있습니다.
- `src/`: 백엔드 소스 코드 및 Thymeleaf 템플릿
- `gradle/`, `build.gradle`, `settings.gradle`, `gradlew`: Gradle 빌드 도구
- `build/`: 빌드 결과물

## 2. 변경 후 제안 구조
```text
/datecourse
├── backend/                # 기존 Spring Boot 프로젝트 이동
│   ├── src/
│   ├── gradle/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── gradlew
│   └── ...
├── frontend/               # 새로운 프론트엔드 프로젝트 (예: React, Vue, Next.js 등)
│   ├── public/
│   ├── src/
│   ├── package.json
│   └── ...
├── .gitignore              # 전체 프로젝트 공통 ignore 처리
├── README.md               # 프로젝트 전체 설명
└── plan.md                 # 본 변경 계획서
```

## 3. 단계별 수행 작업

### 1단계: 디렉토리 생성
- 루트에 `backend/` 및 `frontend/` 디렉토리를 생성합니다.

### 2단계: 백엔드 파일 이동
- 다음 파일 및 디렉토리를 `backend/` 폴더로 이동합니다:
  - `src/`
  - `gradle/`
  - `build.gradle`
  - `settings.gradle`
  - `gradlew`
  - `gradlew.bat`
  - `.gitattributes` (백엔드 전용 설정일 경우)
  - `README.md` (기존 내용은 백엔드용으로 보존)

### 3단계: 프론트엔드 초기화 및 구현
- `frontend/` 디렉토리에서 **Next.js (App Router, TypeScript, Tailwind CSS)**를 초기화하고 구현을 시작합니다.
- 상세 구현 계획은 [plans/frontend-implementation-v1.md](plans/frontend-implementation-v1.md)를 참조하십시오.
- `frontend/design/SKILL.md` 및 `frontend/FRONTEND.md` 지침을 엄격히 준수합니다.

### 4단계: 설정 업데이트
- 루트 디렉토리에 전체 프로젝트를 아우르는 `.gitignore` 및 `README.md`를 새로 작성하거나 수정합니다.
- IDE(IntelliJ 등) 설정을 새로운 구조에 맞게 다시 로드합니다.

## 4. 주의 사항
- **경로 참조 수정**: 빌드 스크립트나 외부 연동 설정에서 상대 경로를 사용하는 경우 수정이 필요할 수 있습니다.
- **Git 히스토리**: `git mv` 명령어를 사용하여 파일 이동 시 히스토리가 유지되도록 합니다.
- **Thymeleaf 제거**: 프론트엔드를 별도로 구축하는 경우, 기존 `src/main/resources/templates`에 있는 SSR 관련 코드들을 점진적으로 API 기반으로 전환해야 합니다.
