import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import SideBar from "./components/SideBar";
import CarCardItem from "../../components/CarCardItem";
import { BASE_URL } from "../../constants/url";
import { Link } from "react-router-dom";

const Content = styled.div`
  display: flex;
  flex: 1;
  padding-top: 50px;
  gap: 35px;
`;

const CardListContainer = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 15px;
`;

const CardListHeader = styled.div`
  display: flex;
  justify-content: space-between;
  font-size: ${({ theme }) => theme.fontSize.medium};
  font-weight: 700;
`;

const CardList = styled.div`
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 25px;
`;

function PostListPage() {
  const [thumnailList, setThumnailList] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [regionList, setRegionList] = useState();
  const [repairList, setRepairList] = useState();
  const [tuneUpList, setTuneUpList] = useState();
  const [isDone, setIsDone] = useState();

  useEffect(() => {
    setIsLoading(true);
    fetch(`${BASE_URL}post`)
      .then((res) => res.json())
      .then((json) => {
        setThumnailList(json.data);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  useEffect(() => {
    setIsLoading(true);
    fetch(
      `${BASE_URL}post?done=${isDone ? isDone : ""}&region=${
        regionList ? regionList.join(",") : ""
      }&repair=${repairList ? repairList.join(",") : ""}&tuneUp=${
        tuneUpList ? tuneUpList.join(",") : ""
      }`
    )
      .then((res) => res.json())
      .then((json) => {
        setThumnailList(json.data);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [regionList, repairList, tuneUpList, isDone]);

  const carList =
    !isLoading &&
    thumnailList.map((item) => (
      <Link to={`/post/info/?post_id=${item.postId}`} key={item.postId}>
        <CarCardItem cardInfo={item} />
      </Link>
    ));

  return (
    <Page title={"게시물 작성"}>
      <Content>
        <SideBar
          setRegionList={setRegionList}
          setRepairList={setRepairList}
          setTuneUpList={setTuneUpList}
          setIsDone={setIsDone}
        />
        <CardListContainer>
          {isLoading ? (
            <p>Loading...</p>
          ) : (
            <>
              <CardListHeader>
                <p>{thumnailList.length}건</p>
              </CardListHeader>
              <CardList>{carList}</CardList>
            </>
          )}
        </CardListContainer>
      </Content>
    </Page>
  );
}

export default PostListPage;
