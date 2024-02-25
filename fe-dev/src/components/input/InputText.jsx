import React, { forwardRef } from "react";
import styled from "styled-components";

const Input = styled.input`
  type: "text";
  width: ${(props) => (props.size === "small" ? "100%" : "160px")};
  font-size: ${(props) =>
    props.size === "small"
      ? props.theme.fontSize.regular
      : props.theme.fontSize.medium};
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

const InputText = forwardRef(function InputText(
  { size, placeholder, onChange, defaultValue, name, type, value, min, max },
  ref
) {
  return (
    <Input
      ref={ref}
      size={size}
      type={type}
      placeholder={placeholder}
      onChange={onChange}
      defaultValue={defaultValue}
      name={name}
      value={value}
      min={min}
      max={max}
    ></Input>
  );
});

export default InputText;
