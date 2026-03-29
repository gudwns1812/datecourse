import api from "./api";

export interface SignupRequest {
  username: string;
  loginId: string;
  password: string;
  email: string;
  gender: "MALE" | "FEMALE";
  phoneNumber: string;
  birth: string; // YYYY-MM-DD
}

export interface LoginRequest {
  loginId: string;
  password: string;
}

export interface ApiResponse<T> {
  result: "SUCCESS" | "FAIL";
  data: T;
  error: unknown;
}

export const authService = {
  signup: async (data: SignupRequest) => {
    const response = await api.post<ApiResponse<null>>("api/v1/auth/signup", data);
    return response.data;
  },

  login: async (data: LoginRequest) => {
    const response = await api.post<ApiResponse<string>>("api/v1/auth/login", data);
    return response.data; // Success data is username
  },

  checkId: async (loginId: string) => {
    const response = await api.get<ApiResponse<boolean>>(`api/v1/auth/check-id?loginId=${loginId}`);
    return response.data;
  },

  checkSession: async () => {
    const response = await api.get<ApiResponse<string>>("api/v1/auth/me");
    return response.data;
  },
};
