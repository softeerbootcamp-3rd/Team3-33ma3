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
  max-height: ${({ $isActive }) => ($isActive ? "4000px" : "0px")};
  width: 100%;
  overflow: hidden;
  transition: max-height 0.4s ease-in-out;
`;

const imgSize = {
  width: "10px",
  height: "7px",
};

function DropDownButton({ title, number, children }) {
  const [isActive, setIsActive] = useState(false);

  function toggle() {
    setIsActive(!isActive);
  }

  return (
    <>
      <Button onClick={toggle}>
        <TextContainer>
          <p>{title}</p>
          <CountItem>{number}</CountItem>
        </TextContainer>
        <img src={isActive ? SlideIn : SlideOut} style={imgSize} />
      </Button>
      <ChildContainer $isActive={isActive}>{children}</ChildContainer>
    </>
  );
}

export default DropDownButton;
