import React from "react";
import styled from "styled-components";

const OptionItemContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
`;

const ItemTitle = styled.h3`
  width: 160px;
  color: ${(props) => props.theme.colors.text_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  margin-right: 30px;
`;

const ItemContent = styled.div`
  flex: 1;
`;

function OptionItem({ title, children }) {
  return (
    <OptionItemContainer>
      <ItemTitle>{title}</ItemTitle>
      <ItemContent>{children}</ItemContent>
    </OptionItemContainer>
  );
}

export default OptionItem;
