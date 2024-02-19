import React from "react";
import styled from "styled-components";
import Person from "../../assets/person.svg";
import SpeechBubble from "../../assets/speech_bubble.svg";
import PrizeImg from "../../assets/1st_prize.svg";

const Container = styled.button`
  display: flex;
  width: 175px;
  position: relative;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  transition: transform 0.2s;
  font-weight: 500;
  font-size: ${(props) => props.theme.fontSize.regular};
  color: ${(props) => props.theme.colors.text_strong};
  transform: ${({ $isActive }) => $isActive && "scale(1.05)"};

  &:hover {
    transform: scale(1.05);
  }

  &.pullUp {
    animation-name: pullUp;
    -webkit-animation-name: pullUp;
  }

  animation-duration: 1.1s;
  -webkit-animation-duration: 1.1s;

  animation-timing-function: ease-out;
  -webkit-animation-timing-function: ease-out;

  transform-origin: 50% 100%;
  -ms-transform-origin: 50% 100%;
  -webkit-transform-origin: 50% 100%;

  @keyframes pullUp {
    0% {
      transform: scaleY(0.1);
    }
    40% {
      transform: scaleY(1.02);
    }
    60% {
      transform: scaleY(0.98);
    }
    80% {
      transform: scaleY(1.01);
    }
    100% {
      transform: scaleY(0.98);
    }
    80% {
      transform: scaleY(1.01);
    }
    100% {
      transform: scaleY(1);
    }
  }

  @-webkit-keyframes pullUp {
    0% {
      -webkit-transform: scaleY(0.1);
    }
    40% {
      -webkit-transform: scaleY(1.02);
    }
    60% {
      -webkit-transform: scaleY(0.98);
    }
    80% {
      -webkit-transform: scaleY(1.01);
    }
    100% {
      -webkit-transform: scaleY(0.98);
    }
    80% {
      -webkit-transform: scaleY(1.01);
    }
    100% {
      -webkit-transform: scaleY(1);
    }
  }
`;

const Bubble = styled.div`
  width: 147px;
  height: 147px;
  position: relative;

  &.floating {
    animation-name: floating;
    -webkit-animation-name: floating;

    animation-duration: 1.5s;
    -webkit-animation-duration: 1.5s;

    animation-iteration-count: infinite;
    -webkit-animation-iteration-count: infinite;
  }

  @keyframes floating {
    0% {
      transform: translateY(0%);
    }
    50% {
      transform: translateY(8%);
    }
    100% {
      transform: translateY(0%);
    }
  }

  @-webkit-keyframes floating {
    0% {
      -webkit-transform: translateY(0%);
    }
    50% {
      -webkit-transform: translateY(8%);
    }
    100% {
      -webkit-transform: translateY(0%);
    }
  }

  @keyframes pulse {
    0% {
      transform: scale(0.9);
      opacity: 0.7;
    }
    50% {
      transform: scale(1);
      opacity: 1;
    }
    100% {
      transform: scale(0.9);
      opacity: 0.7;
    }
  }

  @-webkit-keyframes pulse {
    0% {
      -webkit-transform: scale(0.95);
      opacity: 0.7;
    }
    50% {
      -webkit-transform: scale(1);
      opacity: 1;s
    }
    100% {
      -webkit-transform: scale(0.95);
      opacity: 0.7;
    }
  }

  &.hatch{
    animation-name: hatch;
    -webkit-animation-name: hatch;	
  
    animation-duration: 2s;	
    -webkit-animation-duration: 2s;
  
    animation-timing-function: ease-in-out;	
    -webkit-animation-timing-function: ease-in-out;
  
    transform-origin: 50% 100%;
    -ms-transform-origin: 50% 100%;
    -webkit-transform-origin: 50% 100%; 
  
    visibility: visible !important;		
  }
  
  @keyframes hatch {
    0% {
      transform: rotate(0deg) scaleY(0.9);
    }
    20% {
      transform: rotate(-2deg) scaleY(1.05);
    }
    35% {
      transform: rotate(2deg) scaleY(1);
    }
    50% {
      transform: rotate(-2deg);
    }	
    65% {
      transform: rotate(1deg);
    }	
    80% {
      transform: rotate(-1deg);
    }		
    100% {
      transform: rotate(0deg);
    }									
  }
  
  @-webkit-keyframes hatch {
    0% {
      -webkit-transform: rotate(0deg) scaleY(0.6);
    }
    20% {
      -webkit-transform: rotate(-2deg) scaleY(1.05);
    }
    35% {
      -webkit-transform: rotate(2deg) scaleY(1);
    }
    50% {
      -webkit-transform: rotate(-2deg);
    }	
    65% {
      -webkit-transform: rotate(1deg);
    }	
    80% {
      -webkit-transform: rotate(-1deg);
    }		
    100% {
      -webkit-transform: rotate(0deg);
    }		
  }
`;

const Price = styled.div`
  position: absolute;
  font-weight: 700;
  margin-top: 45px;
  width: 100%;
  font-size: ${(props) => props.theme.fontSize.large};
  color: ${(props) => props.theme.colors.text_strong};
`;

const bubbleImageSize = {
  width: "147px",
  height: "147px",
};

const Prize = styled.img`
  position: absolute;
  z-index: 2;
  width: 60px;
  height: 60px;
  left: 0;
`;

function AuctionPrice({
  price,
  centerName,
  isActive,
  isEnd,
  isEdited,
  isSelected,
  onClick,
}) {
  console.log("hi", !isEnd && !isEdited);
  return (
    <Container
      $isActive={isActive}
      onClick={onClick}
      className={!isEnd && !isEdited && "pullUp"}
    >
      {isSelected && <Prize src={PrizeImg} />}
      <Bubble className={isEdited && "hatch"}>
        <Price>{price}만</Price>
        <img src={SpeechBubble} style={bubbleImageSize} />
      </Bubble>
      <img src={Person} style={{ width: "161px", height: "113px" }} />
      <p>{centerName}</p>
    </Container>
  );
}

export default AuctionPrice;
