import { useRef, useEffect } from "react";
import { searchCoordinateToAddress } from "./LocationModal";

const DEFAULT_LATITUDE = 0;
const DEFAULT_LONGITUDE = 0;
const DEFAULT_ZOOM_SCALE = 15;

function getCurrentLocation() {
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      reject(new Error("Geolocation is not supported!"));
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const { latitude, longitude } = position.coords;
        resolve({ latitude, longitude });
      },
      (error) => {
        reject(error);
      }
    );
  });
}

function initMap(latitude, longitude, mapElement, setNewAddress) {
  const center = new naver.maps.LatLng(latitude, longitude);
  searchCoordinateToAddress(center, setNewAddress);

  const mapOptions = {
    center: center,
    zoom: DEFAULT_ZOOM_SCALE,
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

export default function ViewCurrentLocation({
  setMap,
  setMarker,
  setAddress,
  setCircle,
  setDragend,
}) {
  const mapElement = useRef();

  useEffect(() => {
    async function fetchAndSetLocation() {
      try {
        const currentLocation = await getCurrentLocation();
        const { map, marker, circle } = initMap(
          currentLocation.latitude,
          currentLocation.longitude,
          mapElement.current,
          setAddress
        );
        setMap(map);
        setMarker(marker);
        setCircle(circle);

        naver.maps.Event.addListener(map, "drag", (e) => {
          const currentCoords = map.getCenter();
          marker.setPosition(currentCoords);
          circle.setCenter(currentCoords);
        });

        naver.maps.Event.addListener(map, "dragend", (e) => {
          const currentCoords = map.getCenter();
          map.setCenter(currentCoords);
          marker.setPosition(currentCoords);

          setDragend(() => true);
          searchCoordinateToAddress(currentCoords, setAddress);
        });
      } catch (error) {
        console.error(
          console.error("Failed to fetch current location:", error)
        );
      }
    }
    fetchAndSetLocation();
  }, []);

  return (
    <div>
      <div
        ref={mapElement}
        id="map"
        style={{ width: "350px", height: "350px" }}
      >
        Loading....
      </div>
    </div>
  );
}
