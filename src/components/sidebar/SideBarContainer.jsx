import React from "react";
import styled from "styled-components";
import { SIDE_BAR_WIDTH } from "../../constants/layouts";

const SideBar = styled.div`
  width: ${SIDE_BAR_WIDTH};
  min-width: ${SIDE_BAR_WIDTH};
  display: flex;
  flex-direction: column;
`;

const SideBarTitle = styled.h1`
  font-size: ${({ theme }) => theme.fontSize.medium};
  color: ${({ theme }) => theme.colors.text_strong};
  font-weight: 700;
  margin-bottom: 80px;
`;

const SideBarContent = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`;

function SideBarContainer({ title, children }) {
  return (
    <SideBar>
      <SideBarTitle>{title}</SideBarTitle>
      <SideBarContent>{children}</SideBarContent>
    </SideBar>
  );
}

export default SideBarContainer;
