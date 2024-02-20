import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import SideBar from "./components/SideBar";
import CarCardItem from "./components/CarCardItem";
import { BASE_URL } from "../../constants/url";
import { Link, useRouteLoaderData } from "react-router-dom";

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
  const [isMine, setIsMine] = useState();
  const { accessToken } = useRouteLoaderData("root");
  const headers = accessToken ? { Authorization: accessToken } : {};

  useEffect(() => {
    setIsLoading(true);
    fetch(`${BASE_URL}post`)
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((json) => {
        setThumnailList(json.data);
        setIsLoading(false);
      });
  }, []);

  useEffect(() => {
    setIsLoading(true);
    fetch(
      `${BASE_URL}post?mine=${isMine ? isMine : ""}&done=${
        isDone ? isDone : ""
      }&region=${regionList ? regionList.join(",") : ""}&repair=${
        repairList ? repairList.join(",") : ""
      }&tuneUp=${tuneUpList ? tuneUpList.join(",") : ""}`,
      {
        method: "GET",
        headers: headers,
      }
    )
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((json) => {
        setThumnailList(json.data);
        setIsLoading(false);
      });
  }, [regionList, repairList, tuneUpList, isDone, isMine]);

  const carList =
    !isLoading &&
    thumnailList.map((item) => (
      <Link to={`/post/info/?post_id=${item.postId}`} key={item.postId}>
        <CarCardItem cardInfo={item} />
      </Link>
    ));

  return (
    <Page title={"게시물 목록"}>
      <Content>
        <SideBar
          setRegionList={setRegionList}
          setRepairList={setRepairList}
          setTuneUpList={setTuneUpList}
          setIsDone={setIsDone}
          setIsMine={setIsMine}
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
