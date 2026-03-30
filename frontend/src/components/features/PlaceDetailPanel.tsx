"use client";

import Badge from "@/components/common/Badge";
import { getPlaceCategoryMeta } from "@/data/placeCategories";
import { PlaceData } from "@/services/place";

interface PlaceDetailPanelProps {
  place: PlaceData | null;
  baseLocationName: string;
  isLoading: boolean;
  layout?: "rail" | "sheet";
}

const formatDistance = (meters: number) => {
  if (!Number.isFinite(meters) || meters <= 0) {
    return "거리 정보 없음";
  }

  if (meters >= 1000) {
    return `${(meters / 1000).toFixed(1)}km`;
  }

  return `${Math.round(meters)}m`;
};

const formatScore = (score: number) => {
  if (!Number.isFinite(score)) {
    return "0.0";
  }

  return score.toFixed(1);
};

export default function PlaceDetailPanel({
  place,
  baseLocationName,
  isLoading,
  layout = "rail",
}: PlaceDetailPanelProps) {
  const isRail = layout === "rail";
  const containerClassName = isRail
    ? "rounded-[2rem] border border-slate-200 bg-gradient-to-b from-white to-slate-50/90 p-5 shadow-[0_24px_70px_rgba(15,23,42,0.12)] dark:border-slate-700 dark:from-slate-900 dark:to-slate-950/70"
    : "rounded-t-[2rem] border border-slate-200 bg-white p-5 shadow-[0_-18px_40px_rgba(15,23,42,0.18)] dark:border-slate-700 dark:bg-slate-950";

  if (isLoading && !place) {
    return (
      <div className={containerClassName}>
        <div className="animate-pulse space-y-3">
          <div className="h-4 w-24 rounded-full bg-slate-200 dark:bg-slate-800" />
          <div className="h-7 w-3/4 rounded-full bg-slate-200 dark:bg-slate-800" />
          <div className="h-24 rounded-3xl bg-slate-200 dark:bg-slate-800" />
          <div className="h-32 rounded-3xl bg-slate-200 dark:bg-slate-800" />
          <div className="h-20 rounded-3xl bg-slate-200 dark:bg-slate-800" />
        </div>
      </div>
    );
  }

  if (!place) {
    return (
      <div className={containerClassName}>
        <div className="flex min-h-80 flex-col items-center justify-center rounded-[1.75rem] border border-dashed border-slate-200 bg-slate-50/80 px-6 text-center dark:border-slate-700 dark:bg-slate-950/40">
          <span className="material-symbols-outlined text-5xl text-slate-300">pin_drop</span>
          <p className="mt-4 text-lg font-bold text-slate-700 dark:text-slate-100">
            장소를 선택해 상세를 확인하세요
          </p>
          <p className="mt-2 text-sm leading-relaxed text-slate-500 dark:text-slate-400">
            지도 마커나 추천 목록의 카드를 누르면 이곳에 장소 설명과 태그, 거리 정보가 표시됩니다.
          </p>
        </div>
      </div>
    );
  }

  const categoryMeta = getPlaceCategoryMeta(place.category);
  const summary = place.description || place.detail || "아직 등록된 소개 문구가 없어요.";
  const detail = place.detail && place.detail !== summary ? place.detail : null;

  return (
    <div className={containerClassName}>
      <div className="flex items-start justify-between gap-4">
        <div className="min-w-0">
          <p className="text-xs font-semibold uppercase tracking-[0.24em] text-slate-400">
            Selected Place
          </p>
          <div className="mt-3 flex flex-wrap items-center gap-2">
            <span
              className="material-symbols-outlined text-[20px]"
              style={{ color: categoryMeta.color }}
            >
              {categoryMeta.icon}
            </span>
            <h2 className="text-2xl font-extrabold tracking-tight text-slate-900 dark:text-slate-100">
              {place.name}
            </h2>
          </div>
          <div className="mt-3 flex flex-wrap items-center gap-2">
            <Badge variant="secondary">{categoryMeta.label}</Badge>
            <Badge variant="secondary">{formatDistance(place.distanceMeters)}</Badge>
            <Badge variant="secondary">추천 {formatScore(place.recommendationScore)}</Badge>
          </div>
        </div>

        <div
          className="inline-flex h-12 w-12 shrink-0 items-center justify-center rounded-2xl"
          style={{ backgroundColor: `${categoryMeta.color}18` }}
        >
          <span
            className="material-symbols-outlined text-[24px]"
            style={{ color: categoryMeta.color }}
          >
            {categoryMeta.icon}
          </span>
        </div>
      </div>

      <div className="mt-5 rounded-[1.75rem] bg-slate-50/90 p-4 dark:bg-slate-900/60">
        <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">
          대표 소개
        </p>
        <p className="mt-3 text-sm leading-7 text-slate-600 dark:text-slate-300">{summary}</p>
      </div>

      {detail && (
        <div className="mt-4 rounded-[1.75rem] border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">
            상세 설명
          </p>
          <p className="mt-3 text-sm leading-7 text-slate-600 dark:text-slate-300">{detail}</p>
        </div>
      )}

      <div className="mt-4 rounded-[1.75rem] border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
        <div className="flex items-center justify-between gap-3">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">
              기준 위치
            </p>
            <p className="mt-2 text-sm font-bold text-slate-900 dark:text-slate-100">
              {baseLocationName}
            </p>
          </div>
          <div className="text-right">
            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">
              거리
            </p>
            <p className="mt-2 text-sm font-bold text-slate-900 dark:text-slate-100">
              {formatDistance(place.distanceMeters)}
            </p>
          </div>
        </div>
        <p className="mt-3 text-xs leading-6 text-slate-500 dark:text-slate-400">
          선택 상태는 지도 마커 강조와 함께 유지됩니다.
        </p>
      </div>

      {place.tags.length > 0 && (
        <div className="mt-4">
          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">태그</p>
          <div className="mt-3 flex flex-wrap gap-2">
            {place.tags.map((tag) => (
              <Badge key={tag} variant="primary" className="bg-slate-100 text-slate-700">
                #{tag}
              </Badge>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
