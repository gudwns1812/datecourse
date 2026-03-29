"use client";

import { useEffect } from "react";
import { authService } from "@/services/auth";
import { useAuthStore } from "@/store/useAuthStore";

export function AuthSync({ children }: { children: React.ReactNode }) {
    const { setAuthenticated, setUnauthenticated } = useAuthStore();

    useEffect(() => {
        const syncAuth = async () => {
            try {
                const response = await authService.checkSession();
                if (response.result === "SUCCESS" && typeof response.data === "string" && response.data.trim()) {
                    setAuthenticated(response.data);
                    return;
                }
                setUnauthenticated();
            } catch {
                setUnauthenticated();
            }
        };

        syncAuth();

        const handleVisibility = () => {
            if (document.visibilityState === "visible") {
                syncAuth();
            }
        };
        window.addEventListener("visibilitychange", handleVisibility);
        window.addEventListener("focus", syncAuth);

        return () => {
            window.removeEventListener("visibilitychange", handleVisibility);
            window.removeEventListener("focus", syncAuth);
        };
    }, [setAuthenticated, setUnauthenticated]);

    return <>{children}</>;
}
