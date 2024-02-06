import { useRef, useEffect, useState } from "react";

const DEFAULT_LATITUDE = 37.3595704;
const DEFAULT_LONGITUDE = 127.105399;

export default function ViewCurrentLocation() {
  const mapElement = useRef();
  const [newMap, setNewMap] = useState();
  const [newLatitude, setNewLatitude] = useState(DEFAULT_LATITUDE);
  const [newLongitude, setNewLongitude] = useState(DEFAULT_LONGITUDE);

  useEffect(() => {
    navigator.geolocation.getCurrentPosition((position) => {
      setNewLatitude(position.coords.latitude);
      setNewLongitude(position.coords.longitude);
    });
    const center = new N.LatLng(newLatitude, newLongitude);
    const mapOptions = {
      center: center,
      zoom: 15,
      scaleControl: false,
    };

    const map = new N.Map(mapElement.current, mapOptions);
    setNewMap(map);
  }, [newLatitude, newLongitude]);

  return (
    <div style={{ width: "400px", height: "400px" }}>
      <div
        ref={mapElement}
        id="map"
        style={{ width: "100%", height: "100%" }}
      />
    </div>
  );
}
