import React from "react";
import styled from "styled-components";

const Button = styled.button`
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;

  padding: 10px 120px;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  background: ${(props) => props.theme.colors.surface_brand};

  &:hover {
    background: ${(props) => props.theme.colors.surface_opacity_75.color};
    opacity: ${(props) => props.theme.colors.surface_opacity_75.opacity};
  }
  &:active {
    background: ${(props) => props.theme.colors.surface_opacity_50.color};
    opacity: ${(props) => props.theme.colors.surface_opacity_50.opacity};
  }
`;

export default function SubmitButton({ children, onClick }) {
  return (
    <>
      <Button onClick={onClick}>{children}</Button>
    </>
  );
}
