import React from "react";
import styled from "styled-components";
import ModalPortal from "../../../components/modal/ModalPortal";
import SuccessImg from "../../../assets/success.svg";
import failImg from "../../../assets/fail.svg";

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;
  color: ${({ theme }) => theme.colors.text_strong};
  font-weight: 700;
  font-size: ${({ theme }) => theme.fontSize.medium};
  align-items: center;
  padding: 25px 0px;
`;

const Img = styled.img`
  width: 130px;
  height: 130px;
`;

const TextContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
  align-items: center;
`;

function ResultModal({ handleClose, endMessage }) {
  console.log(endMessage);

  const failMessage = endMessage.message
    .split(".")
    .map((item, index) => <p key={index}>{item}</p>);

  return (
    <ModalPortal
      width={"500px"}
      title={"경매 결과"}
      handleClose={handleClose}
      animation
    >
      <Content>
        <Img src={endMessage.data ? SuccessImg : failImg} />
        {endMessage.data ? (
          <p>{endMessage.message}</p>
        ) : (
          <TextContainer>{failMessage}</TextContainer>
        )}
      </Content>
    </ModalPortal>
  );
}

export default ResultModal;
