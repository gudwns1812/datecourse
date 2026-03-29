"use client";

import { useEffect, useRef, useState } from "react";

interface KakaoStationMapProps {
  stationName: string;
  latitude: number;
  longitude: number;
  className?: string;
  mapClassName?: string;
}

let kakaoScriptPromise: Promise<void> | null = null;

const loadKakaoMapScript = (appKey: string) => {
  if (typeof window === "undefined") {
    return Promise.reject(new Error("window is not available"));
  }

  if (window.kakao?.maps) {
    return Promise.resolve();
  }

  if (kakaoScriptPromise) {
    return kakaoScriptPromise;
  }

  kakaoScriptPromise = new Promise<void>((resolve, reject) => {
    const existingScript = document.getElementById("kakao-map-sdk");
    if (existingScript) {
      existingScript.addEventListener("load", () => resolve(), { once: true });
      existingScript.addEventListener("error", () => reject(new Error("카카오 지도 스크립트를 불러오지 못했습니다.")), {
        once: true,
      });
      return;
    }

    const script = document.createElement("script");
    script.id = "kakao-map-sdk";
    script.async = true;
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${appKey}&autoload=false`;
    script.onload = () => resolve();
    script.onerror = () => reject(new Error("카카오 지도 스크립트를 불러오지 못했습니다."));
    document.head.appendChild(script);
  });

  return kakaoScriptPromise;
};

export default function KakaoStationMap({
  stationName,
  latitude,
  longitude,
  className = "",
  mapClassName = "h-[380px] md:h-[520px]",
}: KakaoStationMapProps) {
  const mapRef = useRef<HTMLDivElement | null>(null);
  const [loadError, setLoadError] = useState<string | null>(null);
  const appKey = process.env.NEXT_PUBLIC_KAKAO_JS_KEY;
  const validationError = !appKey
    ? "카카오 지도 키가 설정되지 않았습니다."
    : !Number.isFinite(latitude) || !Number.isFinite(longitude)
      ? "지도 좌표 정보가 없습니다."
      : null;

  useEffect(() => {
    if (validationError || !appKey) {
      return;
    }

    let isCancelled = false;
    const container = mapRef.current;
    if (!container) {
      return;
    }

    const initializeMap = async () => {
      try {
        setLoadError(null);
        await loadKakaoMapScript(appKey);

        if (!window.kakao?.maps || isCancelled) {
          return;
        }

        window.kakao.maps.load(() => {
          if (isCancelled || !mapRef.current || !window.kakao?.maps) {
            return;
          }

          const center = new window.kakao.maps.LatLng(latitude, longitude);
          const map = new window.kakao.maps.Map(mapRef.current, {
            center,
            level: 3,
          });
          const marker = new window.kakao.maps.Marker({ position: center });
          marker.setMap(map);
        });
      } catch (e) {
        if (!isCancelled) {
          setLoadError(e instanceof Error ? e.message : "지도를 불러오지 못했습니다.");
        }
      }
    };

    initializeMap();

    return () => {
      isCancelled = true;
    };
  }, [appKey, latitude, longitude, validationError]);

  const error = validationError || loadError;

  return (
    <div className={`w-full ${className}`}>
      <div className="mb-3 flex items-center justify-between gap-2">
        <h3 className="text-lg font-bold text-slate-900 dark:text-slate-100">
          {stationName} 위치
        </h3>
        <span className="text-xs text-slate-500 dark:text-slate-400">Kakao Map</span>
      </div>

      {error ? (
        <div className="h-72 w-full rounded-2xl border border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-900/50 flex items-center justify-center text-sm text-red-500">
          {error}
        </div>
      ) : (
        <div
          ref={mapRef}
          className={`${mapClassName} w-full rounded-2xl border border-slate-200 dark:border-slate-700 overflow-hidden`}
        />
      )}
    </div>
  );
}
