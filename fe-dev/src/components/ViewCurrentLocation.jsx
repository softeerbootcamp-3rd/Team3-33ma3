import { useRef, useEffect } from "react";
import {
  getCurrentLocation,
  searchCoordinateToAddress,
} from "../utils/locationUtils";
import {
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
    icon: {
      url: "/src/assets/center_marker.png",
      origin: 0,
      anchor: 0,
      scaledSize: new naver.maps.Size(10, 10),
    },
  };
  const marker = new naver.maps.Marker(markerOptions);

  return { map, marker };
}

// Modal의 지도 컴포넌트
export default function ViewCurrentLocation(props) {
  const mapElement = useRef();

  useEffect(() => {
    // 현재 좌표 값 기반 비동기 처리 함수
    async function fetchAndSetLocation() {
      try {
        const currentCoords = await getCurrentLocation();
        const { map, marker } = initMap(
          currentCoords.latitude,
          currentCoords.longitude,
          mapElement.current,
          props.updateData.updateAddress
        );
        props.updateData.updateMap(map);
        props.updateData.updateMarker(marker);
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
