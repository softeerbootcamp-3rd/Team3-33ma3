import React, { useEffect } from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import CloseImg from "../../assets/close.svg";
import { MIN_WIDTH } from "../../constants/layouts";

const Modal = styled.div`
  position: absolute;
  justify-content: center;
  top: 0;
  left: 0;
  width: 100%;
  min-width: ${MIN_WIDTH};
  height: 100vh;
  background-color: rgba(134, 134, 134, 0.53);
  z-index: 5;
`;

const ModalBody = styled.div`
  padding: 30px;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: ${({ width }) => width};
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  box-shadow: ${(props) => props.theme.boxShadow.floating};
  background-color: ${({ theme }) => theme.colors.surface_default};
  z-index: 6;
  display: flex;
  flex-direction: column;
  gap: 20px;

  &.expandOpen {
    animation-name: expandOpen;
    -webkit-animation-name: expandOpen;

    animation-duration: 0.9s;
    -webkit-animation-duration: 0.9s;

    animation-timing-function: ease-out;
    -webkit-animation-timing-function: ease-out;

    visibility: visible !important;
  }

  @keyframes expandOpen {
    0% {
      transform: translate(-50%, -50%) scale(1.8);
    }
    50% {
      transform: translate(-50%, -50%) scale(0.95);
    }
    80% {
      transform: translate(-50%, -50%) scale(1.05);
    }
    90% {
      transform: translate(-50%, -50%) scale(0.98);
    }
    100% {
      transform: translate(-50%, -50%) scale(1);
    }
  }
`;

const Title = styled.p`
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.large};
  font-weight: 700;
`;

const ModalHeader = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: center;
`;

const CloseButton = styled.button`
  width: 35px;
  height: 35px;
`;

const ModalContent = styled.div`
  width: 100%;
`;

function ModalPortal({ width, title, handleClose, animation, children }) {
  return createPortal(
    <Modal className="show">
      <ModalBody
        id="modal-body"
        width={width}
        className={animation && "expandOpen"}
      >
        <ModalHeader>
          <Title>{title}</Title>
          <CloseButton onClick={handleClose}>
            <img src={CloseImg} style={{ width: "35px", height: "35px" }} />
          </CloseButton>
        </ModalHeader>
        <ModalContent>{children}</ModalContent>
      </ModalBody>
    </Modal>,
    document.getElementById("modal")
  );
}

export default ModalPortal;
