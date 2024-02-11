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

export const URL = "http://192.168.1.141:8080/";
const KM_TO_M_CONVERSION_FACTOR = 1000;
const MIN_RADIUS = 0;
const MAX_RADIUS = 10;

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

function generateKeyBasedOnCurrentTime() {
  const currentTime = new Date().getTime();
  const key = `key_${currentTime}`;
  return key;
}

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

export function searchAddressToCoordinate(address, map, marker, circle) {
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
      circle.setCenter(point);
    }
  );
}

const LocationModal = forwardRef(function LocationModal({ props }, ref) {
  const [newMap, setNewMap] = useState();
  const [newMarker, setNewMarker] = useState();
  const [newAddress, setNewAddress] = useState();
  const [newCircle, setNewCircle] = useState();
  const [markerList, setMarkerList] = useState([]);

  const [isDragend, setIsDragend] = useState(false);

  const dialog = useRef();

  if (isDragend) {
    setIsDragend(false);
    const coords = newMarker.position;
    const radius = newCircle.getRadius() / KM_TO_M_CONVERSION_FACTOR;

    if (radius < MIN_RADIUS || radius > MAX_RADIUS) {
      alert("반경은 1이상 10이하까지 입력해주세요.");
      return;
    }

    // fetch(
    //   `${URL}location?latitude=${coords._lat}&longitude=${coords._lng}&radius=${
    //     radius || MIN_RADIUS
    //   }`
    // )
    //   .then((res) => {
    //     return res.json();
    //   })
    //   .then((data) => {
    //     const repairCenterList = data.data;
    //   })
    //   .catch((error) => {
    //     console.log(error);
    //   });
  }

  function handleCloseModal() {
    dialog.current.close();
  }

  function handleInputAddressChange(e) {
    const inputAddress = e.target.value;
    searchAddressToCoordinate(inputAddress, newMap, newMarker, newCircle);
  }

  function handleInputRadiusChange(e) {
    const inputRadius = e.target.value;
    const coords = newMarker.position;

    if (inputRadius < MIN_RADIUS || inputRadius > MAX_RADIUS) {
      alert("반경은 1이상 10이하까지 입력해주세요.");
      return;
    }

    newCircle.setRadius(inputRadius * KM_TO_M_CONVERSION_FACTOR);

    // fetch(
    //   `${URL}location?latitude=${coords._lat}&longitude=${coords._lng}&radius=${
    //     inputRadius || MIN_RADIUS
    //   }`
    // )
    //   .then((res) => {
    //     return res.json();
    //   })
    //   .then((data) => {
    //     const repairCenterList = data.data;
    //   })
    //   .catch((error) => {
    //     console.log(error);
    //   });
  }

  function handleSubmitOnClick() {}

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.showModal();
      },
    };
  });

  useEffect(() => {
    fetch(`${URL}center/all`)
      .then((res) => {
        return res.json();
      })
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
          };
          const marker = new naver.maps.Marker(markerOptions);

          return marker;
        });

        setMarkerList(() => markers);
      })
      .catch((error) => {
        console.log(error);
      });
  }, []);

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
            setDragend={setIsDragend}
          />
          <InputText
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
