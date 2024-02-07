import { forwardRef, useRef, useState } from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import ViewCurrentLocation from "./ViewCurrentLocation";
import InputText from "./input/InputText";

const Dialog = styled.dialog`
  display: inline-flex;
  padding: 30px;
  flex-direction: column;
  align-items: center;
  gap: 20px;

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

const LocationModal = forwardRef(function LocationModal(props, ref) {
  const [currentAddress, setCurrentAddress] = useState("");

  function handleLocation(address) {
    setCurrentAddress(address);
  }

  return createPortal(
    <>
      <Dialog ref={ref} className="location-modal" open>
        <TopContainer>
          <Title>위치선택</Title>
          <CloseButton>X</CloseButton>
        </TopContainer>
        <ViewCurrentLocation setNewAddress={handleLocation} />
        <InputText size={"small"} defaultValue={currentAddress} />
        <BottomContainer>
          반경
          <RadiusContainer>
            <InputText placeholder={0} />
            Km
          </RadiusContainer>
        </BottomContainer>
      </Dialog>
    </>,
    document.getElementById("modal")
  );
});

export default LocationModal;
