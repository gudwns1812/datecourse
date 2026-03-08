# 프론트엔드 구현 계획 (v1.0) - 어디역? (Where Station?)

이 문서는 `frontend/design/SKILL.md`와 `frontend/FRONTEND.md`의 지침을 바탕으로 '어디역?' 서비스의 프론트엔드를 구축하기 위한 상세 실행 계획을 담고 있습니다.

## 1. 준비 단계 (Environment Setup)

- [ ] **Next.js 프로젝트 초기화**: `frontend/` 디렉토리 내에서 Next.js (App Router, TypeScript, Tailwind CSS) 스캐폴딩.
  - 명령어: `npx create-next-app@latest . --typescript --tailwind --eslint --app --src-dir --import-alias "@/*"`
- [ ] **디자인 시스템 연동**: `tailwind.config.ts`에 `SKILL.md`의 컬러 팔레트, 폰트(Plus Jakarta Sans), 보더 라디우스 설정 반영.
- [ ] **에셋 준비**: `Material Symbols Outlined` 폰트 및 필요한 정적 이미지(로고 등) 배치.

## 2. 기초 골격 및 레이아웃 (Base & Layout)

- [ ] **글로벌 스타일**: `src/app/globals.css`에 배경색(`background-light/dark`) 및 기본 타이포그래피 적용.
- [ ] **공통 레이아웃**: `src/app/layout.tsx`에 `Header`와 `Footer` 컴포넌트 배치.
  - **Header**: 로고, 데스크탑 메뉴, 알림/프로필 버튼 (Sticky & Glassmorphism 효과).
  - **Footer**: 서비스 정보 및 하단 링크.

## 3. 핵심 컴포넌트 개발 (Core Components)

- [ ] **LuckyDraw (메인 기능)**:
  - 중앙의 거대한 원형 휠 구현.
  - `Framer Motion`을 활용한 회전 애니메이션 및 '랜덤 뽑기' 인터랙션 개발.
- [ ] **StationCard (추천 결과)**:
  - 장소 이미지, 제목, 가격 지표, 태그 배지 포함.
  - 호버 시 `scale-105` 및 그림자 효과 적용.
- [ ] **Common UI Components**:
  - `Button`, `Badge`, `Input` 등 재사용 가능한 원자(Atomic) 단위 컴포넌트 구현.

## 4. API 연동 및 상태 관리 (API & State)

- [ ] **API 서비스 레이어**: `src/services/`에 `api/backend-api/` 명세를 기반으로 한 Axios 또는 Fetch 인스턴스 설정.
  - `stationService.ts`: 랜덤 역 정보 및 주변 장소 데이터 호출.
  - `memberService.ts`: 로그인 및 회원 정보 관리.
- [ ] **상태 관리**: `React Query`를 사용하여 서버 데이터 캐싱 및 로딩/에러 상태 처리.

## 5. 테스트 및 검증 (Testing & Validation)

- [ ] **테스트 환경 설정**: `Jest` 및 `React Testing Library` 설치 및 구성.
- [ ] **단위 테스트**: `LuckyDraw`의 버튼 클릭 로직, `StationCard` 렌더링 검증.
- [ ] **통합 테스트**: 역 뽑기 클릭 후 결과 데이터가 화면에 올바르게 표시되는지 확인.

## 6. 최종 점검 및 빌드 (Finalization)

- [ ] **반응형 체크**: 모바일, 태블릿, 데스크탑 환경에서의 레이아웃 일관성 확인.
- [ ] **빌드 테스트**: `npm run build`를 통해 컴파일 에러 및 성능 최적화 상태 점검.

---
*모든 단계는 `FRONTEND.md`의 코딩 표준을 준수하며 진행합니다.*
