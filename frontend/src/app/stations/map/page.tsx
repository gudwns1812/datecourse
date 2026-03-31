'use client';

import { Suspense, useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import { AnimatePresence, motion } from 'framer-motion';

import Badge from '@/components/common/Badge';
import Button from '@/components/common/Button';
import Input from '@/components/common/Input';
import KakaoStationMap from '@/components/features/KakaoStationMap';
import PlaceDetailBottomSheet from '@/components/features/PlaceDetailBottomSheet';
import PlaceDetailPanel from '@/components/features/PlaceDetailPanel';
import PlaceResultCard from '@/components/features/PlaceResultCard';
import { PLACE_CATEGORY_ALL, PLACE_CATEGORIES, PlaceCategoryFilter } from '@/data/placeCategories';
import { PlaceData, PlaceSearchBounds, placeService } from '@/services/place';
import { useAuthStore } from '@/store/useAuthStore';

type GeoLocationState = 'idle' | 'locating' | 'resolved' | 'fallback';
type BaseMode = 'station' | 'current';
type LocationSource = 'station' | 'browser' | 'fallback';

interface LocationPoint {
  name: string;
  latitude: number;
  longitude: number;
}

export default function StationMapPage() {
  return (
    <Suspense fallback={<StationMapPageFallback />}>
      <StationMapPageContent />
    </Suspense>
  );
}

const SEOUL_CITY_HALL: LocationPoint = {
  name: '서울시청',
  latitude: 37.5662952,
  longitude: 126.9779451,
};

const SEARCH_RESULT_SIZE = 200;
const SEARCH_DEBOUNCE_MS = 350;
const LOCATION_DEBUG_PREFIX = '[StationMapLocation]';
const BOUNDS_EPSILON = 0.00001;

const formatLocationLabel = (point: LocationPoint) => {
  if (point.name === SEOUL_CITY_HALL.name) {
    return '서울시청';
  }

  return point.name;
};

const areBoundsEqual = (left: PlaceSearchBounds | null, right: PlaceSearchBounds | null) => {
  if (left === right) {
    return true;
  }

  if (!left || !right) {
    return false;
  }

  return (
    Math.abs(left.southWestLat - right.southWestLat) <= BOUNDS_EPSILON &&
    Math.abs(left.southWestLng - right.southWestLng) <= BOUNDS_EPSILON &&
    Math.abs(left.northEastLat - right.northEastLat) <= BOUNDS_EPSILON &&
    Math.abs(left.northEastLng - right.northEastLng) <= BOUNDS_EPSILON
  );
};

const StationMapPageFallback = () => (
  <div className="flex flex-1 items-center justify-center px-6 py-12">
    <div className="flex flex-col items-center gap-3 text-slate-500">
      <span className="material-symbols-outlined animate-spin text-4xl text-primary">refresh</span>
      <p className="font-semibold">Loading map...</p>
    </div>
  </div>
);

function StationMapPageContent() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const { isLoggedIn, authChecked } = useAuthStore();

  const stationName = searchParams.get('name') ?? '';
  const line = searchParams.get('line') ?? '';
  const latitudeParam = searchParams.get('lat');
  const longitudeParam = searchParams.get('lng');
  const latitude = latitudeParam === null ? Number.NaN : Number(latitudeParam);
  const longitude = longitudeParam === null ? Number.NaN : Number(longitudeParam);
  const hasValidStation = Boolean(stationName) && Number.isFinite(latitude) && Number.isFinite(longitude);

  const stationLocation: LocationPoint | null = hasValidStation
    ? {
        name: stationName,
        latitude,
        longitude,
      }
    : null;

  const [baseMode, setBaseMode] = useState<BaseMode>(hasValidStation ? 'station' : 'current');
  const [currentLocation, setCurrentLocation] = useState<LocationPoint | null>(null);
  const [locationStatus, setLocationStatus] = useState<GeoLocationState>(hasValidStation ? 'resolved' : 'idle');
  const [locationRequestKey, setLocationRequestKey] = useState(0);
  const [searchValue, setSearchValue] = useState('');
  const [debouncedSearchValue, setDebouncedSearchValue] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<PlaceCategoryFilter>(PLACE_CATEGORY_ALL.value);
  const [places, setPlaces] = useState<PlaceData[]>([]);
  const [selectedPlaceId, setSelectedPlaceId] = useState<string | null>(null);
  const [focusSelectedPlace, setFocusSelectedPlace] = useState(false);
  const [isDetailSheetOpen, setIsDetailSheetOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [loadError, setLoadError] = useState<string | null>(null);
  const [viewportBounds, setViewportBounds] = useState<PlaceSearchBounds | null>(null);
  const [searchBounds, setSearchBounds] = useState<PlaceSearchBounds | null>(null);
  const [searchRequestKey, setSearchRequestKey] = useState(0);

  useEffect(() => {
    if (authChecked && !isLoggedIn) {
      router.replace('/login');
    }
  }, [authChecked, isLoggedIn, router]);

  useEffect(() => {
    const timer = window.setTimeout(() => {
      setDebouncedSearchValue(searchValue.trim());
    }, SEARCH_DEBOUNCE_MS);

    return () => {
      window.clearTimeout(timer);
    };
  }, [searchValue]);

  useEffect(() => {
    console.info(`${LOCATION_DEBUG_PREFIX} environment`, {
      href: window.location.href,
      origin: window.location.origin,
      isSecureContext: window.isSecureContext,
      hasNavigatorGeolocation: 'geolocation' in navigator,
    });
  }, []);

  useEffect(() => {
    if (baseMode !== 'current') {
      return;
    }

    let isCancelled = false;
    setLocationStatus('locating');
    console.info(`${LOCATION_DEBUG_PREFIX} request:start`, {
      requestKey: locationRequestKey,
      isSecureContext: window.isSecureContext,
    });

    if (!('geolocation' in navigator)) {
      if (!isCancelled) {
        setCurrentLocation(null);
        setLocationStatus('fallback');
        console.warn(`${LOCATION_DEBUG_PREFIX} request:unsupported`);
      }

      return () => {
        isCancelled = true;
      };
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        if (isCancelled) return;
        console.info(`${LOCATION_DEBUG_PREFIX} request:success`, {
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
          accuracy: position.coords.accuracy,
          altitude: position.coords.altitude,
          altitudeAccuracy: position.coords.altitudeAccuracy,
          heading: position.coords.heading,
          speed: position.coords.speed,
          timestamp: new Date(position.timestamp).toISOString(),
        });
        setCurrentLocation({
          name: '현재 위치',
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
        });
        setLocationStatus('resolved');
      },
      (error) => {
        if (isCancelled) return;

        setCurrentLocation(null);
        setLocationStatus('fallback');
        console.warn(`${LOCATION_DEBUG_PREFIX} request:error`, {
          code: error.code,
          message: error.message,
        });
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0,
      },
    );

    return () => {
      isCancelled = true;
    };
  }, [baseMode, locationRequestKey]);

  const fallbackLocation = baseMode === 'current' && locationStatus === 'fallback' ? SEOUL_CITY_HALL : null;
  const activeLocation = baseMode === 'station' ? stationLocation : (currentLocation ?? fallbackLocation);
  const activeLocationSource: LocationSource | null =
    baseMode === 'station'
      ? stationLocation
        ? 'station'
        : null
      : currentLocation
        ? 'browser'
        : fallbackLocation
          ? 'fallback'
          : null;
  const activeLatitude = activeLocation?.latitude ?? null;
  const activeLongitude = activeLocation?.longitude ?? null;
  const activeLocationName = activeLocation ? formatLocationLabel(activeLocation) : '';

  useEffect(() => {
    console.info(`${LOCATION_DEBUG_PREFIX} active-location`, {
      baseMode,
      locationStatus,
      activeLocationSource,
      latitude: activeLatitude,
      longitude: activeLongitude,
      name: activeLocationName,
    });
  }, [activeLatitude, activeLongitude, activeLocationName, activeLocationSource, baseMode, locationStatus]);

  useEffect(() => {
    setViewportBounds(null);
    setSearchBounds(null);
    setPlaces([]);
    setSelectedPlaceId(null);
    setFocusSelectedPlace(false);
    setIsDetailSheetOpen(false);
    setSearchRequestKey(0);
  }, [activeLatitude, activeLongitude]);

  useEffect(() => {
    if (!authChecked || !isLoggedIn || activeLatitude === null || activeLongitude === null || searchBounds === null) {
      return;
    }

    const controller = new AbortController();
    let isCancelled = false;

    const loadPlaces = async () => {
      setLoading(true);
      setLoadError(null);

      try {
        const response = await placeService.getPlaces(
          {
            latitude: activeLatitude,
            longitude: activeLongitude,
            query: debouncedSearchValue || undefined,
            category: selectedCategory === PLACE_CATEGORY_ALL.value ? undefined : selectedCategory,
            size: SEARCH_RESULT_SIZE,
            southWestLat: searchBounds.southWestLat,
            southWestLng: searchBounds.southWestLng,
            northEastLat: searchBounds.northEastLat,
            northEastLng: searchBounds.northEastLng,
          },
          controller.signal,
        );

        if (isCancelled) {
          return;
        }

        if (response.result === 'SUCCESS') {
          const nextPlaces = response.data.items;
          setPlaces(nextPlaces);
          setFocusSelectedPlace(false);
          setSelectedPlaceId(null);
        } else {
          setPlaces([]);
          setFocusSelectedPlace(false);
          setSelectedPlaceId(null);
          setIsDetailSheetOpen(false);
          setLoadError(
            typeof response.error === 'string' && response.error.length > 0
              ? response.error
              : '장소 정보를 불러오지 못했어요.',
          );
        }
      } catch (error) {
        if (isCancelled || controller.signal.aborted) {
          return;
        }

        setPlaces([]);
        setFocusSelectedPlace(false);
        setSelectedPlaceId(null);
        setIsDetailSheetOpen(false);
        setLoadError(error instanceof Error ? error.message : '장소 정보를 불러오지 못했어요.');
      } finally {
        if (!isCancelled && !controller.signal.aborted) {
          setLoading(false);
        }
      }
    };

    loadPlaces();

    return () => {
      isCancelled = true;
      controller.abort();
    };
  }, [activeLatitude, activeLongitude, authChecked, debouncedSearchValue, isLoggedIn, searchBounds, searchRequestKey, selectedCategory]);

  useEffect(() => {
    if (!selectedPlaceId) {
      setIsDetailSheetOpen(false);
    }
  }, [selectedPlaceId]);

  const mapTitle = activeLocationName || '현재 위치';
  const selectedPlace = places.find((place) => place.id === selectedPlaceId) ?? null;

  const handlePlaceSelect = (placeId: string) => {
    setSelectedPlaceId(placeId);
    setFocusSelectedPlace(true);
    setIsDetailSheetOpen(true);
  };

  const handleUseCurrentLocation = () => {
    setBaseMode('current');
    setCurrentLocation(null);
    setLocationRequestKey((previous) => previous + 1);
  };

  const handleBoundsChange = (nextBounds: PlaceSearchBounds) => {
    setViewportBounds((previous) => (areBoundsEqual(previous, nextBounds) ? previous : nextBounds));
    if (searchBounds === null) {
      setSearchBounds(nextBounds);
      setSearchRequestKey((current) => current + 1);
    }
  };

  const handleSearchInArea = () => {
    if (!viewportBounds) {
      return;
    }

    setSearchBounds({ ...viewportBounds });
    setSearchRequestKey((current) => current + 1);
    setSelectedPlaceId(null);
    setFocusSelectedPlace(false);
    setIsDetailSheetOpen(false);
  };

  const handleViewportInteract = () => {
    setFocusSelectedPlace(false);
  };

  if (!authChecked) {
    return (
      <div className="flex flex-1 items-center justify-center px-6 py-12">
        <div className="flex flex-col items-center gap-3 text-slate-500">
          <span className="material-symbols-outlined animate-spin text-4xl text-primary">refresh</span>
          <p className="font-semibold">로그인 상태를 확인하고 있어요.</p>
        </div>
      </div>
    );
  }

  if (!isLoggedIn) return null;

  const isStationMode = baseMode === 'station' && Boolean(stationLocation);
  const statusBadgeLabel =
    baseMode === 'station'
      ? line || '역 기준'
      : locationStatus === 'locating'
        ? '현재 위치 확인 중'
        : activeLocationSource === 'fallback'
          ? '서울시청 기준'
          : '현재 위치';
  const isRailVisible = Boolean(selectedPlace && isDetailSheetOpen);
  const hasPendingBounds =
    !loading && viewportBounds !== null && searchBounds !== null && !areBoundsEqual(viewportBounds, searchBounds);

  return (
    <>
      <div className="flex-1 px-4 py-8 md:px-6 md:py-10">
        <div className={`mx-auto w-full ${isRailVisible ? 'max-w-[1480px]' : 'max-w-7xl'}`}>
          <div className={`grid items-start gap-6 ${isRailVisible ? 'lg:grid-cols-[minmax(0,1fr)_320px]' : ''}`}>
            <section className="relative overflow-hidden rounded-[2rem] border border-slate-200 bg-white/90 shadow-[0_24px_80px_rgba(15,23,42,0.12)] backdrop-blur dark:border-slate-700 dark:bg-slate-950/70">
              <div className="absolute -right-24 -top-24 h-72 w-72 rounded-full bg-primary/10 blur-3xl" />
              <div className="absolute -left-24 bottom-0 h-64 w-64 rounded-full bg-sky-400/10 blur-3xl" />

              <div className="relative border-b border-slate-200/80 px-6 py-6 md:px-8">
                <div className="flex flex-wrap items-start justify-between gap-4">
                  <div className="max-w-2xl">
                    <div className="flex flex-wrap items-center gap-2">
                      <Badge variant={locationStatus === 'resolved' ? 'primary' : 'secondary'}>
                        {statusBadgeLabel}
                      </Badge>
                      {isStationMode && line && (
                        <Badge variant="subway" lineColor="#ee2b8c">
                          {line}
                        </Badge>
                      )}
                    </div>
                    <h1 className="mt-4 text-3xl font-extrabold tracking-tight text-slate-900 md:text-5xl dark:text-slate-100">
                      {isStationMode && stationLocation ? `${stationName} 주변 장소` : '현재 위치 주변 장소'}
                    </h1>
                    <p className="mt-3 max-w-2xl text-sm leading-relaxed text-slate-500 dark:text-slate-400">
                      이름, 설명, 태그로 장소를 검색하고 카테고리별 추천 장소를 지도와 함께 확인해보세요.
                    </p>
                  </div>

                  <div className="flex flex-wrap items-center gap-3">
                    <Button variant="outline" onClick={() => router.push('/stations/random')} icon="casino">
                      역 다시 찾기
                    </Button>
                  </div>
                </div>

                <div className="mt-6 grid gap-3 md:grid-cols-[repeat(2,minmax(0,1fr))]">
                  <button
                    type="button"
                    onClick={() => setBaseMode('station')}
                    disabled={!stationLocation}
                    aria-pressed={baseMode === 'station'}
                    className={`rounded-2xl border px-4 py-4 text-left transition-all ${
                      baseMode === 'station'
                        ? 'border-primary bg-primary/5 shadow-[0_10px_30px_rgba(238,43,140,0.12)]'
                        : 'border-slate-200 bg-white/80 hover:border-primary/30 dark:border-slate-700 dark:bg-slate-900/50'
                    } ${!stationLocation ? 'cursor-not-allowed opacity-50' : ''}`}>
                    <div className="flex items-center gap-2">
                      <span className="material-symbols-outlined text-[20px] text-primary">train</span>
                      <span className="font-bold text-slate-900 dark:text-slate-100">역 기준</span>
                    </div>
                    <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">
                      선택한 역을 중심으로 주변 장소를 추천해드려요.
                    </p>
                  </button>

                  <button
                    type="button"
                    onClick={handleUseCurrentLocation}
                    aria-pressed={baseMode === 'current'}
                    className={`rounded-2xl border px-4 py-4 text-left transition-all ${
                      baseMode === 'current'
                        ? 'border-primary bg-primary/5 shadow-[0_10px_30px_rgba(238,43,140,0.12)]'
                        : 'border-slate-200 bg-white/80 hover:border-primary/30 dark:border-slate-700 dark:bg-slate-900/50'
                    }`}>
                    <div className="flex items-center gap-2">
                      <span className="material-symbols-outlined text-[20px] text-primary">my_location</span>
                      <span className="font-bold text-slate-900 dark:text-slate-100">현재 위치</span>
                    </div>
                    <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">
                      GPS가 실패하면 서울시청을 기준으로 추천을 이어갑니다.
                    </p>
                  </button>
                </div>

                <div className="mt-6 grid gap-4 lg:grid-cols-[minmax(0,1fr)_320px]">
                  <div className="space-y-4">
                    <Input
                      fullWidth
                      icon="search"
                      value={searchValue}
                      onChange={(event) => setSearchValue(event.target.value)}
                      placeholder="장소명, 설명, 태그로 검색"
                      className="bg-white/90 dark:bg-slate-900/60"
                    />

                    <div className="flex flex-wrap gap-2">
                      <button
                        type="button"
                        onClick={() => setSelectedCategory(PLACE_CATEGORY_ALL.value)}
                        className={`rounded-full border px-4 py-2 text-sm font-semibold transition-all ${
                          selectedCategory === PLACE_CATEGORY_ALL.value
                            ? 'border-primary bg-primary text-white shadow-[0_10px_24px_rgba(238,43,140,0.2)]'
                            : 'border-slate-200 bg-white text-slate-600 hover:border-primary/30 hover:text-primary dark:border-slate-700 dark:bg-slate-900/60 dark:text-slate-300'
                        }`}>
                        전체
                      </button>

                      {PLACE_CATEGORIES.map((category) => {
                        const isActive = selectedCategory === category.value;

                        return (
                          <button
                            key={category.value}
                            type="button"
                            onClick={() => setSelectedCategory(category.value)}
                            className={`inline-flex items-center gap-2 rounded-full border px-4 py-2 text-sm font-semibold transition-all ${
                              isActive
                                ? 'border-transparent text-white shadow-[0_10px_24px_rgba(15,23,42,0.18)]'
                                : 'border-slate-200 bg-white text-slate-600 hover:border-primary/30 hover:text-slate-900 dark:border-slate-700 dark:bg-slate-900/60 dark:text-slate-300'
                            }`}
                            style={isActive ? { backgroundColor: category.color } : undefined}
                            aria-pressed={isActive}>
                            <span className="material-symbols-outlined text-[18px]">{category.icon}</span>
                            {category.label}
                          </button>
                        );
                      })}
                    </div>
                  </div>

                  <div className="rounded-3xl border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
                    <div className="flex flex-wrap gap-2">
                      <Badge variant="secondary">현재 지도 영역</Badge>
                      <Badge variant="secondary">최대 {SEARCH_RESULT_SIZE}개</Badge>
                      {hasPendingBounds ? <Badge variant="secondary">새 검색 대기</Badge> : null}
                    </div>
                    <p className="mt-3 text-sm leading-relaxed text-slate-500 dark:text-slate-400">
                      {loadError
                        ? loadError
                        : baseMode === 'current' && locationStatus === 'locating'
                          ? '현재 위치를 확인하는 중이에요. 잠시만 기다려주세요.'
                          : '비어 있는 검색어에서는 주변 추천 장소를 바로 보여줍니다.'}
                    </p>
                  </div>
                </div>
              </div>

              <div className="relative px-6 py-6 lg:px-8 lg:py-8">
                <motion.div
                  initial={{ opacity: 0, y: 12 }}
                  animate={{ opacity: 1, y: 0 }}
                  className={`min-w-0 ${isRailVisible ? '' : 'mx-auto w-full max-w-[1180px]'}`}>
                  <div className="grid items-start gap-6 xl:grid-cols-[minmax(0,1.45fr)_340px]">
                    <div className="space-y-5">
                      {activeLocation ? (
                        <KakaoStationMap
                          stationName={mapTitle}
                          latitude={activeLocation.latitude}
                          longitude={activeLocation.longitude}
                          places={places}
                          selectedPlaceId={selectedPlaceId}
                          focusSelectedPlace={focusSelectedPlace}
                          recenterLabel={baseMode === 'current' ? '현재 위치로 이동' : '기준 위치로 이동'}
                          onPlaceSelect={handlePlaceSelect}
                          onBoundsChange={handleBoundsChange}
                          onViewportInteract={handleViewportInteract}
                          searchPending={hasPendingBounds}
                          searchInAreaLoading={loading}
                          onSearchInArea={handleSearchInArea}
                          onRecenter={() => setFocusSelectedPlace(false)}
                          mapClassName="h-[420px] md:h-[640px]"
                        />
                      ) : (
                        <div className="flex h-[420px] items-center justify-center rounded-3xl border border-slate-200 bg-slate-50 dark:border-slate-700 dark:bg-slate-900/50 md:h-[640px]">
                          <div className="flex flex-col items-center gap-3 text-center text-slate-500">
                            <span className="material-symbols-outlined animate-pulse text-5xl text-primary">place</span>
                            <p className="font-semibold">
                              {baseMode === 'current' ? '현재 위치를 확인하고 있어요.' : '역 위치를 불러오는 중이에요.'}
                            </p>
                            <p className="text-sm text-slate-400">위치가 준비되면 바로 주변 장소를 보여드릴게요.</p>
                          </div>
                        </div>
                      )}

                      <div className="grid gap-3 md:grid-cols-3">
                        <div className="rounded-2xl border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
                          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">기준 위치</p>
                          <p className="mt-2 text-sm font-bold text-slate-900 dark:text-slate-100">
                            {activeLocation ? activeLocationName : '확인 중'}
                          </p>
                        </div>
                        <div className="rounded-2xl border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
                          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">결과</p>
                          <p className="mt-2 text-sm font-bold text-slate-900 dark:text-slate-100">
                            {loading ? '불러오는 중' : `${places.length}곳`}
                          </p>
                        </div>
                        <div className="rounded-2xl border border-slate-200 bg-white/80 p-4 dark:border-slate-700 dark:bg-slate-900/50">
                          <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">정렬</p>
                          <p className="mt-2 text-sm font-bold text-slate-900 dark:text-slate-100">추천순</p>
                        </div>
                      </div>

                      {selectedPlace && !isDetailSheetOpen && (
                        <button
                          type="button"
                          onClick={() => setIsDetailSheetOpen(true)}
                          className="flex w-full items-center justify-between rounded-3xl border border-slate-200 bg-white px-5 py-4 text-left shadow-sm transition-all hover:border-primary/40 hover:bg-primary/5 dark:border-slate-700 dark:bg-slate-900/60 lg:hidden">
                          <div>
                            <p className="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">
                              선택한 장소
                            </p>
                            <p className="mt-1 text-base font-bold text-slate-900 dark:text-slate-100">
                              {selectedPlace.name}
                            </p>
                          </div>
                          <span className="material-symbols-outlined text-primary">keyboard_arrow_up</span>
                        </button>
                      )}
                    </div>

                    <section className="rounded-[1.75rem] border border-slate-200 bg-white/80 p-5 dark:border-slate-700 dark:bg-slate-900/40 xl:flex xl:h-[calc(640px+7rem)] xl:min-h-0 xl:flex-col xl:overflow-hidden">
                      <div className="flex flex-wrap items-end justify-between gap-3">
                        <div>
                          <p className="text-xs font-semibold uppercase tracking-[0.22em] text-slate-400">
                            Recommended List
                          </p>
                          <h2 className="mt-2 text-2xl font-extrabold tracking-tight text-slate-900 dark:text-slate-100">
                            추천 장소 목록
                          </h2>
                          <p className="mt-2 text-sm text-slate-500 dark:text-slate-400">
                            카드를 선택하면 지도 포커스와 상세 패널이 함께 갱신됩니다.
                          </p>
                        </div>
                        <Badge variant="secondary">{places.length}곳</Badge>
                      </div>

                      <div className="mt-5 space-y-3 xl:min-h-0 xl:flex-1 xl:overflow-y-auto xl:pr-1">
                        {loading && places.length === 0 ? (
                          <div className="flex min-h-56 items-center justify-center rounded-2xl border border-dashed border-slate-200 bg-slate-50 dark:border-slate-700 dark:bg-slate-950/30">
                            <div className="flex flex-col items-center gap-3 text-slate-500">
                              <span className="material-symbols-outlined animate-spin text-4xl text-primary">
                                refresh
                              </span>
                              <p className="font-semibold">장소를 불러오는 중이에요.</p>
                            </div>
                          </div>
                        ) : places.length > 0 ? (
                          <AnimatePresence initial={false}>
                            {places.map((place) => (
                              <motion.div
                                key={place.id}
                                initial={{ opacity: 0, y: 8 }}
                                animate={{ opacity: 1, y: 0 }}
                                exit={{ opacity: 0, y: -8 }}>
                                <PlaceResultCard
                                  place={place}
                                  selected={place.id === selectedPlaceId}
                                  onClick={() => handlePlaceSelect(place.id)}
                                />
                              </motion.div>
                            ))}
                          </AnimatePresence>
                        ) : (
                          <div className="flex min-h-56 flex-col items-center justify-center rounded-2xl border border-dashed border-slate-200 bg-slate-50 px-6 py-8 text-center dark:border-slate-700 dark:bg-slate-950/30">
                            <span className="material-symbols-outlined text-4xl text-slate-300">location_off</span>
                            <p className="mt-3 font-semibold text-slate-700 dark:text-slate-200">
                              조건에 맞는 장소가 없어요
                            </p>
                            <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">
                              검색어를 줄이거나 카테고리를 바꿔보세요.
                            </p>
                          </div>
                        )}
                      </div>
                    </section>
                  </div>
                </motion.div>
              </div>
            </section>

            <AnimatePresence>
              {isRailVisible ? (
                <motion.aside
                  initial={{ opacity: 0, x: 16 }}
                  animate={{ opacity: 1, x: 0 }}
                  exit={{ opacity: 0, x: 16 }}
                  className="hidden lg:block">
                  <div className="sticky top-6 h-[calc(100vh-3rem)]">
                    <div className="flex h-full flex-col rounded-[2rem] border border-slate-200 bg-white/85 p-4 shadow-[0_24px_70px_rgba(15,23,42,0.12)] backdrop-blur dark:border-slate-700 dark:bg-slate-950/75">
                      <div className="border-b border-slate-200/80 px-2 pb-4 dark:border-slate-700/80">
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <p className="text-xs font-semibold uppercase tracking-[0.24em] text-slate-400">
                              Detail Rail
                            </p>
                            <h2 className="mt-2 text-xl font-extrabold tracking-tight text-slate-900 dark:text-slate-100">
                              장소 상세
                            </h2>
                            <p className="mt-2 text-sm leading-relaxed text-slate-500 dark:text-slate-400">
                              선택한 장소의 설명과 거리, 태그를 이 패널에서 길게 확인할 수 있어요.
                            </p>
                          </div>
                          <button
                            type="button"
                            onClick={() => setIsDetailSheetOpen(false)}
                            aria-label="상세 레일 닫기"
                            className="inline-flex h-10 w-10 items-center justify-center rounded-2xl border border-slate-200 bg-white text-slate-500 transition-all hover:border-primary/30 hover:text-primary dark:border-slate-700 dark:bg-slate-900 dark:text-slate-300">
                            <span className="material-symbols-outlined text-[20px]">close</span>
                          </button>
                        </div>
                      </div>

                      <div className="mt-4 min-h-0 flex-1 overflow-y-auto pr-1">
                        <AnimatePresence mode="wait" initial={false}>
                          <motion.div
                            key={selectedPlace?.id ?? 'empty-place'}
                            initial={{ opacity: 0, y: 10 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -10 }}>
                            <PlaceDetailPanel
                              place={selectedPlace}
                              baseLocationName={mapTitle}
                              isLoading={loading}
                              layout="rail"
                            />
                          </motion.div>
                        </AnimatePresence>
                      </div>
                    </div>
                  </div>
                </motion.aside>
              ) : null}
            </AnimatePresence>
          </div>
        </div>
      </div>

      <PlaceDetailBottomSheet
        place={selectedPlace}
        baseLocationName={mapTitle}
        isLoading={loading}
        open={isDetailSheetOpen}
        onClose={() => setIsDetailSheetOpen(false)}
      />
    </>
  );
}
