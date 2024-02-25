import React, { useState } from "react";
import styled from "styled-components";

const InputRangeContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_strong};
`;

const Range = styled.input`
  width: 100%;
  accent-color: grey;
  margin: 0;
`;

const RangeUnit = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: ${(props) => props.theme.fontSize.small};
  color: ${(props) => props.theme.colors.text_weak};
`;

function InputRadius(props) {
  const [value, setValue] = useState(1);

  function onChange(e) {
    const radius = e.target.value;
    setValue(radius);
    props.updateRadius(radius);
  }

  return (
    <InputRangeContainer>
      <p>반경 {value}km</p>
      <div>
        <Range
          type="range"
          min={1}
          max={10}
          onChange={onChange}
          value={value}
          name={props.name}
        />
        <RangeUnit>
          <p>1</p>
          <p>10</p>
        </RangeUnit>
      </div>
    </InputRangeContainer>
  );
}

export default InputRadius;
