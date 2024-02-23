import styled from "styled-components";
import { useState, useRef, useEffect } from "react";
import ModalPortal from "../../../components/modal/ModalPortal";
import ViewCurrentLocation from "../../../components/ViewCurrentLocation";
import InputText from "../../../components/input/InputText";
import generateKeyBasedOnCurrentTime from "../../../utils/generateKeyBasedOnCurrentTime";
import SubmitButton from "../../../components/button/SubmitButton";
import {
  searchAddressToCoordinate,
  searchCoordinateToAddress,
} from "../../../utils/locationUtils";

const MapContainer = styled.div`
  display: inline-flex;
  align-items: center;
  gap: 10px;
  flex-direction: column;
`;

const AddressContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  gap: 10px;
  font-weight: 500;
`;

function SignUpModal(props) {
  const [newMap, setNewMap] = useState();
  const [newMarker, setNewMarker] = useState();
  const [newAddress, setNewAddress] = useState();

  const inputAddress = useRef();

  function handleInputAddressChange(e) {
    const address = e.target.value;
    searchAddressToCoordinate(address)
      .then((res) => {
        const point = res.point;
        newMap.setCenter(point);
        newMarker.setPosition(point);
        newCircle.setCenter(newMap.getCenter());
      })
      .catch((error) => console.log(error));
  }

  function handleSubmitOnClick() {
    const userInputAddress = inputAddress.current.value;
    props.onSave(userInputAddress);
    props.handleClose();
  }

  const updateData = {
    updateMap: setNewMap,
    updateMarker: setNewMarker,
    updateAddress: setNewAddress,
  };

  useEffect(() => {
    if (newMap) {
      naver.maps.Event.addListener(newMap, "drag", (e) => {
        const currentCoords = newMap.getCenter();
        newMarker.setPosition(currentCoords);
      });

      naver.maps.Event.addListener(newMap, "dragend", (e) => {
        const currentCoords = newMap.getCenter();
        searchCoordinateToAddress(currentCoords)
          .then((res) => {
            setNewAddress(res);
          })
          .catch((error) => console.log(error));
      });
    }
  }, [newMap]);

  return (
    <ModalPortal
      title={"위치입력"}
      width={"500px"}
      handleClose={props.handleClose}
    >
      <MapContainer>
        <ViewCurrentLocation updateData={updateData} type={"signUp"} />
        <AddressContainer>
          {"주소"}
          <InputText
            ref={inputAddress}
            key={generateKeyBasedOnCurrentTime()}
            defaultValue={newAddress}
            onChange={handleInputAddressChange}
            size="small"
          />
        </AddressContainer>
        <SubmitButton children={"저장"} onClick={handleSubmitOnClick} />
      </MapContainer>
    </ModalPortal>
  );
}

export default SignUpModal;
