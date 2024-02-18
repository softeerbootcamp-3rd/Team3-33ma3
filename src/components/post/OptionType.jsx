import React from "react";
import OptionTitle from "../title/OptionTitle";
import styled from "styled-components";

const OptionContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
`;

function OptionType({ children, title }) {
  return (
    <OptionContainer>
      <OptionTitle title={title} />
      {children}
    </OptionContainer>
  );
}

export default OptionType;
