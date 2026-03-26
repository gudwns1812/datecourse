import api from "./api";
import { ApiResponse } from "./auth";

export interface StationData {
  line: string;
  stationName: string;
  longitude: number;
  latitude: number;
}

export interface StationFilter {
  city?: string;
  district?: string;
}

export interface FilterOptions {
  cities: string[];
  districts: Record<string, string[]>;
  lines: string[];
}

export const stationService = {
  getRandomStation: async (filter?: StationFilter) => {
    // URLSearchParams를 이용하여 파라미터 추가
    const params = new URLSearchParams();
    if (filter?.city) params.append("city", filter.city);
    if (filter?.district) params.append("district", filter.district);

    const queryString = params.toString();
    const url = `api/v1/stations/random${queryString ? `?${queryString}` : ""}`;

    const response = await api.get<ApiResponse<StationData>>(url);
    return response.data;
  }
};
