import React from "react";
import styled from "styled-components";
import ImageBidPaddle from "../assets/bidPaddle.png";

const BidPaddleContainer = styled.button`
  display: flex;
  flex-direction: column;
  gap: 5px;
  width: 230px;
  text-align: center;
  transition: transform 0.2s;
  align-items: center;

  &:hover {
    transform: scale(1.05);
  }
`;

const Paddle = styled.div`
  position: relative;
`;

const Price = styled.p`
  position: absolute;
  font-weight: 700;
  margin-left: 64px;
  margin-top: 40px;
  width: 137px;
  font-size: ${(props) => props.theme.fontSize.large};
  color: ${(props) => props.theme.colors.text_strong};
`;

const CenterName = styled.p`
  font-weight: 500;
  font-size: ${(props) => props.theme.fontSize.regular};
  color: ${(props) => props.theme.colors.text_weak};
`;

function BidPaddle({ price, centerName, isActive }) {
  return (
    <BidPaddleContainer style={{ transform: isActive ? "scale(1.05)" : "" }}>
      <Paddle>
        <Price>{price}만</Price>
        <img src={ImageBidPaddle} style={{ width: "230px", height: "230px" }} />
      </Paddle>
      <CenterName>{centerName}</CenterName>
    </BidPaddleContainer>
  );
}

export default BidPaddle;
