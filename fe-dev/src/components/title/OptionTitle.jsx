import React from "react";
import styled from "styled-components";

const Title = styled.h1`
  width: 100%;
  border-bottom: 3px solid ${(props) => props.theme.colors.border_default};
  font-size: ${(props) => props.theme.fontSize.large};
  font-weight: 700;
  padding-bottom: 17px;
`;

function OptionTitle({ title }) {
  return <Title>{title}</Title>;
}

export default OptionTitle;
