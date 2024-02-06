import React from "react";
import styled from "styled-components";

const Input = styled.input`
  type: "text";
  width: ${(props) => (props.size === "small" ? "100%" : "160px")};
  font-size: ${(props) =>
    props.size === "small"
      ? props.theme.fontSize.medium
      : props.theme.fontSize.large};
  font-weight: ${(props) => (props.size === "small" ? "500" : "700")};
  padding: 10px;
  border: none;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  box-sizing: border-box;
  text-align: ${(props) => (props.size === "small" ? "left" : "center")};
  background: ${(props) => props.theme.colors.surface_white_weak};

  &:focus {
    outline: none;
    background: ${(props) => props.theme.colors.surface_weak};
  }
`;

function InputText({ size, placeholder, onChange }) {
  return (
    <Input size={size} placeholder={placeholder} onChange={onChange}></Input>
  );
}

export default InputText;
