import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import SideBar from "./components/SideBar";
import CenterCardItem from "./components/CenterCardItem";
import { Link } from "react-router-dom";
import { BASE_URL } from "../../constants/url";

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

const dummy = [
  {
    centerId: 3,
    scoreAvg: 4.5,
    reviewCount: 1,
    centerName: "소은점",
    link: "https://threethree.s3.ap-northeast-2.amazonaws.com/f5c4dbdd-4691-4813-bbfb-5d02e0364832.jpeg",
  },
  {
    centerId: 2,
    scoreAvg: 4.3,
    reviewCount: 1,
    centerName: "보경점",
    link: "https://threethree.s3.ap-northeast-2.amazonaws.com/2a395d3c-30a6-4c25-aa8d-8e2c98dc553d.jpeg",
  },
  {
    centerId: 5,
    scoreAvg: 4.2,
    reviewCount: 1,
    centerName: "땡땡점",
    link: "https://threethree.s3.ap-northeast-2.amazonaws.com/profile.png",
  },
];

function CenterListPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [isDone, setIsDone] = useState();
  const [repairList, setRepairList] = useState();
  const [tuneUpList, setTuneUpList] = useState();
  const [centerInfoList, setCenterInfoList] = useState();

  useEffect(() => {
    setIsLoading(true);
    fetch(`${BASE_URL}review`)
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((json) => {
        setCenterInfoList(json);
        setIsLoading(false);
      });
  }, []);

  const centerList = dummy.map((centerInfo) => (
    <Link to={`/center-review/info/?center_id=${centerInfo.centerId}`}>
      <CenterCardItem centerInfo={centerInfo} />
    </Link>
  ));

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
                <p>{dummy.length} 건</p>
              </ReviewListHeader>
              <ReviewList>{centerList}</ReviewList>
            </>
          )}
        </ReviewListContainer>
      </Content>
    </Page>
  );
}

export default CenterListPage;
