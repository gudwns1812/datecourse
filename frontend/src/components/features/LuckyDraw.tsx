"use client";

import { useRouter } from "next/navigation";
import Button from "@/components/common/Button";
import { useAuthStore } from "@/store/useAuthStore";

export default function LuckyDraw() {
  const router = useRouter();
  const { isLoggedIn, authChecked } = useAuthStore();

  const handleStartDraw = () => {
    if (!authChecked) {
      router.push("/stations/random");
      return;
    }

    if (!isLoggedIn) {
      alert("로그인이 필요한 서비스입니다. 로그인 페이지로 이동합니다.");
      router.push("/login");
      return;
    }
    
    router.push("/stations/random");
  };

  return (
    <div className="flex flex-col items-center">
      <div className="relative group mb-12">
        {/* Decorative Glow */}
        <div className="absolute -inset-8 bg-gradient-to-r from-primary/30 to-pink-500/30 rounded-full blur-3xl opacity-50 group-hover:opacity-100 transition duration-1000"></div>
        
        <div className="relative flex flex-col items-center">
          {/* Static Wheel Placeholder for Main Page */}
          <div className="w-64 h-64 md:w-80 md:h-80 rounded-full border-8 border-white dark:border-slate-800 shadow-2xl bg-white dark:bg-slate-900 flex items-center justify-center overflow-hidden relative">
            
            <div className="absolute inset-0 opacity-10 flex items-center justify-center">
              <span className="material-symbols-outlined text-[200px] text-primary rotate-45">
                refresh
              </span>
            </div>

            <div className="text-center p-8 z-10">
              <span className="material-symbols-outlined text-6xl text-primary mb-4 block">
                location_on
              </span>
              <div className="h-10 flex items-center justify-center">
                <span className="text-2xl font-bold text-slate-400">
                  ??? 역
                </span>
              </div>
            </div>

            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-4 h-8 bg-primary rounded-b-full"></div>
          </div>
        </div>
      </div>

      <Button
        size="xl"
        className="px-10"
        onClick={handleStartDraw}
        icon="casino"
      >
        랜덤으로 데이트역 뽑기
      </Button>
    </div>
  );
}
