import ViewCurrentLocation from "../../../components/ViewCurrentLocation";
import InputText from "../../../components/input/InputText";
import ModalPortal from "../../../components/modal/ModalPortal";
import styled from "styled-components";
import InputRadius from "./InputRadius";
import { useRef, useState } from "react";
import { searchAddressToCoordinate } from "../../../utils/locationUtils";

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

function MapModal() {
  const [viewInfo, setViewInfo] = useState({});
  const [markerList, setMarkerList] = useState([]);
  const inputAddress = useRef();

  return (
    <ModalPortal title={"위치입력"} width={"500px"}>
      <MapContainer>
        <ViewCurrentLocation />
        {"주소"}
        <InputText size="small" />
        <InputRadius name={"deadline"} />
      </MapContainer>
    </ModalPortal>
  );
}

export { MapModal };
