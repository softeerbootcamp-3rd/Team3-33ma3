import React from "react";
import OptionTitle from "../../../components/title/OptionTitle";
import styled from "styled-components";

const OptionContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
`;

function Option({ children, title }) {
  return (
    <OptionContainer>
      <OptionTitle title={title} />
      {children}
    </OptionContainer>
  );
}

export default Option;
