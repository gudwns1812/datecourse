export type PlaceCategoryValue = "카페" | "음식점" | "문화시설" | "관광명소";

export type PlaceCategoryFilter = PlaceCategoryValue | "all";

export interface PlaceCategoryOption {
  value: PlaceCategoryValue;
  label: string;
  icon: string;
  color: string;
}

export const PLACE_CATEGORY_ALL = {
  value: "all" as const,
  label: "전체",
  icon: "apps",
  color: "#64748b",
};

export const PLACE_CATEGORIES = [
  {
    value: "카페",
    label: "카페",
    icon: "local_cafe",
    color: "#0ea5e9",
  },
  {
    value: "음식점",
    label: "음식점",
    icon: "restaurant",
    color: "#f97316",
  },
  {
    value: "문화시설",
    label: "문화시설",
    icon: "museum",
    color: "#14b8a6",
  },
  {
    value: "관광명소",
    label: "관광명소",
    icon: "travel_explore",
    color: "#f43f5e",
  },
] as const satisfies ReadonlyArray<PlaceCategoryOption>;

export const PLACE_CATEGORY_LOOKUP = PLACE_CATEGORIES.reduce(
  (acc, category) => {
    acc[category.value] = category;
    return acc;
  },
  {} as Record<PlaceCategoryValue, PlaceCategoryOption>
);

export const getPlaceCategoryMeta = (category?: string) => {
  if (!category) {
    return PLACE_CATEGORY_ALL;
  }

  return PLACE_CATEGORY_LOOKUP[category as PlaceCategoryValue] ?? PLACE_CATEGORY_ALL;
};
