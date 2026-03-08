# 어디역? (Where Station?) Design System

이 문서는 Stitch에서 생성된 디자인을 바탕으로 정의된 '어디역?' 프로젝트의 UI/UX 가이드라인 및 디자인 시스템입니다. 프론트엔드 개발 시 이 명세에 따라 일관된 사용자 경험을 제공해야 합니다.

## 1. 디자인 철학 (Design Philosophy)
- **Playful & Modern**: 서울 지하철을 테마로 한 가볍고 즐거운 데이트 탐색 경험 제공.
- **Interactive**: 결정 장애를 해결해주는 '랜덤 뽑기' 요소와 직관적인 시각적 피드백 강조.
- **Mobile First**: 이동 중에도 편리하게 사용할 수 있는 반응형 레이아웃 지향.

## 2. 컬러 팔레트 (Color Palette)

| 구분 | 색상 코드 | 용도 |
| :--- | :--- | :--- |
| **Primary** | `#ee2b8c` | 브랜드 컬러, 버튼, 강조 텍스트, 아이콘 |
| **Background (Light)** | `#f8f6f7` | 라이트 모드 기본 배경색 |
| **Background (Dark)** | `#221019` | 다크 모드 기본 배경색 |
| **Surface (Light)** | `#ffffff` | 카드, 네비게이션 바 배경 |
| **Surface (Dark)** | `#1e293b` (slate-800/50) | 다크 모드 카드 배경 |
| **Text (Primary)** | `#0f172a` (slate-900) | 기본 텍스트 |
| **Text (Secondary)** | `#475569` (slate-600) | 설명 텍스트, 부가 정보 |

## 3. 타이포그래피 (Typography)
- **Primary Font**: `Plus Jakarta Sans`
- **Fallback**: `sans-serif`
- **Styles**:
  - **Hero Title**: `text-4xl` to `text-6xl`, `font-extrabold`, `tracking-tight`
  - **Section Heading**: `text-2xl`, `font-bold`
  - **Body Text**: `text-base` or `text-lg`, `font-normal`, `leading-relaxed`

## 4. 아이콘 시스템 (Iconography)
- **Library**: [Material Symbols Outlined](https://fonts.google.com/icons)
- **Key Icons**:
  - `subway` / `train`: 브랜드 로구 및 교통 상징
  - `restaurant`: 'Eat' 섹션
  - `visibility`: 'See' 섹션
  - `local_activity`: 'Do' 섹션
  - `casino`: 랜덤 뽑기 버튼
  - `location_on`: 장소 표시

## 5. 핵심 컴포넌트 구조

### 5.1 네비게이션 바 (Navigation Bar)
- **특징**: `sticky top-0`, `backdrop-blur-md`, 하단 보더 `border-primary/10`.
- **구성**: 서비스 로고 (`어디역?`), 메뉴 링크(데스크탑), 알림 및 프로필 버튼.

### 5.2 럭키 드로우 (Lucky Draw Section)
- **디자인**: 거대한 원형 휠 그래픽 중심.
- **인터랙션**: 중앙에 `location_on` 아이콘과 `??? 역` 텍스트 배치.
- **CTA**: `랜덤으로 데이트역 뽑기` 버튼 (Shadow `shadow-primary/40` 포함).

### 5.3 장소 추천 카드 (Recommendation Cards)
- **레이아웃**: 좌측(또는 상단) 이미지 + 우측(또는 하단) 상세 정보.
- **요소**: 장소명, 가격 지표(`$`, `$$$`, `Free`), 설명, 태그 배지.
- **호버 효과**: 이미지 확대 (`scale-105`) 및 그림자 강화 (`shadow-md`).

### 5.4 지하철 호선 배지 (Line Badges)
- **예시**: `bg-[#9966cc]/10 text-[#9966cc]` (5호선 예시).
- **형태**: `rounded-full`, 작은 원형 색상 포인트 포함.

## 6. 레이아웃 가이드
- **Container**: `max-w-7xl` (메인), `max-w-4xl` (상세 페이지).
- **Padding**: 모바일 `px-6`, 데스크탑 `px-10`.
- **Border Radius**: 기본 `0.5rem`, 강조 컴포넌트 `1rem` (`lg`) ~ `1.5rem` (`xl`).

---
*본 문서는 `stitch_.zip`의 `code.html` 분석을 통해 작성되었습니다.*
