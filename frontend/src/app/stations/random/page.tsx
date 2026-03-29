"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { motion, AnimatePresence } from "framer-motion";
import Button from "@/components/common/Button";
import Badge from "@/components/common/Badge";
import StationCard from "@/components/features/StationCard";
import StationFilterPanel from "@/components/features/StationFilterPanel";
import KakaoStationMap from "@/components/features/KakaoStationMap";
import { StationData, StationFilter, stationService } from "@/services/station";
import { useAuthStore } from "@/store/useAuthStore";

type PageState = "idle" | "fetching" | "spinning" | "result";

export default function RandomStationPage() {
  const [pageState, setPageState] = useState<PageState>("idle");
  const [station, setStation] = useState<StationData | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [currentFilter, setCurrentFilter] = useState<StationFilter>({});

  const router = useRouter();
  const { isLoggedIn, authChecked } = useAuthStore();

  useEffect(() => {
    if (authChecked && !isLoggedIn) {
      router.replace("/login");
    }
  }, [authChecked, isLoggedIn, router]);

  const handleDraw = async (filter: StationFilter) => {
    setCurrentFilter(filter);
    setPageState("fetching");
    setStation(null);
    setError(null);

    try {
      const response = await stationService.getRandomStation(filter);
      if (response.result === "SUCCESS") {
        setStation(response.data);
        setPageState("spinning");
        // Spinning effect duration
        setTimeout(() => {
          setPageState("result");
        }, 2000);
      } else {
        setError(response.error || "역 정보를 불러오는 데 실패했습니다.");
        setPageState("idle");
      }
    } catch (err: unknown) {
      const status = (err as { response?: { status?: number } }).response?.status;
      if (status === 404) {
        setError("선택한 조건에 맞는 역이 없습니다.");
      } else {
        setError("서버와 통신 중 오류가 발생했습니다.");
      }
      setPageState("idle");
    }
  };

  const handleRedraw = () => {
    handleDraw(currentFilter);
  };

  const handleChangeFilter = () => {
    setPageState("idle");
    setStation(null);
    setError(null);
  };

  const handleViewOnMapPage = () => {
    if (!station) return;

    const params = new URLSearchParams({
      name: station.stationName,
      line: station.line,
      lat: String(station.latitude),
      lng: String(station.longitude),
    });

    router.push(`/stations/map?${params.toString()}`);
  };

  if (!authChecked) {
    return (
      <div className="flex-1 flex items-center justify-center px-6 py-12">
        <div className="flex flex-col items-center gap-3 text-slate-500">
          <span className="material-symbols-outlined text-4xl animate-spin text-primary">refresh</span>
          <p className="font-semibold">로그인 상태를 확인하고 있어요...</p>
        </div>
      </div>
    );
  }

  if (!isLoggedIn) return null;

  return (
    <div className="flex-1 flex flex-col items-center py-12 px-6">
      <div className="max-w-4xl w-full">
        <div className="flex flex-col items-center mb-16 w-full">

          <AnimatePresence mode="wait">
            {pageState === "idle" && (
              <motion.div
                key="filter-panel"
                initial={{ opacity: 0, y: 10 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -10 }}
                className="w-full"
              >
                <StationFilterPanel onDraw={handleDraw} isFetching={false} />
                {error && (
                  <p className="text-red-500 font-bold mt-6 text-center">{error}</p>
                )}
              </motion.div>
            )}

            {pageState === "fetching" && (
              <motion.div
                key="fetching"
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="w-full flex justify-center py-20"
              >
                <div className="flex flex-col items-center">
                  <span className="material-symbols-outlined text-[60px] text-primary animate-spin mb-4">
                    refresh
                  </span>
                  <p className="text-slate-500 font-medium">로딩 중...</p>
                </div>
              </motion.div>
            )}

            {(pageState === "spinning" || pageState === "result") && (
              <motion.div
                key="station-result"
                initial={{ opacity: 0, y: 16 }}
                animate={{ opacity: 1, y: 0 }}
                className="flex flex-col items-center text-center w-full"
              >
                <div className="w-full rounded-3xl border border-slate-200 dark:border-slate-700 bg-white/85 dark:bg-slate-900/60 shadow-sm p-8 md:p-10">
                  {pageState === "spinning" && (
                    <div className="py-10 flex flex-col items-center">
                      <span className="material-symbols-outlined text-[56px] text-primary animate-spin mb-4">
                        refresh
                      </span>
                      <p className="text-slate-500 font-semibold">역을 찾고 있어요...</p>
                    </div>
                  )}

                  {pageState === "result" && station && (
                    <motion.div
                      initial={{ opacity: 0, y: 20 }}
                      animate={{ opacity: 1, y: 0 }}
                      className="flex flex-col items-center"
                    >
                      <div className="flex flex-wrap justify-center gap-2 mb-4">
                        <Badge variant="subway" lineColor="#ee2b8c">
                          {station.line}
                        </Badge>
                      </div>
                      <h1 className="text-4xl md:text-6xl font-extrabold text-slate-900 dark:text-slate-100 mb-8 tracking-tight">
                        {station.stationName} 역
                      </h1>

                      <div className="flex flex-wrap justify-center gap-3 md:gap-4">
                        <Button onClick={handleRedraw} icon="refresh">
                          다시 뽑기
                        </Button>
                        <Button onClick={handleViewOnMapPage} icon="map">
                          지도에서 보기
                        </Button>
                        <Button variant="outline" onClick={handleChangeFilter} icon="tune">
                          필터 변경
                        </Button>
                      </div>

                      <div className="w-full max-w-5xl mt-10">
                        <KakaoStationMap
                          stationName={station.stationName}
                          latitude={station.latitude}
                          longitude={station.longitude}
                          mapClassName="h-[420px] md:h-[580px]"
                        />
                      </div>
                    </motion.div>
                  )}
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        {/* Recommendations Section */}
        {pageState === "result" && station && (
          <motion.section
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            transition={{ delay: 0.3 }}
            className="space-y-12 mb-20"
          >
            <div className="border-t border-slate-100 dark:border-slate-800 pt-12">
              <h2 className="text-2xl font-bold mb-8 text-center">{station.stationName} 주변 추천 코스</h2>
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
