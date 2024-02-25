import React from "react";
import styled from "styled-components";
import { CONTENT_MAX_WIDTH } from "../../constants/layouts";
import HeadTitle from "../../components/title/HeadTitle";

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
`;

const Content = styled.div`
  max-width: ${CONTENT_MAX_WIDTH};
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  padding: 30px;
  box-sizing: border-box;
`;

function Page({ title, children }) {
  return (
    <PageContainer>
      {title && <HeadTitle title={title} />}
      <Content>{children}</Content>
    </PageContainer>
  );
}

export default Page;
