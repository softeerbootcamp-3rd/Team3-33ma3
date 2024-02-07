import { forwardRef } from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import ViewCurrentLocation from "./ViewCurrentLocation";

const Dialog = styled.dialog`
  display: inline-flex;
  padding: 30px;
  flex-direction: column;
  align-items: center;
  gap: 30px;

  ${(props) => props.theme.dropShadow.floating}
`;

const LocationModal = forwardRef(function LocationModal(props, ref) {
  return createPortal(
    <>
      <Dialog ref={ref} className="location-modal">
        <ViewCurrentLocation />
      </Dialog>
    </>,
    document.getElementById("modal")
  );
});

export default LocationModal;
