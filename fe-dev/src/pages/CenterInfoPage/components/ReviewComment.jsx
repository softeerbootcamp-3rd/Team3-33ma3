import React, { useState } from "react";
import styled from "styled-components";
import StarRating from "../../../components/post/StarRating";
import ChipButton from "../../../components/button/ChipButton";
import SubmitButton from "../../../components/button/SubmitButton";
import { BASE_URL } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";

const CommentContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  font-size: ${(props) => props.theme.fontSize.regular};
  padding: 20px;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  box-shadow: ${(props) => props.theme.boxShadow.up};
  color: ${(props) => props.theme.colors.text_strong};
  box-sizing: border-box;
`;

const HeaderContainer = styled.div`
  display: flex;
  flex-direction: row;
  width: 100%;
  justify-content: space-between;
  align-items: center;
`;

const Writer = styled.p`
  display: flex;
  font-weight: 700;
  gap: 10px;
  align-items: center;
  font-size: ${(props) => props.theme.fontSize.medium};
`;

const Contents = styled.p`
  width: 100%;
  font-weight: 500;
  line-height: normal;
`;

const BottomContainer = styled.div`
  display: flex;
  gap: 20px;
`;

const ServiceList = styled.div`
  width: 0;
  flex: 1;
  overflow-x: auto;
  padding: 5px;
  display: flex;
  gap: 10px;
  white-space: nowrap;
  scrollbar-width: none;
`;

const UserContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: center;
`;

const ImgContainer = styled.div`
  width: 50px;
  height: 50px;
  border-radius: ${({ theme }) => theme.radiuses.radius_s};
  overflow: hidden;
`;

const Img = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

function ReviewComment({ reviewInfo }) {
  const { memberId, accessToken } = useRouteLoaderData("root");
  const [isLoading, setIsLoading] = useState(false);

  const services = [...reviewInfo.repairList, ...reviewInfo.tuneUpList].map(
    (type, index) => <ChipButton block type={type} key={index} />
  );

  function handleRemoveComment() {
    setIsLoading(true);
    fetch(`${BASE_URL}review/${reviewInfo.reviewId}`, {
      method: "DELETE",
      headers: {
        Authorization: accessToken,
      },
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        } else {
          throw new Error("댓글 삭제에 실패했습니다.");
        }
      })
      .then(() => window.location.reload())
      .catch((error) => alert(error.message))
      .finally(() => setIsLoading(false));
  }

  return (
    <CommentContainer>
      <HeaderContainer>
        <UserContainer>
          <ImgContainer>
            <Img src={reviewInfo.writerImage} />
          </ImgContainer>
          <Writer>{reviewInfo.writerName}</Writer>
        </UserContainer>
        {reviewInfo.writerId === Number(memberId) && (
          <SubmitButton
            disabled={isLoading}
            size={"small"}
            onClick={handleRemoveComment}
          >
            삭제
          </SubmitButton>
        )}
      </HeaderContainer>
      <Contents>{reviewInfo.contents}</Contents>
      <BottomContainer>
        <StarRating score={reviewInfo.score} />
        <ServiceList>{services}</ServiceList>
      </BottomContainer>
    </CommentContainer>
  );
}

export default ReviewComment;
