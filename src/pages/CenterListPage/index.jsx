import React, { useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import SideBar from "./components/SideBar";
import CenterCardItem from "./components/CenterCardItem";
import { Link } from "react-router-dom";

const Content = styled.div`
  display: flex;
  flex: 1;
  padding-top: 50px;
  gap: 35px;
`;

const ReviewListContainer = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 15px;
`;

const ReviewListHeader = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: ${({ theme }) => theme.fontSize.medium};
  font-weight: 700;
`;

const ReviewList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 15px;
`;

function CenterListPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [isDone, setIsDone] = useState();
  const [repairList, setRepairList] = useState();
  const [tuneUpList, setTuneUpList] = useState();

  return (
    <Page title={"센터 후기 목록"}>
      <Content>
        <SideBar
          setRepairList={setRepairList}
          setTuneUpList={setTuneUpList}
          setIsDone={setIsDone}
        />
        <ReviewListContainer>
          {isLoading ? (
            <p>Loading...</p>
          ) : (
            <>
              <ReviewListHeader>
                <p>3건</p>
              </ReviewListHeader>
              <ReviewList>
                <Link to="/center-review/info">
                  <CenterCardItem />
                </Link>
              </ReviewList>
            </>
          )}
        </ReviewListContainer>
      </Content>
    </Page>
  );
}

export default CenterListPage;
