import React from "react";
import styled from "styled-components";

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
`;

function SelectCategory({ name, carCategoryList }) {
  return (
    <Select name={name}>
      {carCategoryList.map((category, index) => (
        <option key={index}>{category}</option>
      ))}
    </Select>
  );
}

export default SelectCategory;
