"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import Button from "@/components/common/Button";
import Badge from "@/components/common/Badge";
import StationCard from "@/components/features/StationCard";
import { StationData, stationService } from "@/services/station";
import { useAuthStore } from "@/store/useAuthStore";

export default function RandomStationPage() {
  const [isSpinning, setIsSpinning] = useState(true);
  const [station, setStation] = useState<StationData | null>(null);
  const [error, setError] = useState<string | null>(null);
  
  const router = useRouter();
  const { isLoggedIn } = useAuthStore();

  const fetchRandomStation = async () => {
    setIsSpinning(true);
    setStation(null);
    setError(null);
    
    try {
      const response = await stationService.getRandomStation();
      if (response.result === "SUCCESS") {
        setStation(response.data);
      } else {
        setError("역 정보를 불러오는 데 실패했습니다.");
      }
    } catch (err) {
      setError("서버와 통신 중 오류가 발생했습니다.");
    } finally {
      // Keep spinning for at least 2 seconds for effect
      setTimeout(() => {
        setIsSpinning(false);
      }, 2000);
    }
  };

  useEffect(() => {
    if (!isLoggedIn) {
      router.push("/login");
      return;
    }
    fetchRandomStation();
  }, [isLoggedIn]);

  return (
    <div className="flex-1 flex flex-col items-center py-12 px-6">
      <div className="max-w-4xl w-full">
        {/* Draw Section */}
        <div className="flex flex-col items-center mb-16 text-center">
          <div className="relative group mb-12">
            <div className="absolute -inset-8 bg-gradient-to-r from-primary/30 to-pink-500/30 rounded-full blur-3xl opacity-50 group-hover:opacity-100 transition duration-1000"></div>
            
            <div className="relative flex flex-col items-center">
              <div className="w-64 h-64 md:w-80 md:h-80 rounded-full border-8 border-white dark:border-slate-800 shadow-2xl bg-white dark:bg-slate-900 flex items-center justify-center overflow-hidden relative">
                
                <motion.div 
                  className="absolute inset-0 opacity-10 flex items-center justify-center"
                  animate={isSpinning ? { rotate: 360 * 5 } : { rotate: 45 }}
                  transition={isSpinning ? { duration: 2, ease: "easeInOut" } : { duration: 0 }}
                >
                  <span className="material-symbols-outlined text-[200px] text-primary">
                    refresh
                  </span>
                </motion.div>

                <div className="text-center p-8 z-10">
                  <span className="material-symbols-outlined text-6xl text-primary mb-4 block">
                    {isSpinning ? "pending" : "location_on"}
                  </span>
                  <AnimatePresence mode="wait">
                    <motion.div
                      key={isSpinning ? "spinning" : (station?.stationName || "error")}
                      initial={{ opacity: 0, y: 10 }}
                      animate={{ opacity: 1, y: 0 }}
                      exit={{ opacity: 0, y: -10 }}
                      className="h-10 flex items-center justify-center"
                    >
                      <span className={`text-2xl font-extrabold ${station ? 'text-slate-900 dark:text-slate-100' : 'text-slate-400'}`}>
                        {isSpinning ? "???" : (station?.stationName || "오류")} 역
                      </span>
                    </motion.div>
                  </AnimatePresence>
                </div>

                <div className="absolute top-0 left-1/2 -translate-x-1/2 w-4 h-8 bg-primary rounded-b-full"></div>
              </div>
            </div>
          </div>

          {!isSpinning && station && (
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              className="flex flex-col items-center"
            >
              <div className="flex flex-wrap justify-center gap-2 mb-4">
                {station.lineNumbers.map((line) => (
                  <Badge key={line} variant="subway" lineColor="#ee2b8c">
                    {line}
                  </Badge>
                ))}
              </div>
              <h1 className="text-4xl md:text-5xl font-extrabold text-slate-900 dark:text-slate-100 mb-4 tracking-tight">
                {station.stationName}
              </h1>
              <p className="text-slate-500 dark:text-slate-400 mb-8 max-w-lg">
                <span className="material-symbols-outlined text-sm align-middle mr-1">location_on</span>
                {station.stationAddress}
              </p>
              
              <div className="flex gap-4">
                <Button onClick={fetchRandomStation} icon="refresh">
                  다시 뽑기
                </Button>
                <Button variant="outline" onClick={() => router.push("/")} icon="home">
                  홈으로
                </Button>
              </div>
            </motion.div>
          )}

          {!isSpinning && error && (
            <div className="text-center">
              <p className="text-primary font-bold mb-6">{error}</p>
              <Button onClick={fetchRandomStation} icon="refresh">재시도</Button>
            </div>
          )}
        </div>

        {/* Recommendations Section (Placeholder with inferred data) */}
        {!isSpinning && station && (
          <motion.section
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
            className="space-y-12 mb-20"
          >
            <div className="border-t border-slate-100 dark:border-slate-800 pt-12">
              <h2 className="text-2xl font-bold mb-8 text-center">주변 추천 코스</h2>
              <div className="grid gap-6">
                <StationCard
                  title="강변 루프탑 레스토랑"
                  description="한강이 한눈에 보이는 멋진 전망과 함께 즐기는 이탈리안 다이닝입니다."
                  imageUrl="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=800&auto=format&fit=crop"
                  priceLevel="$$$"
                  category="Eat"
                  tags={["전망좋은", "데이트", "양식"]}
                  icon="restaurant"
                />
                <StationCard
                  title="역전 산책로 공원"
                  description="한적하게 걷기 좋은 도심 속 녹지 공간입니다. 저녁 노을이 아름답습니다."
                  imageUrl="https://images.unsplash.com/photo-1502082553048-f009c37129b9?q=80&w=800&auto=format&fit=crop"
                  priceLevel="Free"
                  category="See"
                  tags={["산책", "야경", "사진"]}
                  icon="visibility"
                />
              </div>
            </div>
          </motion.section>
        )}
      </div>
    </div>
  );
}
