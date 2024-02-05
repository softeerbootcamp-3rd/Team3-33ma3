import styled from "styled-components";
import { useState } from "react";

const Button = styled.button`
  color: ${(props) => props.theme.colors.text_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;

  padding: 5px 15px;
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  box-shadow: 0px 0px 0px 1.5px ${(props) => props.theme.colors.border_strong};
  background: ${(props) => props.theme.colors.surface_default};

  &:hover,
  &.active {
    box-shadow: 0px 0px 0px 1.5px ${(props) => props.theme.colors.surface_black};
    background: ${(props) => props.theme.colors.surface_default};
    color: ${(props) => props.theme.colors.text_strong};
  }
`;

export default function ChipButton({ type, onClick }) {
  const [isActive, setIsActive] = useState(false);

  function handleOnclick() {
    setIsActive((prevStat) => !prevStat);
    onClick(type);
  }

  return (
    <>
      <Button className={isActive ? "active" : ""} onClick={handleOnclick}>
        {type}
      </Button>
    </>
  );
}
