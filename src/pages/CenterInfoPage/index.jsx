import React, { useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import Carousel from "../../components/image/Carousel";
import OptionType from "../../components/post/OptionType";
import SubmitButton from "../../components/button/SubmitButton";
import OptionItem from "../../components/post/OptionItem";
import StarRating from "../../components/post/StarRating";
import StarImg from "../../assets/star.svg";
import ReviewComment from "./components/ReviewComment";

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 45px;
`;

const TopContainer = styled.div`
  padding-top: 70px;
  display: flex;
  gap: 45px;
  width: 100%;
`;

const TopContentContainer = styled.div`
  padding-top: 30px;
  width: 100%;
`;

const imgs = [StarImg, StarImg];

function CenterInfoPage() {
  return (
    <Page>
      <Content>
        <TopContainer>
          <Carousel imgList={imgs} thumbnail size="large" />
          <TopContentContainer>
            <OptionType title={"센터 정보"}>
              <OptionItem title={"센터 이름"}>민우 센터</OptionItem>
              <OptionItem title={"별점"}>
                <StarRating score={4} />
              </OptionItem>
            </OptionType>
          </TopContentContainer>
        </TopContainer>
        <OptionType title={"후기 목록"}>
          <ReviewComment />
        </OptionType>
      </Content>
    </Page>
  );
}

export default CenterInfoPage;
