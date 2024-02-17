import React from "react";
import { createPortal } from "react-dom";
import styled from "styled-components";
import CloseImg from "../../assets/close.svg";

const Modal = styled.div`
  position: absolute;
  justify-content: center;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  background-color: rgba(134, 134, 134, 0.53);
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
  z-index: 2;
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

const Title = styled.p`
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.medium};
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
    <Modal>
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
