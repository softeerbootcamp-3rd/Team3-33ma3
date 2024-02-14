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

function PostPage({ postId }) {
  const [postData, setPostData] = useState();
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    fetch(BASE_URL + "post/one/" + postId)
      .then((res) => res.json())
      .then((json) => {
        console.log(json.data);
        setPostData(json.data[0]);
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
              <OptionType title={"경매 현황"}>
                <AuctionAverageStatus />
              </OptionType>
            </FullColumn>
            <FullColumn>
              <AuctionResult />
            </FullColumn>
          </PostInfo>
          <SubmitButton children={"경매 참여"} />
        </PostContainer>
      )}
    </Page>
  );
}

export default PostPage;
