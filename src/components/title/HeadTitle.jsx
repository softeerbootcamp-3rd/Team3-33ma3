import React from "react";
import styled from "styled-components";

const TitleContainer = styled.div`
  display: flex;
  width: 100%;
  border-bottom: 3px solid ${(props) => props.theme.colors.border_default};
  padding: 60px 0px 20px 0px;
  justify-content: center;
  align-items: center;
`;

const Title = styled.h1`
  max-width: 1100px;
  width: 100%;
  font-size: ${(props) => props.theme.fontSize.title};
  font-weight: 700;
`;

function HeadTitle({ title }) {
  return (
    <TitleContainer>
      <Title>{title}</Title>
    </TitleContainer>
  );
}

export default HeadTitle;
