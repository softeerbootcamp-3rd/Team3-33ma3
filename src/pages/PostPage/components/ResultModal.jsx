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

function ResultModal({ selected, handleClose }) {
  return (
    <ModalPortal width={"500px"} title={"경매 결과"} handleClose={handleClose}>
      <Content>
        <Img src={selected ? SuccessImg : failImg} />

        {selected ? (
          <p>축하드립니다! 경매에 낙찰되셨습니다!</p>
        ) : (
          <TextContainer>
            <p>아쉽습니다. 낙찰에 실패하셨습니다.</p>{" "}
            <p>다음 기회를 노려보세요.</p>
          </TextContainer>
        )}
      </Content>
    </ModalPortal>
  );
}

export default ResultModal;
