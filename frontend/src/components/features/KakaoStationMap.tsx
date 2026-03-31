"use client";

import { useEffect, useRef, useState } from "react";

import { getPlaceCategoryMeta } from "@/data/placeCategories";
import { PlaceData, PlaceSearchBounds } from "@/services/place";

const extractBounds = (map: KakaoMapInstance): PlaceSearchBounds => {
  const bounds = map.getBounds();
  const southWest = bounds.getSouthWest();
  const northEast = bounds.getNorthEast();

  return {
    southWestLat: southWest.getLat(),
    southWestLng: southWest.getLng(),
    northEastLat: northEast.getLat(),
    northEastLng: northEast.getLng(),
  };
};

interface KakaoStationMapProps {
  stationName: string;
  latitude: number;
  longitude: number;
  className?: string;
  mapClassName?: string;
  places?: PlaceData[];
  selectedPlaceId?: string | null;
  focusSelectedPlace?: boolean;
  recenterLabel?: string;
  onPlaceSelect?: (placeId: string) => void;
  onRecenter?: () => void;
  onBoundsChange?: (bounds: PlaceSearchBounds) => void;
  onViewportInteract?: () => void;
  searchPending?: boolean;
  searchInAreaLoading?: boolean;
  onSearchInArea?: () => void;
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
      existingScript.addEventListener(
        "error",
        () => reject(new Error("카카오 지도 스크립트를 불러오지 못했습니다.")),
        { once: true }
      );
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

const createMarkerImage = (maps: KakaoMapsNamespace, color: string, selected: boolean) => {
  const width = selected ? 42 : 34;
  const height = selected ? 54 : 44;
  const anchorX = width / 2;
  const anchorY = height - 2;
  const strokeColor = selected ? "#ffffff" : "rgba(255,255,255,0.95)";
  const shadowColor = selected ? "rgba(15, 23, 42, 0.18)" : "rgba(15, 23, 42, 0.12)";
  const svg = `
    <svg xmlns="http://www.w3.org/2000/svg" width="${width}" height="${height}" viewBox="0 0 ${width} ${height}">
      <g filter="drop-shadow(0 8px 16px ${shadowColor})">
        <path d="M${width / 2} ${height - 2}C${width - 4} ${height - 15} ${width - 2} ${height - 26} ${width - 2} ${height / 2}C${width - 2} 9.95 ${width / 2 + 9.5} 2 ${width / 2} 2C${width / 2 - 9.5} 2 2 9.95 2 ${height / 2}C2 ${height - 26} 4 ${height - 15} ${width / 2} ${height - 2}Z" fill="${color}" />
        <circle cx="${width / 2}" cy="${height / 2 - 2}" r="${selected ? 8 : 7}" fill="${strokeColor}" opacity="0.95" />
        <circle cx="${width / 2}" cy="${height / 2 - 2}" r="${selected ? 3.8 : 3.2}" fill="${color}" />
      </g>
    </svg>
  `;

  return new maps.MarkerImage(
    `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`,
    new maps.Size(width, height),
    {
      anchor: new maps.Point(anchorX, anchorY),
    }
  );
};

export default function KakaoStationMap({
  stationName,
  latitude,
  longitude,
  className = "",
  mapClassName = "h-[380px] md:h-[520px]",
  places = [],
  selectedPlaceId = null,
  focusSelectedPlace = false,
  recenterLabel = "기준 위치로 이동",
  onPlaceSelect,
  onRecenter,
  onBoundsChange,
  onViewportInteract,
  searchPending = false,
  searchInAreaLoading = false,
  onSearchInArea,
}: KakaoStationMapProps) {
  const mapRef = useRef<HTMLDivElement | null>(null);
  const mapInstanceRef = useRef<KakaoMapInstance | null>(null);
  const markersRef = useRef<KakaoMarker[]>([]);
  const [mapReady, setMapReady] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);
  const appKey = process.env.NEXT_PUBLIC_KAKAO_JS_KEY;
  const validationError = !appKey
    ? "카카오 지도 키가 설정되어 있지 않습니다."
    : !Number.isFinite(latitude) || !Number.isFinite(longitude)
      ? "지도 좌표 정보가 없습니다."
      : null;

  useEffect(() => {
    if (validationError || !appKey || !mapRef.current) {
      return;
    }

    let isCancelled = false;

    const initializeMap = async () => {
      try {
        setLoadError(null);
        await loadKakaoMapScript(appKey);

        const maps = window.kakao?.maps;
        if (!maps || isCancelled) {
          return;
        }

        maps.load(() => {
          const loadedMaps = window.kakao?.maps;
          if (isCancelled || !mapRef.current || !loadedMaps) {
            return;
          }

          const center = new loadedMaps.LatLng(latitude, longitude);

          if (mapInstanceRef.current) {
            mapInstanceRef.current.setCenter(center);
            mapInstanceRef.current.setLevel(4);
            return;
          }

          mapInstanceRef.current = new loadedMaps.Map(mapRef.current, {
            center,
            level: 4,
          });
          setMapReady(true);
        });
      } catch (error) {
        if (!isCancelled) {
          setLoadError(error instanceof Error ? error.message : "지도를 불러오지 못했습니다.");
        }
      }
    };

    initializeMap();

    return () => {
      isCancelled = true;
    };
  }, [appKey, latitude, longitude, validationError]);

  useEffect(() => {
    const maps = window.kakao?.maps;
    const map = mapInstanceRef.current;
    if (!mapReady || !map || !maps || !onViewportInteract) {
      return;
    }

    const handleViewportInteract = () => {
      onViewportInteract();
    };

    maps.event.addListener(map, "dragstart", handleViewportInteract);
    maps.event.addListener(map, "zoom_start", handleViewportInteract);

    return () => {
      maps.event.removeListener(map, "dragstart", handleViewportInteract);
      maps.event.removeListener(map, "zoom_start", handleViewportInteract);
    };
  }, [mapReady, onViewportInteract]);

  useEffect(() => {
    const maps = window.kakao?.maps;
    const map = mapInstanceRef.current;
    if (!mapReady || !map || !maps || !onBoundsChange) {
      return;
    }

    const handleIdle = () => {
      onBoundsChange(extractBounds(map));
    };

    maps.event.addListener(map, "idle", handleIdle);
    handleIdle();

    return () => {
      maps.event.removeListener(map, "idle", handleIdle);
    };
  }, [latitude, longitude, mapReady, onBoundsChange]);

  useEffect(() => {
    const maps = window.kakao?.maps;
    if (!mapReady || !mapInstanceRef.current || !maps) {
      return;
    }

    const map = mapInstanceRef.current;

    markersRef.current.forEach((marker) => marker.setMap(null));
    markersRef.current = [];

    const basePosition = new maps.LatLng(latitude, longitude);
    const baseMarker = new maps.Marker({
      position: basePosition,
      image: createMarkerImage(maps, "#ee2b8c", true),
    });
    baseMarker.setMap(map);
    markersRef.current.push(baseMarker);

    const selectedPlace = places.find((place) => place.id === selectedPlaceId) ?? null;

    places.forEach((place) => {
      const categoryMeta = getPlaceCategoryMeta(place.category);
      const position = new maps.LatLng(place.latitude, place.longitude);
      const marker = new maps.Marker({
        position,
        image: createMarkerImage(maps, categoryMeta.color, place.id === selectedPlaceId),
      });

      marker.setMap(map);
      markersRef.current.push(marker);

      if (onPlaceSelect) {
        maps.event.addListener(marker, "click", () => onPlaceSelect(place.id));
      }
    });

    if (focusSelectedPlace && selectedPlace) {
      const selectedPosition = new maps.LatLng(selectedPlace.latitude, selectedPlace.longitude);
      map.panTo(selectedPosition);
    }

    return () => {
      markersRef.current.forEach((marker) => marker.setMap(null));
      markersRef.current = [];
    };
  }, [focusSelectedPlace, latitude, longitude, mapReady, onPlaceSelect, places, selectedPlaceId]);

  const error = validationError || loadError;

  return (
    <div className={`w-full ${className}`}>
      <div className="mb-3 flex items-center justify-between gap-2">
        <h3 className="text-lg font-bold text-slate-900 dark:text-slate-100">{stationName} 주변</h3>
        <span className="text-xs text-slate-500 dark:text-slate-400">Kakao Map</span>
      </div>

      {error ? (
        <div className="flex h-72 w-full items-center justify-center rounded-2xl border border-slate-200 bg-slate-50 text-sm text-red-500 dark:border-slate-700 dark:bg-slate-900/50">
          {error}
        </div>
      ) : (
        <div className="relative">
          <div
            ref={mapRef}
            className={`${mapClassName} w-full overflow-hidden rounded-2xl border border-slate-200 dark:border-slate-700`}
          />

          {searchPending && onSearchInArea ? (
            <button
              type="button"
              onClick={onSearchInArea}
              disabled={searchInAreaLoading}
              className="absolute left-1/2 top-4 z-10 inline-flex -translate-x-1/2 items-center gap-2 rounded-full border border-primary/20 bg-white/95 px-4 py-2 text-sm font-bold text-slate-800 shadow-[0_12px_24px_rgba(15,23,42,0.18)] backdrop-blur transition-all hover:-translate-y-0.5 hover:border-primary/40 hover:text-primary disabled:cursor-not-allowed disabled:opacity-70 dark:border-slate-700 dark:bg-slate-950/90 dark:text-slate-100"
            >
              <span className="material-symbols-outlined text-[18px]">
                {searchInAreaLoading ? "progress_activity" : "travel_explore"}
              </span>
              {searchInAreaLoading ? "검색 중..." : "이 영역에서 검색"}
            </button>
          ) : null}

          <button
            type="button"
            onClick={() => {
              const maps = window.kakao?.maps;
              const map = mapInstanceRef.current;

              if (maps && map) {
                map.panTo(new maps.LatLng(latitude, longitude));
                map.setLevel(4);
                onBoundsChange?.(extractBounds(map));
              }

              onRecenter?.();
            }}
            className="absolute bottom-4 left-4 inline-flex items-center gap-2 rounded-full border border-white/70 bg-white/95 px-4 py-2 text-sm font-bold text-slate-700 shadow-[0_12px_24px_rgba(15,23,42,0.18)] backdrop-blur transition-all hover:-translate-y-0.5 hover:border-primary/30 hover:text-primary dark:border-slate-700 dark:bg-slate-950/90 dark:text-slate-100"
          >
            <span className="material-symbols-outlined text-[18px]">my_location</span>
            {recenterLabel}
          </button>
        </div>
      )}
    </div>
  );
}
