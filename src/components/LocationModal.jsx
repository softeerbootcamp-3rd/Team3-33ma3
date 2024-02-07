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

export default function LocationModal() {
  return createPortal(
    <>
      <Dialog className="location-modal" open>
        <ViewCurrentLocation />
      </Dialog>
    </>,
    document.getElementById("modal")
  );
}
