export {};

declare global {
  interface Window {
    kakao?: {
      maps?: {
        load: (callback: () => void) => void;
        LatLng: new (lat: number, lng: number) => unknown;
        Map: new (
          container: HTMLElement,
          options: {
            center: unknown;
            level: number;
          }
        ) => unknown;
        Marker: new (options: { position: unknown }) => {
          setMap: (map: unknown) => void;
        };
      };
    };
  }
}
