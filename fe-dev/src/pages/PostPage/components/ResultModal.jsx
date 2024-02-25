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
  return (
    <ModalPortal
      width={"500px"}
      title={"경매 결과"}
      handleClose={handleClose}
      animation
    >
      <Content>
        {endMessage.message !== "SELECT END" && (
          <Img src={endMessage.data ? SuccessImg : failImg} />
        )}
        {endMessage.message === "SELECT END" ? (
          <p>경매가 마감되었습니다.</p>
        ) : endMessage.data ? (
          <p>축하드립니다! 제시한 견적이 낙찰되셨습니다!</p>
        ) : (
          <TextContainer>
            <p>아쉽습니다.</p>
            <p>제시한 견적이 낙찰되지 못했습니다. 다음 기회를 노려주세요.</p>
          </TextContainer>
        )}
      </Content>
    </ModalPortal>
  );
}

export default ResultModal;
