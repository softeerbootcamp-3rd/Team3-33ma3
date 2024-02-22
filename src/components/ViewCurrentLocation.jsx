import { useRef, useEffect } from "react";
import {
  getCurrentLocation,
  searchCoordinateToAddress,
} from "../utils/locationUtils";
import {
  DEFAULT_LATITUDE,
  DEFAULT_LONGITUDE,
  DEFAULT_MAX_ZOOM,
  DEFAULT_MIN_ZOOM,
  DEFAULT_ZOOM_SCALE,
} from "../constants/mapConstants";

// 현재 위치 좌표 기반 map, marker, circle 객체 생성 및 주소 상태 업데이트 함수
function initMap(latitude, longitude, mapElement, setNewAddress) {
  const center = new naver.maps.LatLng(latitude, longitude);
  searchCoordinateToAddress(center)
    .then((res) => {
      setNewAddress(res);
    })
    .catch((error) => console.log(error));

  const mapOptions = {
    center: center,
    zoom: DEFAULT_ZOOM_SCALE,
    maxZoom: DEFAULT_MAX_ZOOM,
    minZoom: DEFAULT_MIN_ZOOM,
    scaleControl: false,
  };
  const map = new naver.maps.Map(mapElement, mapOptions);

  const markerOptions = {
    position: map.getCenter(),
    map: map,
  };
  const marker = new naver.maps.Marker(markerOptions);

  const circleOptions = {
    map: map,
    center: center,
  };
  const circle = new naver.maps.Circle(circleOptions);

  return { map, marker, circle };
}

// Modal의 지도 컴포넌트
export default function ViewCurrentLocation({
  setMap,
  setMarker,
  setAddress,
  setCircle,
}) {
  const mapElement = useRef();

  useEffect(() => {
    // 현재 좌표 값 기반 비동기 처리 함수
    async function fetchAndSetLocation() {
      try {
        // const currentLocation = await getCurrentLocation();
        const { map, marker, circle } = initMap(
          DEFAULT_LATITUDE,
          DEFAULT_LONGITUDE,
          mapElement.current,
          setAddress
        );
        setMap(map);
        setMarker(marker);
        setCircle(circle);
      } catch (error) {
        console.error("Failed to fetch current location:", error);
      }
    }
    fetchAndSetLocation();
  }, []);

  return (
    <div>
      <div
        ref={mapElement}
        id="map"
        style={{ width: "500px", height: "500px" }}
      >
        Loading....
      </div>
    </div>
  );
}
