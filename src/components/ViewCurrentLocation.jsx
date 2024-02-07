import { useRef, useEffect, useState } from "react";

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

function initMap(latitude, longitude, mapElement, setNewAddress) {
  const center = new naver.maps.LatLng(latitude, longitude);
  const mapOptions = {
    center: center,
    zoom: DEFAULT_ZOOM_SCALE,
    scaleControl: false,
  };
  const map = new naver.maps.Map(mapElement, mapOptions);
  searchCoordinateToAddress(center, setNewAddress);
  const marker = new naver.maps.Marker({
    position: map.getCenter(),
    map: map,
  });

  naver.maps.Event.addListener(map, "drag", (e) => {
    marker.setPosition(map.getCenter());
  });

  naver.maps.Event.addListener(map, "dragend", (e) => {
    const currentCoords = map.getCenter();
    searchCoordinateToAddress(currentCoords, setNewAddress);
  });

  return map;
}

function searchCoordinateToAddress(latLng, setNewAddress) {
  naver.maps.Service.reverseGeocode(
    {
      coords: latLng,
      orders: [
        naver.maps.Service.OrderType.ADDR,
        naver.maps.Service.OrderType.ROAD_ADDR,
      ].join(","),
    },
    function (status, response) {
      if (status !== naver.maps.Service.Status.OK) {
        return alert("Something went wrong!");
      }
      const address = response.v2.address.roadAddress
        ? response.v2.address.roadAddress
        : response.v2.address.jibunAddress;
      setNewAddress(address);
    }
  );
}

export default function ViewCurrentLocation({ setNewAddress }) {
  const mapElement = useRef();
  const [newMap, setNewMap] = useState();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    async function fetchAndSetLocation() {
      const currentLocation = await fetchCurrentLocation();
      const map = initMap(
        currentLocation.latitude,
        currentLocation.longitude,
        mapElement.current,
        setNewAddress
      );
      setIsLoading(true);
      setNewMap(map);
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
