import { create } from "zustand";
import { persist } from "zustand/middleware";

interface AuthState {
  isLoggedIn: boolean;
  username: string | null;
  login: (username: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      isLoggedIn: false,
      username: null,
      login: (username: string) => set({ isLoggedIn: true, username }),
      logout: () => set({ isLoggedIn: false, username: null }),
    }),
    {
      name: "auth-storage", // name of the item in the storage (must be unique)
    }
  )
);
