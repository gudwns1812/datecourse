# [Frontend] 개발 지침 및 가이드라인

이 문서는 '어디역?' 프로젝트의 프론트엔드 개발을 위한 기술 스택, 구조, 그리고 `frontend/design/SKILL.md`에 정의된 디자인 시스템을 반영한 상세 지침을 담고 있습니다.

## 1. 기술 스택 (Tech Stack)

- **Framework**: `Next.js` (App Router 권장)
- **Language**: `TypeScript`
- **Styling**: `Tailwind CSS`
- **Icon Library**: `Material Symbols Outlined`
- **State Management**: `React Context API` 또는 `Zustand` (필요 시)
- **Data Fetching**: `React Query (TanStack Query)`

## 2. 디자인 시스템 연동 (Tailwind Configuration)

`frontend/design/SKILL.md`의 내용을 바탕으로 `tailwind.config.js`를 다음과 같이 설정해야 합니다.

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
    darkMode: 'class', // 다크 모드 지원
    theme: {
        extend: {
            colors: {
                primary: '#ee2b8c',
                'background-light': '#f8f6f7',
                'background-dark': '#221019',
                // 추가적인 surface 및 text 컬러는 slate 계열 활용
            },
            fontFamily: {
                display: ['Plus Jakarta Sans', 'sans-serif'],
            },
            borderRadius: {
                DEFAULT: '0.5rem',
                'lg': '1rem',
                'xl': '1.5rem',
            },
            boxShadow: {
                'primary-glow': '0 10px 15px -3px rgba(238, 43, 140, 0.4)',
            }
        },
    },
}
```

## 3. 프로젝트 구조 (Directory Structure)

```text
frontend/
├── src/
│   ├── app/                # Next.js App Router (Pages, Layouts)
│   ├── components/         # 재사용 가능한 UI 컴포넌트
│   │   ├── common/         # Button, Input, Badge 등 기초 컴포넌트
│   │   ├── layout/         # Header, Footer, Sidebar
│   │   └── features/       # 특정 기능 전용 컴포넌트 (LuckyDraw, StationCard)
│   ├── hooks/              # 커스텀 훅
│   ├── services/           # API 호출 로직
│   ├── store/              # 상태 관리 (Zustand 등)
│   ├── types/              # TypeScript 인터페이스/타입 정의
│   └── utils/              # 유틸리티 함수
├── public/                 # 정적 자원 (Images, Fonts)
└── design/                 # 디자인 시스템 문서 (SKILL.md 등)
```

## 4. 컴포넌트 개발 지침

### 4.1 기본 원칙

- 모든 컴포넌트는 **Functional Component**와 **Hooks**를 사용합니다.
- **Atomic Design** 개념을 참고하여 컴포넌트를 분리하되, 복잡성을 최소화합니다.
- `SKILL.md`의 **Mobile First** 원칙에 따라 모든 컴포넌트는 반응형으로 구현합니다.

### 4.2 주요 컴포넌트 구현 참고

- **LuckyDraw**: 중앙의 원형 휠은 CSS Animation 또는 Framer Motion을 사용하여 역동적인 회전 효과를 구현합니다.
- **StationCard**: 호버 시 `scale-105` 및 `shadow-md` 효과를 적용하여 인터랙티브한 경험을 제공합니다.
- **LineBadge**: 호선별 고유 컬러를 `SKILL.md`의 가이드에 따라 적용합니다.

### 4.3 디자인 시스템 외 컴포넌트 처리

- 개발 과정에서 `frontend/design/SKILL.md`에 명시되지 않은 새로운 컴포넌트가 필요한 경우:
    1. **Stitch에 문의**: 필요한 컴포넌트의 디자인 가이드를 Stitch를 통해 생성합니다.
    2. **직접 디자인 유추**: Stitch 사용이 어려운 경우, 기존 디자인 시스템의 컬러, 간격, 스타일을 바탕으로 일관성 있게 유추하여 구현합니다.
    3. **SKILL.md 업데이트**: 새롭게 정의된 컴포넌트의 디자인 명세를 반드시 `frontend/design/SKILL.md`에 추가하여 시스템의 일관성을 유지합니다.

## 5. 개발 표준 (Coding Standards)

- **Naming**: 파일 및 디렉토리명은 `kebab-case`, 컴포넌트명은 `PascalCase`를 사용합니다.
- **Accessibility (A11y)**: 시각 장애인을 위한 ARIA 속성을 준수하고, 적절한 대체 텍스트를 제공합니다.
- **Performance**: 이미지 최적화를 위해 Next.js의 `next/image`를 필수 사용합니다.
- **Consistency**: Tailwind 클래스 순서는 일관성을 위해 권장되는 순서(또는 Prettier 플러그인)를 따릅니다.

## 6. API 연동 가이드

- **필수 참조**: 백엔드 API 명세는 루트 디렉토리의 `api/backend-api/`에 상세히 정의되어 있습니다. **기능 구현 전 반드시 해당 문서를 확인**해야 하며, 이를 어길 경우 통신 에러가 발생할
  수 있습니다.
- **통신 로직**: 백엔드 API와의 모든 통신은 `services/` 디렉토리에 정의합니다.
- **환경 변수**: `.env.local`을 사용하여 API 베이스 URL 및 필요한 시크릿을 관리합니다.
- **에러 핸들링**: 전역 Error Boundary 또는 React Query의 에러 처리를 활용하여 사용자에게 적절한 피드백을 제공합니다.
- **오버 엔지니어링 금지** : 아직 만들지도 않은 기능을 미리 만들면 안됩니다.

## 7. 테스트 및 검증 (Testing & Verification)

- **TDD 지향**: 기능을 추가하거나 수정할 때는 반드시 테스트 코드를 동반해야 합니다.
- **단위 테스트**: `Jest` 및 `React Testing Library`를 사용하여 개별 컴포넌트와 유틸리티 함수의 동작을 검증합니다.
- **E2E 테스트**: 주요 사용자 시나리오(예: 랜덤 역 뽑기, 로그인)에 대해서는 `Cypress` 또는 `Playwright`를 활용한 검증을 권장합니다.
- **검증 필수**: 기능 구현 후에는 작성된 테스트를 모두 통과해야 하며, 테스트 없는 기능 추가는 지양합니다.

---
*참고: 상세 디자인 명세는 `frontend/design/SKILL.md`를 항상 우선적으로 참조하십시오.*
