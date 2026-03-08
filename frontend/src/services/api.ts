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
    // Standard error handling
    const message = error.response?.data?.error || "An unexpected error occurred.";
    console.error("API Error:", message);
    return Promise.reject(error);
  }
);

export default api;
