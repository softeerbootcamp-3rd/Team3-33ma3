import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import OptionType from "../../components/post/OptionType";
import SubmitButton from "../../components/button/SubmitButton";
import Content from "./components/Content";
import AuctionAverageStatus from "./components/AuctionAverageStatus";
import CarInfo from "./components/CarInfo";
import { BASE_URL } from "../../constants/url";
import OfferList from "./components/OfferList";
import AuctionResult from "./components/AuctionResult";
import { useRouteLoaderData, useSearchParams } from "react-router-dom";
import AuctionStatus from "./components/AuctionStatus";

const PostContainer = styled.div`
  padding-top: 70px;
  display: flex;
  flex-direction: column;
  gap: 55px;
  align-items: center;
`;

const PostInfo = styled.div`
  display: grid;
  width: 100%;
  grid-template-columns: 1fr 2fr;
  gap: 45px;
`;

const FullColumn = styled.div`
  grid-column: 1/3;
`;

function PostPage() {
  const [postData, setPostData] = useState();
  const [offerList, setOfferList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [query, setQuery] = useSearchParams();
  const { accessToken } = useRouteLoaderData("root");
  const postId = query.get("post_id");

  useEffect(() => {
    setIsLoading(true);
    fetch(BASE_URL + `post/one/${postId}`, {
      method: "GET",
      headers: {
        Authorization: accessToken ? accessToken : null,
      },
    })
      .then((res) => res.json())
      .then((json) => {
        console.log(json.data);
        setPostData(json.data.postDetail);
        setOfferList(json.data.offerDetails);
        setIsLoading(false);
      });
  }, []);

  return (
    <Page>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <PostContainer>
          <PostInfo>
            <CarInfo postData={postData} />
            <FullColumn>
              <Content content={postData.contents} />
            </FullColumn>
            <FullColumn>
              <AuctionStatus curOfferList={offerList} postId={postId} />
            </FullColumn>
          </PostInfo>
          <SubmitButton>경매 참여</SubmitButton>
        </PostContainer>
      )}
    </Page>
  );
}

export default PostPage;
