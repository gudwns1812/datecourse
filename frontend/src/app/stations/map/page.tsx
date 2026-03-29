"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Button from "@/components/common/Button";
import Badge from "@/components/common/Badge";
import KakaoStationMap from "@/components/features/KakaoStationMap";
import { useAuthStore } from "@/store/useAuthStore";

const SEOUL_CITY_HALL = {
  name: "서울 시청",
  latitude: 37.5662952,
  longitude: 126.9779451,
};

export default function StationMapPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { isLoggedIn, authChecked } = useAuthStore();

  const stationName = searchParams.get("name") ?? "";
  const line = searchParams.get("line") ?? "";
  const latitude = Number(searchParams.get("lat"));
  const longitude = Number(searchParams.get("lng"));
  const hasValidStation =
    Boolean(stationName) && Number.isFinite(latitude) && Number.isFinite(longitude);

  const [nearbyCenter, setNearbyCenter] = useState(SEOUL_CITY_HALL);
  const [locationStatus, setLocationStatus] = useState<"locating" | "resolved" | "fallback">(
    hasValidStation ? "resolved" : "locating"
  );

  useEffect(() => {
    if (authChecked && !isLoggedIn) {
      router.replace("/login");
    }
  }, [authChecked, isLoggedIn, router]);

  useEffect(() => {
    if (hasValidStation) {
      return;
    }

    let isCancelled = false;

    if (!("geolocation" in navigator)) {
      Promise.resolve().then(() => {
        if (!isCancelled) {
          setLocationStatus("fallback");
        }
      });
      return () => {
        isCancelled = true;
      };
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        if (isCancelled) return;
        setNearbyCenter({
          name: "내 주변",
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
        setLocationStatus("resolved");
      },
      () => {
        if (isCancelled) return;
        setLocationStatus("fallback");
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 60000,
      }
    );

    return () => {
      isCancelled = true;
    };
  }, [hasValidStation]);

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

  const mapName = hasValidStation ? stationName : nearbyCenter.name;
  const mapLatitude = hasValidStation ? latitude : nearbyCenter.latitude;
  const mapLongitude = hasValidStation ? longitude : nearbyCenter.longitude;

  return (
    <div className="flex-1 flex flex-col items-center py-12 px-6">
      <div className="max-w-6xl w-full">
        <section className="rounded-3xl border border-slate-200 dark:border-slate-700 bg-white/85 dark:bg-slate-900/60 p-8 md:p-10 shadow-sm">
          <div className="flex flex-wrap items-center justify-between gap-4 mb-8">
            <div>
              <div className="flex items-center gap-2 mb-3">
                {hasValidStation ? (
                  line && (
                    <Badge variant="subway" lineColor="#ee2b8c">
                      {line}
                    </Badge>
                  )
                ) : (
                  <Badge variant={locationStatus === "resolved" ? "primary" : "secondary"}>
                    {locationStatus === "resolved" ? "내 위치 기준" : "서울 기본 위치"}
                  </Badge>
                )}
              </div>
              <h1 className="text-3xl md:text-5xl font-extrabold tracking-tight text-slate-900 dark:text-slate-100">
                {hasValidStation ? `${stationName} 역 지도` : "내 주변 지도"}
              </h1>
              {!hasValidStation && (
                <p className="mt-3 text-sm text-slate-500 dark:text-slate-400">
                  {locationStatus === "locating"
                    ? "현재 위치를 확인하는 중이에요. 잠시만 기다려주세요."
                    : locationStatus === "resolved"
                      ? "현재 위치를 중심으로 지도를 보여드리고 있어요."
                      : "위치 권한을 확인할 수 없어 서울 시청 기준 지도를 보여드리고 있어요."}
                </p>
              )}
            </div>
            <Button variant="outline" onClick={() => router.push("/stations/random")} icon="casino">
              랜덤역 다시 뽑기
            </Button>
          </div>

          <KakaoStationMap
            stationName={mapName}
            latitude={mapLatitude}
            longitude={mapLongitude}
            mapClassName="h-[520px] md:h-[680px]"
          />
        </section>
      </div>
    </div>
  );
}
