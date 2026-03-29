import axios from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL || "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // For session management
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const requestUrl = String(error.config?.url ?? "");

    // 401 Unauthorized 에러 처리
    if (error.response?.status === 401) {
      console.warn("세션이 만료되었거나 인증되지 않은 사용자입니다.");
      const isSessionCheckRequest = requestUrl.includes("api/v1/auth/me");

      import("@/store/useAuthStore").then(({ useAuthStore }) => {
        useAuthStore.getState().setUnauthenticated();

        // /auth/me 초기 검증 요청은 라우팅 전환 대신 상태 동기화만 수행
        if (isSessionCheckRequest || typeof window === "undefined") {
          return;
        }

        if (!window.location.pathname.startsWith("/login")) {
          window.location.href = "/login";
        }
      });
    }

    // Standard error handling
    const message = error.response?.data?.error || "An unexpected error occurred.";
    const isExpectedRandomStationMiss =
      error.response?.status === 404 && requestUrl.includes("api/v1/stations/random");

    if (!isExpectedRandomStationMiss) {
      console.error("API Error:", message);
    }

    return Promise.reject(error);
  }
);

export default api;
