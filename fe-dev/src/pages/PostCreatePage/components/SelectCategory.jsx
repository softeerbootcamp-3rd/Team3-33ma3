import React from "react";
import styled from "styled-components";
import { CAR_CATEGORY_OPTIONS } from "../../../constants/options";

const Select = styled.select`
  -moz-appearance: none;
  -webkit-appearance: none;
  appearance: none;
  width: 100%;
  height: 100%;
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.regular};
  border: none;
  padding: 10px 0px;
`;

function SelectCategory({ name }) {
  return (
    <Select name={name}>
      {CAR_CATEGORY_OPTIONS.map((category, index) => (
        <option key={index}>{category}</option>
      ))}
    </Select>
  );
}

export default SelectCategory;
