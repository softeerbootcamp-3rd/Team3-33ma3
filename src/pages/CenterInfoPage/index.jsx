import React from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import Carousel from "../../components/image/Carousel";
import OptionType from "../../components/post/OptionType";
import OptionTitle from "../../components/title/OptionTitle";
import SubmitButton from "../../components/button/SubmitButton";
import OptionItem from "../../components/post/OptionItem";
import StarImg from "../../assets/star.svg";

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 45px;
`;

const Img = styled.img`
  width: 20px;
  height: 20px;
`;

const StarContainer = styled.div`
  display: flex;
  gap: 5px;
  align-items: center;
`;

const TopContainer = styled.div`
  padding-top: 70px;
  display: flex;
  gap: 45px;
  width: 100%;
`;

const ButtonContainer = styled.div`
  display: flex;
  align-items: center;
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
              <OptionItem title={"위치"}>
                경기도 평택시 안중읍 송담1로 65
              </OptionItem>
              <OptionItem title={"별점"}>
                <StarContainer>
                  <Img src={StarImg} />
                  <p>4.3 / 5</p>
                </StarContainer>
              </OptionItem>
              <ButtonContainer>
                <SubmitButton size="small">리뷰 작성</SubmitButton>
              </ButtonContainer>
            </OptionType>
          </TopContentContainer>
        </TopContainer>
        <OptionType title={"후기 목록"}></OptionType>
      </Content>
    </Page>
  );
}

export default CenterInfoPage;
