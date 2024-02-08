import { useRef, useEffect, useState } from "react";
import { searchCoordinateToAddress } from "./LocationModal";
import { URL } from "./LocationModal";

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

async function fetchCurrentLocation() {
  try {
    const response = await getCurrentLocation();
    return response;
  } catch (error) {
    console.error(error.message);
    return { latitude: DEFAULT_LATITUDE, longitude: DEFAULT_LONGITUDE };
  }
}

function initMap(
  latitude,
  longitude,
  mapElement,
  setMap,
  setMarker,
  setNewAddress,
  setDragend
) {
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

  naver.maps.Event.addListener(map, "drag", (e) => {
    marker.setPosition(map.getCenter());
  });

  naver.maps.Event.addListener(map, "dragend", (e) => {
    const currentCoords = map.getCenter();
    map.setCenter(currentCoords);
    marker.setPosition(currentCoords);
    setMap(() => map);
    setMarker(() => marker);
    searchCoordinateToAddress(currentCoords, setNewAddress);
    setDragend(() => true);
  });

  return { map, marker };
}

export default function ViewCurrentLocation({
  setMap,
  setMarker,
  setAddress,
  setDragend,
}) {
  const mapElement = useRef();

  useEffect(() => {
    async function fetchAndSetLocation() {
      const currentLocation = await fetchCurrentLocation();
      const { map, marker } = initMap(
        currentLocation.latitude,
        currentLocation.longitude,
        mapElement.current,
        setMap,
        setMarker,
        setAddress,
        setDragend
      );
      setMap(map);
      setMarker(marker);
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
