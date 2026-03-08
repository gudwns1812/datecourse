import api from "./api";
import { ApiResponse } from "./auth";

export interface StationData {
  lineNumbers: string[];
  stationName: string;
  stationAddress: string;
}

export const stationService = {
  getRandomStation: async () => {
    const response = await api.get<ApiResponse<StationData>>("/v1/stations/random");
    return response.data;
  },
};
