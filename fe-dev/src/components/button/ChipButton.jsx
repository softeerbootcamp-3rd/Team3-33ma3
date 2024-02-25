import styled from "styled-components";
import { useState } from "react";

const Button = styled.button`
  color: ${(props) => props.theme.colors.text_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  padding: 5px 10px;
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  box-shadow: 0px 0px 0px 1.5px ${(props) => props.theme.colors.border_strong};
  background: ${(props) => props.theme.colors.surface_default};
  transition: 0.3s;
  ${({ $block }) => $block && "pointer-events: none"};

  &:hover,
  &.active {
    box-shadow: 0px 0px 0px 2px ${(props) => props.theme.colors.surface_black};
    background: ${(props) => props.theme.colors.surface_default};
    color: ${(props) => props.theme.colors.text_strong};
    transition: 0.3s;
  }
`;

export default function ChipButton({ type, block, onClick }) {
  const [isActive, setIsActive] = useState(false);

  function handleOnclick() {
    setIsActive((prevStat) => !prevStat);
    onClick();
  }

  return (
    <Button
      type="button"
      className={isActive ? "active" : ""}
      onClick={handleOnclick}
      $block={block}
    >
      {type}
    </Button>
  );
}
