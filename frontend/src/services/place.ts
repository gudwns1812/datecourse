import api from "./api";
import { ApiResponse } from "./auth";
import { PlaceCategoryValue } from "@/data/placeCategories";

export interface PlaceData {
  id: string;
  name: string;
  category: string;
  description: string;
  detail: string;
  latitude: number;
  longitude: number;
  distanceMeters: number;
  recommendationScore: number;
  tags: string[];
}

export interface PlaceSearchParams {
  latitude: number;
  longitude: number;
  query?: string;
  category?: PlaceCategoryValue;
  radius?: number;
  size?: number;
}

export interface PlaceSearchResult {
  items: PlaceData[];
  totalCount: number;
}

type RawPlaceRecord = Record<string, unknown>;

const isRecord = (value: unknown): value is RawPlaceRecord =>
  typeof value === "object" && value !== null;

const pickString = (record: RawPlaceRecord, keys: string[]) => {
  for (const key of keys) {
    const value = record[key];
    if (typeof value === "string" && value.trim().length > 0) {
      return value;
    }
    if (typeof value === "number" && Number.isFinite(value)) {
      return String(value);
    }
  }

  return "";
};

const pickNumber = (record: RawPlaceRecord, keys: string[], fallback = 0) => {
  for (const key of keys) {
    const value = record[key];
    if (typeof value === "number" && Number.isFinite(value)) {
      return value;
    }
    if (typeof value === "string" && value.trim().length > 0) {
      const parsed = Number(value);
      if (Number.isFinite(parsed)) {
        return parsed;
      }
    }
  }

  return fallback;
};

const pickTags = (record: RawPlaceRecord) => {
  const candidates = [record.tags, record.tagNames, record.placeTags, record.placeTagsName];

  for (const candidate of candidates) {
    if (!Array.isArray(candidate)) {
      continue;
    }

    return candidate
      .map((tag) => {
        if (typeof tag === "string") {
          return tag.trim();
        }

        if (isRecord(tag)) {
          return pickString(tag, ["name", "tagName", "label"]);
        }

        return "";
      })
      .filter(Boolean);
  }

  return [] as string[];
};

const normalizePlace = (rawPlace: unknown): PlaceData => {
  const record = isRecord(rawPlace) ? rawPlace : {};

  return {
    id: pickString(record, ["id", "placeId", "place_id"]),
    name: pickString(record, ["name", "placeName", "place_name"]),
    category: pickString(record, ["category", "categoryName", "category_name"]),
    description: pickString(record, ["description", "summary"]),
    detail: pickString(record, ["detail", "address", "placeDetail"]),
    latitude: pickNumber(record, ["latitude", "lat", "y"]),
    longitude: pickNumber(record, ["longitude", "lng", "lon", "x"]),
    distanceMeters: pickNumber(record, ["distanceMeters", "distance", "distance_meters"]),
    recommendationScore: pickNumber(record, ["recommendationScore", "score", "recommendation_score"]),
    tags: pickTags(record),
  };
};

const extractPlaceItems = (payload: unknown): PlaceData[] => {
  if (Array.isArray(payload)) {
    return payload.map(normalizePlace);
  }

  if (!isRecord(payload)) {
    return [];
  }

  const candidates = [payload.items, payload.places, payload.content, payload.data];

  for (const candidate of candidates) {
    if (Array.isArray(candidate)) {
      return candidate.map(normalizePlace);
    }
  }

  return [];
};

export const placeService = {
  getPlaces: async (params: PlaceSearchParams, signal?: AbortSignal) => {
    const response = await api.get<ApiResponse<unknown>>("api/v1/places", {
      params: {
        latitude: params.latitude,
        longitude: params.longitude,
        query: params.query,
        category: params.category,
        radius: params.radius,
        size: params.size,
      },
      signal,
    });

    const items = extractPlaceItems(response.data.data);

    return {
      ...response.data,
      data: {
        items,
        totalCount: items.length,
      },
    } as ApiResponse<PlaceSearchResult>;
  },
};
