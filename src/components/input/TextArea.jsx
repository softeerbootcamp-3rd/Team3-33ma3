import React from "react";
import styled from "styled-components";

const Input = styled.textarea`
  width: 100%;
  height: 300px;
  background: ${(props) => props.theme.colors.surface_white_weak};
  border: none;
  resize: none;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  box-sizing: border-box;
  font-size: ${(props) => props.theme.fontSize.medium};
  padding: 10px;

  &:focus {
    outline: none;
    background: ${(props) => props.theme.colors.surface_weak};
  }
`;

function TextArea({ onChange }) {
  return <Input onChange={onChange}></Input>;
}

export default TextArea;
