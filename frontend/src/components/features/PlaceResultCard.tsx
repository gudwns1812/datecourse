"use client";

import Badge from "@/components/common/Badge";
import { getPlaceCategoryMeta } from "@/data/placeCategories";
import { PlaceData } from "@/services/place";

interface PlaceResultCardProps {
  place: PlaceData;
  selected?: boolean;
  onClick: () => void;
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

export default function PlaceResultCard({ place, selected = false, onClick }: PlaceResultCardProps) {
  const categoryMeta = getPlaceCategoryMeta(place.category);
  const summary = place.description || place.detail;

  return (
    <button
      type="button"
      onClick={onClick}
      aria-pressed={selected}
      className={`group w-full rounded-[1.75rem] border p-4 text-left transition-all duration-200 ${
        selected
          ? "border-primary bg-primary/5 shadow-[0_16px_32px_rgba(238,43,140,0.14)]"
          : "border-slate-200 bg-white hover:border-primary/30 hover:bg-primary/5 dark:border-slate-700 dark:bg-slate-900/60 dark:hover:border-primary/40"
      }`}
    >
      <div className="flex items-start justify-between gap-3">
        <div className="min-w-0">
          <div className="flex flex-wrap items-center gap-2">
            <span
              className="material-symbols-outlined text-[18px]"
              style={{ color: categoryMeta.color }}
            >
              {categoryMeta.icon}
            </span>
            <h3 className="truncate text-base font-extrabold text-slate-900 dark:text-slate-100">
              {place.name}
            </h3>
          </div>
          <div className="mt-2 flex flex-wrap items-center gap-2">
            <Badge variant="secondary">{categoryMeta.label}</Badge>
            <Badge variant="secondary">{formatDistance(place.distanceMeters)}</Badge>
          </div>
        </div>
        <span
          className={`mt-1 inline-flex h-3 w-3 rounded-full ${
            selected ? "bg-primary" : "bg-slate-300 dark:bg-slate-600"
          }`}
        />
      </div>

      {summary && (
        <p className="mt-3 line-clamp-2 text-sm leading-relaxed text-slate-600 dark:text-slate-300">
          {summary}
        </p>
      )}
    </button>
  );
}
