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

const InputLength = styled.div`
  display: flex;
  width: 100%;
  padding: 10px;
  box-sizing: border-box;
  justify-content: flex-end;
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_weak};

  & > h3 {
    color: ${(props) => props.theme.colors.text_strong};
  }
`;

function TextArea({ length, maxLength, onChange }) {
  return (
    <>
      <Input onChange={onChange}></Input>
      <InputLength>
        <h3>{length}</h3>
        <p>/{maxLength}</p>
      </InputLength>
    </>
  );
}

export default TextArea;
