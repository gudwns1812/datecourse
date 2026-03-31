export {};

declare global {
  interface KakaoLatLng {
    readonly lat?: number;
    readonly lng?: number;
    getLat(): number;
    getLng(): number;
  }

  interface KakaoPoint {
    readonly x?: number;
    readonly y?: number;
  }

  interface KakaoSize {
    readonly width?: number;
    readonly height?: number;
  }

  interface KakaoMarkerImage {
    readonly src?: string;
  }

  interface KakaoBounds {
    extend(latLng: KakaoLatLng): void;
    getSouthWest(): KakaoLatLng;
    getNorthEast(): KakaoLatLng;
  }

  interface KakaoMarker {
    setMap(map: KakaoMapInstance | null): void;
  }

  interface KakaoMapInstance {
    setCenter(latLng: KakaoLatLng): void;
    setLevel(level: number): void;
    setBounds(bounds: KakaoBounds): void;
    panTo(latLng: KakaoLatLng): void;
    getBounds(): KakaoBounds;
  }

  interface KakaoEventNamespace {
    addListener(target: KakaoMapInstance | KakaoMarker, eventName: string, handler: () => void): void;
    removeListener(target: KakaoMapInstance | KakaoMarker, eventName: string, handler: () => void): void;
  }

  interface KakaoMapsNamespace {
    load(callback: () => void): void;
    LatLng: new (lat: number, lng: number) => KakaoLatLng;
    Map: new (
      container: HTMLElement,
      options: {
        center: KakaoLatLng;
        level: number;
      }
    ) => KakaoMapInstance;
    Marker: new (options: { position: KakaoLatLng; image?: KakaoMarkerImage }) => KakaoMarker;
    MarkerImage: new (
      src: string,
      size: KakaoSize,
      options?: { anchor?: KakaoPoint }
    ) => KakaoMarkerImage;
    Size: new (width: number, height: number) => KakaoSize;
    Point: new (x: number, y: number) => KakaoPoint;
    LatLngBounds: new () => KakaoBounds;
    event: KakaoEventNamespace;
  }

  interface Window {
    kakao?: {
      maps?: KakaoMapsNamespace;
    };
  }
}
