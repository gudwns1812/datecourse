# Datecourse

Datecourse는 "오늘 어디서 만날까?"를 빠르게 결정하기 위한 데이트 코스 탐색 서비스입니다.
사용자는 로그인 후 지역 조건으로 지하철역을 랜덤 추첨하고, 선택한 역 또는 현재 위치를 기준으로 주변 장소를 지도에서 탐색할 수 있습니다.

## 한눈에 보기

- 목적: 랜덤 역 추천과 지도 기반 장소 탐색으로 데이트 의사결정 비용을 줄입니다.
- 핵심 흐름: 회원 인증 -> 랜덤 역 추첨 -> 지도에서 주변 장소 탐색
- 저장소 형태: 백엔드와 프론트엔드를 분리한 모노레포

## 주요 기능

### 1. 인증

- 일반 회원가입과 로그인 API를 제공합니다.
- 세션 기반 인증을 사용하며 프론트는 `withCredentials`를 전제로 동작합니다.
- 카카오 OAuth2 로그인 후 추가 회원정보 입력 플로우를 지원합니다.
- 프론트 전역 인증 상태는 `useAuthStore`와 `AuthSync`로 동기화합니다.

### 2. 랜덤 역 추첨

- 인증된 사용자가 시/도, 구/군 필터를 선택해 랜덤 역을 조회할 수 있습니다.
- 백엔드는 `SubwayStation` 후보를 읽고 `RandomStationGenerator`로 1개를 선택합니다.
- 프론트는 `/stations/random`에서 추첨 애니메이션과 지도 이동을 연결합니다.

### 3. 주변 장소 탐색

- 선택한 역 또는 현재 위치를 기준으로 장소를 검색합니다.
- 지도 viewport bounds를 함께 보내 현재 보이는 영역 안에서만 재검색할 수 있습니다.
- 장소는 거리와 텍스트 매칭을 합산한 `recommendationScore` 기준으로 정렬됩니다.
- 위치 조회 실패 시 프론트는 서울시청 좌표를 fallback 기준점으로 사용합니다.

### 4. 태그 자동 적재 운영 스크립트

- `scripts/place_tag_backfill.py`가 카카오 Local API와 내부 규칙을 이용해 `tag`, `place_tag`를 적재합니다.
- 사용 가이드는 `docs/place-tag-backfill.md`에 정리되어 있습니다.

## 모노레포 구조

```text
/datecourse
├── api/backend-api/        # 백엔드 API 문서 산출물
├── backend/                # Spring Boot API 서버
├── docs/                   # 운영/보조 문서
├── frontend/               # Next.js App Router 프론트엔드
├── plans/                  # 작업 계획 문서
├── scripts/                # 운영 자동화 스크립트
├── AGENTS.md               # Codex 작업 규칙
├── GEMINI.md               # 공통 개발 지침
├── build.gradle            # 루트 Gradle 설정
└── settings.gradle         # 모노레포 모듈 설정
```

## 아키텍처

### 백엔드

- 기술 스택: Java 21, Spring Boot 3.4.3, Spring Web, Spring Data JPA, Spring Security, Spring Session JDBC, OAuth2 Client,
  QueryDSL, PostgreSQL, Hibernate Spatial
- 기본 계층: `Controller -> Service -> Implement/Reader -> Repository`
- 응답 형식: `ApiResponse<T>` 래퍼 사용
- 예외 처리: `CoreException`, `ErrorType`, `ApiControllerAdvice`
- 인증 방식: 세션 기반 인증 + Spring Security + OAuth2(Kakao)
- 소프트 삭제: `@SQLDelete` + `@SQLRestriction("deleted_at is null")`

### 프론트엔드

- 기술 스택: Next.js 16(App Router), React 19, TypeScript, Tailwind CSS v4, Zustand, Axios, Framer Motion
- 주요 레이어
    - `src/app/**`: 라우트와 페이지 조합
    - `src/components/**`: 공통/레이아웃/기능 컴포넌트
    - `src/services/**`: API 호출 레이어
    - `src/store/**`: 인증 상태 저장소
    - `src/data/**`: 필터/카테고리 정적 데이터
- 지도: 카카오 지도 JS SDK 사용
- 인증 UX: `AuthSync`가 초기 세션 체크와 focus/visibility 재동기화를 담당합니다.

## 핵심 라우트

| 경로                 | 설명                     |
|--------------------|------------------------|
| `/`                | 메인 랜딩 + 랜덤 추첨 진입       |
| `/login`           | 일반 로그인 + 카카오 로그인 진입    |
| `/signup`          | 일반 회원가입 + 카카오 추가 정보 입력 |
| `/stations/random` | 지역 필터 선택 및 랜덤 역 추첨     |
| `/stations/map`    | 기준 위치 주변 장소 탐색 지도      |

## 핵심 API

| 메서드    | 경로                        | 설명                       |
|--------|---------------------------|--------------------------|
| `POST` | `/api/v1/auth/signup`     | 회원가입                     |
| `POST` | `/api/v1/auth/login`      | 일반 로그인                   |
| `GET`  | `/api/v1/auth/check-id`   | 아이디 중복 확인                |
| `GET`  | `/api/v1/auth/me`         | 현재 세션 사용자 확인             |
| `GET`  | `/api/v1/stations/random` | 지역 조건 기반 랜덤 역 조회         |
| `GET`  | `/api/v1/places`          | 기준 좌표/지도 bounds 기반 장소 조회 |

참고:

- 상세 API 문서는 `api/backend-api/member.md`, `api/backend-api/station.md`, `api/backend-api/place.md`에 있습니다.
- 현재 지역 필터 옵션 목록은 프론트 `frontend/src/data/filterOptions.ts`에 정적으로 정의되어 있습니다.

## 규약 및 스킬 문서

프로젝트를 수정할 때 먼저 아래 문서를 확인하는 흐름을 권장합니다.

| 문서                           | 역할                          |
|------------------------------|-----------------------------|
| `AGENTS.md`                  | 저장소 전체 작업 규칙, 소유권, 검증 원칙    |
| `GEMINI.md`                  | 공통 개발 원칙과 문서/커밋 가이드         |
| `backend/BACKEND.md`         | 백엔드 아키텍처, TDD, REST Docs 규칙 |
| `frontend/FRONTEND.md`       | 프론트 구조, API 연동, 컴포넌트 지침     |
| `frontend/design/SKILL.md`   | 프론트 디자인 시스템과 UI 톤           |
| `docs/place-tag-backfill.md` | 태그 자동 적재 스크립트 운영 가이드        |

## 로컬 실행

### 사전 준비

- JDK 21
- Node.js 20 이상 권장
- PostgreSQL
- 장소 검색 기능까지 사용하려면 PostGIS가 활성화된 DB 권장
- 카카오 로그인/지도 기능을 쓰려면 카카오 앱 키 필요

### 백엔드 실행

루트에서 실행합니다.

```bash
./gradlew :backend:bootRun
```

주요 검증 명령:

```bash
./gradlew :backend:test
./gradlew :backend:build
```

### 프론트엔드 실행

```bash
cd frontend
npm install
npm run dev
```

주요 검증 명령:

```bash
cd frontend && npm run lint
cd frontend && npm run build
```

## 환경 변수와 설정 포인트

### 프론트엔드

| 이름                         | 설명                |
|----------------------------|-------------------|
| `NEXT_PUBLIC_API_BASE_URL` | 백엔드 API base URL  |
| `NEXT_PUBLIC_KAKAO_JS_KEY` | 카카오 지도 JS SDK 앱 키 |

### 백엔드

| 설정                                | 설명                       |
|-----------------------------------|--------------------------|
| `spring.datasource.*`             | PostgreSQL 연결 정보         |
| `spring.security.oauth2.client.*` | 카카오 OAuth2 설정            |
| `app.frontend.base-url`           | OAuth 성공 후 리다이렉트될 프론트 주소 |
| `app.frontend.signup`             | 게스트 사용자의 추가 정보 입력 경로     |
