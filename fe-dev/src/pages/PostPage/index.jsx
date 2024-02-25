import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import OptionType from "../../components/post/OptionType";
import SubmitButton from "../../components/button/SubmitButton";
import Content from "./components/Content";
import AuctionAverageStatus from "./components/AuctionAverageStatus";
import CarInfo from "./components/CarInfo";
import { BASE_URL } from "../../constants/url";
import { CENTER_TYPE } from "../../constants/options";
import AuctionResult from "./components/AuctionResult";

import {
  redirect,
  useNavigate,
  useRouteLoaderData,
  useSearchParams,
} from "react-router-dom";
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
  const [isLoading, setIsLoading] = useState(true);
  const [isWriter, setIsWriter] = useState();
  const [query, setQuery] = useSearchParams();
  const { accessToken, memberId } = useRouteLoaderData("root");
  const postId = query.get("post_id");
  const navigate = useNavigate();

  useEffect(() => {
    setIsLoading(true);
    const headers = accessToken ? { Authorization: accessToken } : {};
    fetch(BASE_URL + `post/one/${postId}`, {
      method: "GET",
      headers: headers,
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        } else if (res.status === 401) {
          navigate("/auth?mode=login");
        }
      })
      .then((json) => {
        console.log(json.data);

        // 작성자인지 확인
        setIsWriter(json.data.postDetail.writerId === Number(memberId));
        setPostData(json.data);
        setIsLoading(false);
      })
      .catch((error) => alert("로그인이 필요한 페이지입니다."));
  }, []);

  return (
    <Page>
      {isLoading ? (
        <p>Loading...</p>
      ) : (
        <PostContainer>
          <PostInfo>
            <CarInfo postData={postData.postDetail} />
            <FullColumn>
              <Content content={postData.postDetail.contents} />
            </FullColumn>
            <FullColumn>
              {postData.postDetail.dday !== -1 ? (
                isWriter ? (
                  <AuctionStatus
                    curOfferDetails={postData.offerDetails}
                    postId={postId}
                  />
                ) : (
                  <AuctionAverageStatus
                    curAvgPrice={postData.avgPrice}
                    curOfferDetail={postData.offerDetail}
                    postId={postId}
                  />
                )
              ) : (
                <AuctionResult
                  offerList={postData.offerDetails}
                  isWriter={isWriter}
                />
              )}
            </FullColumn>
          </PostInfo>
        </PostContainer>
      )}
    </Page>
  );
}

export default PostPage;
