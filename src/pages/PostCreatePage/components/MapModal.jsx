import styled from "styled-components";
import InputRadius from "./InputRadius";
import { useRef, useState, useEffect } from "react";
import {
  searchAddressToCoordinate,
  searchCoordinateToAddress,
} from "../../../utils/locationUtils";
import ModalPortal from "../../../components/modal/ModalPortal";
import ViewCurrentLocation from "../../../components/ViewCurrentLocation";
import InputText from "../../../components/input/InputText";
import { BASE_URL } from "../../../constants/url";
import {
  DROP,
  KM_TO_M_CONVERSION_FACTOR,
} from "../../../constants/mapConstants";
import SubmitButton from "../../../components/button/SubmitButton";
import { generateKeyBasedOnCurrentTime } from "../../../components/LocationModal";

const MapContainer = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 10px;
  flex-direction: column;
`;

// 반경 내의 marker 출력, 그 외는 제외하는 함수
async function updateMarkers(map, circle, markers) {
  const response = await fetch(
    `${BASE_URL}location?latitude=${map.center._lat}&longitude=${
      map.center._lng
    }&radius=${circle.getRadius() / KM_TO_M_CONVERSION_FACTOR}`
  );

  const jsonData = await response.json();
  const centerList = jsonData.data;

  for (var i = 0; i < markers.length; i++) {
    const marker = markers[i].marker;
    const markerId = markers[i].centerId;

    const centerExists = centerList.some((obj) =>
      Object.values(obj).includes(Number(markerId))
    );
    if (centerExists) {
      showMarker(map, marker);
    } else {
      hideMarker(map, marker);
    }
  }
}

// marker 출력 함수
function showMarker(map, marker) {
  if (marker.getMap()) return;
  marker.setMap(map);
}

// marker 미출력 함수
function hideMarker(map, marker) {
  if (!marker.getMap()) return;
  marker.setMap(null);
}

// Modal 최상위 컴포넌트
function MapModal({ onSave, onSaveList, onSaveRadius }, ref) {
  const [newMap, setNewMap] = useState();
  const [newMarker, setNewMarker] = useState();
  const [newCircle, setNewCircle] = useState();
  const [newAddress, setNewAddress] = useState();
  const [markerList, setMarkerList] = useState([]);

  const inputAddress = useRef();

  function handleInputAddressChange(e) {
    const address = e.target.value;
    searchAddressToCoordinate(address)
      .then((res) => {
        const point = res.point;
        newMap.setCenter(point);
        newMarker.setPosition(point);
        newCircle.setCenter(newMap.getCenter());
        updateMarkers(newMap, newCircle, markerList);
      })
      .catch((error) => console.log(error));
  }

  function updateRadiusRangeChange(data) {
    newCircle.setRadius(data * KM_TO_M_CONVERSION_FACTOR);
    updateMarkers(newMap, newCircle, markerList);
  }

  function handleSubmitOnClick() {
    const userInputAddress = inputAddress.current.value;
    const centerList = markerList.filter((data) => data.marker.getMap());
    onSave(userInputAddress);
    onSaveList(centerList);
    onSaveRadius(newCircle.getRadius());
    setNewAddress(userInputAddress);
  }

  const updateData = {
    updateMap: setNewMap,
    updateMarker: setNewMarker,
    updateCircle: setNewCircle,
    updateAddress: setNewAddress,
  };

  useEffect(() => {
    if (newMap) {
      fetch(`${BASE_URL}center/all`)
        .then((res) => res.json())
        .then((data) => {
          const repairCenterList = data.data;
          const markers = repairCenterList.map((element) => {
            const position = new naver.maps.LatLng(
              element.latitude,
              element.longitude
            );
            const markerOptions = {
              map: newMap,
              position: position,
              title: element.centerName,
              animation: DROP,
              clickable: true,
            };
            const marker = new naver.maps.Marker(markerOptions);
            marker.setMap(null);

            return { centerId: element.centerId, marker: marker };
          });
          naver.maps.Event.addListener(newMap, "drag", (e) => {
            const currentCoords = newMap.getCenter();
            newMarker.setPosition(currentCoords);
            newCircle.setCenter(currentCoords);
          });

          naver.maps.Event.addListener(newMap, "dragend", (e) => {
            const currentCoords = newMap.getCenter();

            updateMarkers(newMap, newCircle, markers);
            searchCoordinateToAddress(currentCoords)
              .then((res) => {
                setNewAddress(res);
              })
              .catch((error) => console.log(error));
          });

          setMarkerList(markers);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, [newMap]);

  return (
    <ModalPortal title={"위치입력"} width={"500px"}>
      <MapContainer>
        <ViewCurrentLocation updateData={updateData} />
        {"주소"}
        <InputText
          key={generateKeyBasedOnCurrentTime()}
          defaultValue={newAddress}
          onChange={handleInputAddressChange}
          size="small"
        />
        <InputRadius updateRadius={updateRadiusRangeChange} name={"deadline"} />
        <SubmitButton children={"저장"} onClick={handleSubmitOnClick} />
      </MapContainer>
    </ModalPortal>
  );
}

export default MapModal;
