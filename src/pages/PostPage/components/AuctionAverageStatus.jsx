import React from "react";
import styled from "styled-components";

const TextContainer = styled.div`
  color: ${({ theme }) => theme.colors.text_strong};
  font-size: ${({ theme }) => theme.fontSize.medium};
  font-weight: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const BoldText = styled.p`
  color: ${(props) =>
    props.color === "red"
      ? props.theme.colors.text_red
      : props.theme.colors.text_strong};
  font-weight: 700;
`;

const Text = styled.div`
  display: flex;
`;
function AuctionAverageStatus({}) {
  return (
    <TextContainer>
      <Text>
        <p>내가 제시한 금액은 </p>
        <BoldText>10만원</BoldText>
        <p>입니다.</p>
      </Text>
      <Text>
        <p>현재 제시 금액의 </p>
        <BoldText color="red">평균가는 8만원</BoldText>
        <p>입니다. 수정하시겠습니까?</p>
      </Text>
    </TextContainer>
  );
}

export default AuctionAverageStatus;
