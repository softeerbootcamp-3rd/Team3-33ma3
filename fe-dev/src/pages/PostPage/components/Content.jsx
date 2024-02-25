import React from "react";
import styled from "styled-components";
import OptionType from "../../../components/post/OptionType";

const ContentContainer = styled.p`
  width: 100%;
  padding: 10px;
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  line-height: normal;
  color: ${(props) => props.theme.colors.text_strong};
`;

function Content({ content }) {
  return (
    <OptionType title={"세부 정보"}>
      <ContentContainer>{content}</ContentContainer>
    </OptionType>
  );
}

export default Content;
