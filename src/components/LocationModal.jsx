import {
  forwardRef,
  useRef,
  useState,
  useEffect,
  useImperativeHandle,
} from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import ViewCurrentLocation from "./ViewCurrentLocation";
import InputText from "./input/InputText";
import SubmitButton from "./button/SubmitButton";
import { BASE_URL } from "../constants/url";

const KM_TO_M_CONVERSION_FACTOR = 1000;
const MIN_RADIUS = 0;
const MAX_RADIUS = 10;
const DROP = 2;

const Dialog = styled.dialog`
  padding: 30px;
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  box-shadow: ${(props) => props.theme.boxShadow.floating};
`;

const Title = styled.p`
  color: ${(props) => props.theme.colors.surface_black};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

const CloseButton = styled.button`
  width: 35px;
  height: 35px;
`;

const TopContainer = styled.div`
  display: flex;

  align-items: center;
  gap: 30px;
  width: 100%;
  justify-content: space-between;
`;

const BottomContainer = styled.div`
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
  display: flex;
  justify-content: space-between;
  align-items: center;
  align-self: stretch;
`;

const RadiusContainer = styled.div`
  display: flex;
  gap: 10px;
  align-items: center;
`;

const Wrapper = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 10px;
  flex-direction: column;
`;

// 날짜 기반 임의 키 생성 함수
export function generateKeyBasedOnCurrentTime() {
  const currentTime = new Date().getTime();
  const key = `key_${currentTime}`;
  return key;
}

// 좌표 기반 주소, 지번 상태 업데이트 함수
export function searchCoordinateToAddress(latLng, updateAddress) {
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
      updateAddress(address);
    }
  );
}

// 주소 기반 좌표 값으로 map, marker, circle의 center 및 반경 내 marker 업데이트 함수
export function searchAddressToCoordinate(
  address,
  map,
  marker,
  circle,
  markerList
) {
  naver.maps.Service.geocode(
    {
      query: address ? address : "DEFAULT",
    },
    function (status, response) {
      if (status === naver.maps.Service.Status.ERROR) {
        return alert("Something went Wrong!");
      }

      // 주소를 도로명으로 찾을 때, 건물명까지 입력하지 않으면 응답받지 못함
      if (response.v2.meta.totalCount === 0) {
        return;
      }

      const item = response.v2.addresses[0]; // 찾은 주소 정보
      const point = new naver.maps.Point(Number(item.x), Number(item.y)); // 지도에서 이동할 좌표
      map.setCenter(point);
      marker.setPosition(point);
      circle.setCenter(map.getCenter());
      updateMarkers(map, circle, markerList);
    }
  );
}

// 반경 내의 marker 출력, 그 외는 제외하는 함수
async function updateMarkers(map, circle, markers) {
  const response = await fetch(
    `${BASE_URL}location?latitude=${map.center._lat}&longitude=${
      map.center._lng
    }&radius=${circle.getRadius() / 1000}`
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
const LocationModal = forwardRef(function LocationModal(
  { onSave, onSaveList, onSaveRadius },
  ref
) {
  const [newMap, setNewMap] = useState();
  const [newMarker, setNewMarker] = useState();
  const [newAddress, setNewAddress] = useState();
  const [newCircle, setNewCircle] = useState();
  const [markerList, setMarkerList] = useState([]);

  const dialog = useRef();
  const inputAddress = useRef();

  function handleCloseModal() {
    dialog.current.close();
  }

  function handleInputAddressChange(e) {
    const inputAddress = e.target.value;
    searchAddressToCoordinate(
      inputAddress,
      newMap,
      newMarker,
      newCircle,
      markerList
    );
  }

  function handleInputRadiusChange(e) {
    const inputRadius = e.target.value;

    if (inputRadius < MIN_RADIUS || inputRadius > MAX_RADIUS) {
      alert("반경은 1이상 10이하까지 입력해주세요.");
      e.target.value = 0;
      return;
    }

    newCircle.setRadius(inputRadius * KM_TO_M_CONVERSION_FACTOR);

    updateMarkers(newMap, newCircle, markerList);
  }

  function handleSubmitOnClick() {
    const userInputAddress = inputAddress.current.value;
    const centerList = markerList.filter((data) => data.marker.getMap());
    onSave(userInputAddress);
    onSaveList(centerList);
    onSaveRadius(newCircle.getRadius());
    setNewAddress(userInputAddress);
    dialog.current.close();
  }

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.showModal();
      },
    };
  });

  useEffect(() => {
    if (newMap) {
      fetch(`${BASE_URL}center/all`)
        .then((res) => {
          console.log(res);
          return res.json();
        })
        .then((data) => {
          console.log(data);
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
            searchCoordinateToAddress(currentCoords, setNewAddress);
          });

          setMarkerList(() => markers);
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }, [newMap]);

  return createPortal(
    <>
      <Dialog ref={dialog} className="location-modal">
        <Wrapper>
          <TopContainer>
            <Title>위치선택</Title>
            <CloseButton onClick={handleCloseModal}>X</CloseButton>
          </TopContainer>
          <ViewCurrentLocation
            setMap={setNewMap}
            setMarker={setNewMarker}
            setAddress={setNewAddress}
            setCircle={setNewCircle}
          />
          <InputText
            ref={inputAddress}
            key={generateKeyBasedOnCurrentTime()}
            defaultValue={newAddress}
            size={"small"}
            onChange={handleInputAddressChange}
          />
          <BottomContainer>
            반경
            <RadiusContainer>
              <InputText placeholder={0} onChange={handleInputRadiusChange} />
              Km
            </RadiusContainer>
          </BottomContainer>
          <SubmitButton children={"저장"} onClick={handleSubmitOnClick} />
        </Wrapper>
      </Dialog>
    </>,
    document.getElementById("modal")
  );
});

export default LocationModal;
