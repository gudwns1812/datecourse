import { create } from "zustand";

interface AuthState {
  isLoggedIn: boolean;
  authChecked: boolean;
  username: string | null;
  login: (username: string) => void;
  logout: () => void;
  setAuthenticated: (username: string) => void;
  setUnauthenticated: () => void;
}

export const useAuthStore = create<AuthState>()((set) => ({
  isLoggedIn: false,
  authChecked: false,
  username: null,
  login: (username: string) => set({ isLoggedIn: true, authChecked: true, username }),
  logout: () => set({ isLoggedIn: false, authChecked: true, username: null }),
  setAuthenticated: (username: string) => set({ isLoggedIn: true, authChecked: true, username }),
  setUnauthenticated: () => set({ isLoggedIn: false, authChecked: true, username: null }),
}));
