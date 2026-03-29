# 로그인 인증 로직 개선 계획

## 문제 정의

| # | 현상 | 원인 |
|---|------|------|
| 1 | 로그인 상태에서 `/login`, `/signup` 진입 가능 | 두 페이지에 리다이렉트 처리 없음 |
| 2 | 세션 만료 후 401 에러 발생 시 로그인 화면으로 이동하지 않음 | `api.ts` 인터셉터가 401을 잡아도 아무 처리 없음 |
| 3 | 서버 세션이 만료되어도 프론트가 로그인 상태로 유지 | `isLoggedIn`이 로컬스토리지 값에만 의존 (서버 상태 미확인) |

---

## 개선 방향

### 1. 401 자동 로그아웃 및 리다이렉트 (`api.ts`)
- Axios 응답 인터셉터에서 **401 에러 감지 시** Zustand `logout()` 호출
- `window.location.href = "/login"` 으로 강제 이동
- 로컬스토리지의 인증 상태도 자동 초기화됨

### 2. 로그인/가입 페이지 진입 제한 (`login/page.tsx`, `signup/page.tsx`)
- 페이지 최상단에서 `isLoggedIn` 확인
- 로그인 상태이면 `router.push("/")` 로 즉시 리다이렉트

### 3. 앱 초기 로드 시 세션 검증 (`layout.tsx` or 전용 hook)
- 앱 마운트 시 `GET /api/v1/auth/me` (또는 동등한 인증 확인 API) 호출
- 서버 응답이 401이면 로컬 인증 상태 초기화 (`logout()`)
- 필요 시 `useAuthStore`에 `isHydrated` 플래그 추가해 SSR hydration mismatch 방지

---

## To-Do List

### Phase 1. 401 자동 처리
- [ ] `src/services/api.ts` 수정
  - 401 응답 인터셉터에서 `useAuthStore.getState().logout()` 호출
  - `window.location.href = "/login"` 리다이렉트 추가

### Phase 2. 진입 제한
- [ ] `src/app/login/page.tsx` 수정
  - `isLoggedIn === true`면 `router.push("/")` 처리
- [ ] `src/app/signup/page.tsx` 수정
  - `isLoggedIn === true`면 `router.push("/")` 처리

### Phase 3. 세션 동기화
- [ ] `src/hooks/useAuthSync.ts` 신규 생성
  - 앱 초기 마운트 시 서버에 세션 유효성 확인 API 호출
  - 401 응답 시 `logout()` + 로그인 페이지 이동
- [ ] `src/app/layout.tsx` 수정
  - 클라이언트 컴포넌트로 `AuthSync` wrapper 추가

---

## 변경 파일 요약

| 구분 | 파일 |
|------|------|
| 수정 | `src/services/api.ts` |
| 수정 | `src/app/login/page.tsx` |
| 수정 | `src/app/signup/page.tsx` |
| 신규 | `src/hooks/useAuthSync.ts` |
| 수정 | `src/app/layout.tsx` |

---

## 전제 조건

> [!IMPORTANT]
> Phase 3의 세션 동기화는 **서버에서 로그인 상태 확인용 API** (`GET /api/v1/members/me` 또는 유사)가 필요합니다.
> 해당 API가 없을 경우 Phase 1, 2만 먼저 진행하고 Phase 3은 나중에 합니다.
