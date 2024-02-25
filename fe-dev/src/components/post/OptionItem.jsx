import React from "react";
import styled from "styled-components";

const OptionItemContainer = styled.div`
  width: 100%;
  display: flex;
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_strong};
  font-size: ${(props) => props.theme.fontSize.regular};
  align-items: center;
`;

const ItemTitle = styled.p`
  width: 160px;
  color: ${(props) => props.theme.colors.text_default};
  margin-right: 30px;
`;

const ItemContent = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
  & > span {
    font-size: ${(props) => props.theme.fontSize.small};
    font-weight: 500;
    color: ${(props) => props.theme.colors.text_red};
  }
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
