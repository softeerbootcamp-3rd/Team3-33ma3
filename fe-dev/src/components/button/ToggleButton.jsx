import React, { useState } from "react";
import styled from "styled-components";

const ToggleContainer = styled.div`
  width: 100%;
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 700;
  padding: 10px 10px 10px 0px;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const ToggleSwitch = styled.label`
  position: relative;
  display: inline-block;
  width: 45.7px;
  height: 20.33px;
`;

const ToggleSlider = styled.span`
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: 0.4s;
  transition: 0.4s;
  border-radius: 34px;

  &:before {
    position: absolute;
    content: "";
    height: 13px;
    width: 13px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    -webkit-transition: 0.4s;
    transition: 0.4s;
    border-radius: 50%;
  }
`;

const CheckBox = styled.input`
  opacity: 0;
  width: 0;
  height: 0;

  &:checked + ${ToggleSlider} {
    background-color: ${(props) => props.theme.colors.surface_brand};
  }

  &:focus + ${ToggleSlider} {
    box-shadow: 0 0 1px #2196f3;
  }

  &:checked + ${ToggleSlider}:before {
    -webkit-transform: translateX(26px);
    -ms-transform: translateX(26px);
    transform: translateX(26px);
  }
`;

function ToggleButton({ title, name, setIsDone }) {
  const [isActive, setIsActive] = useState(false);

  function handleToggle() {
    setIsDone((isActive) => !isActive);
    setIsActive((isActive) => !isActive);
  }

  return (
    <ToggleContainer>
      <p>{title}</p>
      <ToggleSwitch>
        <CheckBox
          type="checkbox"
          checked={isActive}
          onChange={handleToggle}
          name={name}
        />
        <ToggleSlider />
      </ToggleSwitch>
    </ToggleContainer>
  );
}

export default ToggleButton;
