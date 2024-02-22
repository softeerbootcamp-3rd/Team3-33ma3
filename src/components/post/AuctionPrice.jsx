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

  &.slideExpandUp {
    animation-name: slideExpandUp;
    -webkit-animation-name: slideExpandUp;

    animation-duration: 0.9s;
    -webkit-animation-duration: 0.9s;

    animation-timing-function: ease-out;
    -webkit-animation-timing-function: ease -out;

    visibility: visible !important;
  }

  @keyframes slideExpandUp {
    0% {
      transform: translateY(100%) scaleX(0.5);
    }
    30% {
      transform: translateY(-8%) scaleX(0.5);
    }
    40% {
      transform: translateY(2%) scaleX(0.5);
    }
    50% {
      transform: translateY(0%) scaleX(1.1);
    }
    60% {
      transform: translateY(0%) scaleX(0.9);
    }
    70% {
      transform: translateY(0%) scaleX(1.05);
    }
    80% {
      transform: translateY(0%) scaleX(0.95);
    }
    90% {
      transform: translateY(0%) scaleX(1.02);
    }
    100% {
      transform: translateY(0%) scaleX(1);
    }
  }

  @-webkit-keyframes slideExpandUp {
    0% {
      -webkit-transform: translateY(100%) scaleX(0.5);
    }
    30% {
      -webkit-transform: translateY(-8%) scaleX(0.5);
    }
    40% {
      -webkit-transform: translateY(2%) scaleX(0.5);
    }
    50% {
      -webkit-transform: translateY(0%) scaleX(1.1);
    }
    60% {
      -webkit-transform: translateY(0%) scaleX(0.9);
    }
    70% {
      -webkit-transform: translateY(0%) scaleX(1.05);
    }
    80% {
      -webkit-transform: translateY(0%) scaleX(0.95);
    }
    90% {
      -webkit-transform: translateY(0%) scaleX(1.02);
    }
    100% {
      -webkit-transform: translateY(0%) scaleX(1);
    }
  }
`;

const Bubble = styled.div`
  width: 147px;
  height: 147px;
  position: relative;

  &.pulse {
    animation-name: pulse;
    -webkit-animation-name: pulse;

    animation-duration: 0.5s;
    -webkit-animation-duration: 0.5s;

    animation-iteration-count: 3;
    -webkit-animation-iteration-count: 3;
  }

  @keyframes pulse {
    0% {
      transform: scale(1);
      opacity: 0.7;
    }
    50% {
      transform: scale(1.1);
      opacity: 1;
    }
    100% {
      transform: scale(1);
      opacity: 0.7;
    }
  }

  @-webkit-keyframes pulse {
    0% {
      -webkit-transform: scale(1);
      opacity: 0.7;
    }
    50% {
      -webkit-transform: scale(1.1);
      opacity: 1;
    }
    100% {
      -webkit-transform: scale(1);
      opacity: 0.7;
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
  isSelected,
  onClick,
  animation,
}) {
  return (
    <Container
      $isActive={isActive}
      onClick={onClick}
      className={animation === "CREATE" && "slideExpandUp"}
    >
      {isSelected && <Prize src={PrizeImg} />}
      <Bubble className={animation === "UPDATE" && "pulse"}>
        <Price>{price}만</Price>
        <img src={SpeechBubble} style={bubbleImageSize} />
      </Bubble>
      <img src={Person} style={{ width: "161px", height: "113px" }} />
      <p>{isEnd ? "익명" : centerName}</p>
    </Container>
  );
}

export default AuctionPrice;
