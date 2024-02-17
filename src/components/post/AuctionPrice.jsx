import React from "react";
import styled from "styled-components";
import Person from "../../assets/person.svg";
import SpeechBubble from "../../assets/speech_bubble.svg";

const Container = styled.button`
  display: flex;
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

  animation-name: ${(props) => !props.isEnd && "pullUp"};
  -webkit-animation-name: ${(props) => !props.isEnd && "pullUp"};

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

  animation-name: ${(props) => props.inStatus && "floating"};
  -webkit-animation-name: ${(props) => props.inStatus && "floating"};

  animation-duration: 1.5s;
  -webkit-animation-duration: 1.5s;

  animation-iteration-count: infinite;
  -webkit-animation-iteration-count: infinite;

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

function AuctionPrice({
  price,
  centerName,
  isActive,
  inStatus,
  isEnd,
  onClick,
}) {
  return (
    <Container $isActive={isActive} onClick={onClick} isEnd={isEnd}>
      <Bubble inStatus={inStatus}>
        <Price>{price}만</Price>
        <img src={SpeechBubble} style={bubbleImageSize} />
      </Bubble>
      <img src={Person} style={{ width: "161px", height: "113px" }} />
      <p>{centerName}</p>
    </Container>
  );
}

export default AuctionPrice;
