import React, { useState } from "react";
import styled from "styled-components";

const Button = styled.button`
  width: 100%;
  color: ${(props) =>
    props.$isActive
      ? props.theme.colors.text_strong
      : props.theme.colors.text_weak};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: ${(props) => (props.i$sActive ? 700 : 500)};
  padding: 10px 10px 10px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

function DropDownItemButton({ content, onClick }) {
  const [isActive, setIsActive] = useState(false);

  function toggle() {
    if (!onClick()) {
      return;
    }
    setIsActive(!isActive);
  }

  return (
    <Button onClick={toggle} $isActive={isActive}>
      {content}
    </Button>
  );
}

export default DropDownItemButton;
