import React from "react";
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

  animation-name: show;
  -webkit-animation-name: show;

  animation-duration: 0.2s;
  -webkit-animation-duration: 0.2s;

  animation-timing-function: ease-out;
  -webkit-animation-timing-function: ease-out;

  @keyframes show {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }

  @-webkit-keyframes show {
    0% {
      opacity: 0;
    }
    100% {
      opacity: 1;
    }
  }
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

function ModalPortal({ width, title, handleClose, children }) {
  return createPortal(
    <Modal className="show">
      <ModalBody width={width}>
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
