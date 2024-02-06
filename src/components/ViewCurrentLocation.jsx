import { useRef, useEffect, useState } from "react";

const DEFAULT_LATITUDE = 0;
const DEFAULT_LONGITUDE = 0;

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
  }
}

function initMap(latitude, longitude, map) {
  const center = new naver.maps.LatLng(latitude, longitude);
  const mapOptions = {
    center: center,
    zoom: 15,
    scaleControl: false,
  };

  return new naver.maps.Map(map, mapOptions);
}

export default function ViewCurrentLocation() {
  const mapElement = useRef();
  const [newMap, setNewMap] = useState();
  const [newLatitude, setNewLatitude] = useState(DEFAULT_LATITUDE);
  const [newLongitude, setNewLongitude] = useState(DEFAULT_LONGITUDE);

  useEffect(() => {
    fetchCurrentLocation()
      .then((res) => {
        setNewLatitude(res.latitude);
        setNewLongitude(res.longitude);
      })
      .catch((error) => {
        console.error(error);
      });
    const map = initMap(newLatitude, newLongitude, mapElement.current);
    setNewMap(map);
  }, [newLatitude, newLongitude]);

  return (
    <div>
      <div
        ref={mapElement}
        id="map"
        style={{ width: "400px", height: "400px" }}
      />
    </div>
  );
}
