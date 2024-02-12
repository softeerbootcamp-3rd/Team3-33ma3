import React from "react";
import styled from "styled-components";
import { CONTENT_MAX_WIDTH } from "../../constants/layouts";

const TitleContainer = styled.div`
  display: flex;
  width: 100%;
  border-bottom: 3px solid ${(props) => props.theme.colors.border_default};
  padding: 60px 0px 20px 0px;
  justify-content: center;
  align-items: center;
`;

const Title = styled.h1`
  max-width: ${CONTENT_MAX_WIDTH};
  width: 100%;
  font-size: ${(props) => props.theme.fontSize.title};
  font-weight: 700;
  padding: 0px 0px 0px 30px;
  box-sizing: border-box;
`;

function HeadTitle({ title }) {
  return (
    <TitleContainer>
      <Title>{title}</Title>
    </TitleContainer>
  );
}

export default HeadTitle;
