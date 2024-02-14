import React, { useEffect, useState } from "react";
import styled from "styled-components";
import SlideIn from "../../assets/slide_in.svg";
import SlideOut from "../../assets/slide_out.svg";

const Button = styled.button`
  width: 100%;
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 700;
  padding: 10px 10px 10px 0px;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const CountItem = styled.p`
  color: ${(props) => props.theme.colors.text_weak};
  font-size: ${(props) => props.theme.fontSize.small};
  font-weight: 500;
`;

const TextContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
`;

const ChildContainer = styled.div`
  width: 100%;
  overflow: hidden;
`;

const ArcodainButtonChild = styled.div`
  width: 100%;
  animation: ${({ isActive }) =>
      isActive ? "slide-in-dropdown" : "slide-out-dropdown"}
    0.4s ease;

  @keyframes slide-in-dropdown {
    0% {
      transform: translateY(-100%);
    }
    100% {
      transform: translateY(0);
    }
  }

  @keyframes slide-out-dropdown {
    0% {
      transform: translateY(0);
    }
    100% {
      transform: translateY(-100%);
    }
  }
`;

const imgSize = {
  width: "10px",
  height: "7px",
};

function DropDownButton({ title, number, children }) {
  const [isActive, setIsActive] = useState(false);
  const [visibilityAnimation, setVisibilityAnimation] = useState(false);

  function toggle() {
    setIsActive(!isActive);
  }

  useEffect(() => {
    if (isActive) {
      setVisibilityAnimation(true);
    } else {
      setTimeout(() => {
        setVisibilityAnimation(false);
      }, 400);
    }
  }, [isActive]);

  return (
    <>
      <Button onClick={toggle}>
        <TextContainer>
          <p>{title}</p>
          <CountItem>{number}</CountItem>
        </TextContainer>
        <img src={isActive ? SlideIn : SlideOut} style={imgSize} />
      </Button>
      {visibilityAnimation && (
        <ChildContainer>
          <ArcodainButtonChild isActive={isActive}>
            {children}
          </ArcodainButtonChild>
        </ChildContainer>
      )}
    </>
  );
}

export default DropDownButton;
