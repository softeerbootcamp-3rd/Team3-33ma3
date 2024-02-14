import React, { useState } from "react";
import styled from "styled-components";

const ToggleSwitch = styled.label`
  position: relative;
  display: inline-block;
  width: 47.7px;
  height: 23.33px;
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
    height: 15px;
    width: 15px;
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

function ToggleButton({ onClick }) {
  const [isActive, setIsActive] = useState();

  function toggle() {
    setIsActive(!isActive);
    onClick();
  }

  return (
    <ToggleSwitch>
      <CheckBox type="checkbox" checked={isActive} onChange={toggle} />
      <ToggleSlider />
    </ToggleSwitch>
  );
}

export default ToggleButton;
