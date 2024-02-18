import React from "react";
import styled from "styled-components";
import StarRating from "../../../components/post/StarRating";
import ChipButton from "../../../components/button/ChipButton";

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

function ReviewComment() {
  const services = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13].map(() => (
    <ChipButton block type={"부품 교체"} />
  ));

  return (
    <CommentContainer>
      <Writer>익명</Writer>
      <Contents>좋았어요! 친절하고 굳굳</Contents>
      <BottomContainer>
        <StarRating score={3} />
        <ServiceList>{services}</ServiceList>
      </BottomContainer>
    </CommentContainer>
  );
}

export default ReviewComment;
