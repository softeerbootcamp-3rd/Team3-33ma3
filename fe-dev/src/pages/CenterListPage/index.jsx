import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
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

function CenterListPage() {
  const [isLoading, setIsLoading] = useState(true);
  const [centerInfoList, setCenterInfoList] = useState([]);

  useEffect(() => {
    setIsLoading(true);
    fetch(`${BASE_URL}review`)
      .then((res) => {
        return res.json();
      })
      .then((json) => {
        console.log(json);
        setCenterInfoList(json.data);
        setIsLoading(false);
      });
  }, []);

  const centerList = centerInfoList.map((centerInfo) => (
    <Link
      to={`/center-review/info/?center_id=${centerInfo.centerId}`}
      key={centerInfo.centerId}
    >
      <CenterCardItem centerInfo={centerInfo} />
    </Link>
  ));

  return (
    <Page title={"센터 후기 목록"}>
      <Content>
        <ReviewListContainer>
          {isLoading ? (
            <p>Loading...</p>
          ) : (
            <>
              <ReviewListHeader>
                <p>{centerInfoList.length} 건</p>
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
