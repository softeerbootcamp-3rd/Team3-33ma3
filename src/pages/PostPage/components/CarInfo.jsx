import React from "react";
import styled from "styled-components";
import Carousel from "../../../components/image/Carousel";
import OptionType from "../../../components/post/OptionType";
import OptionItem from "../../../components/post/OptionItem";
import ChipButton from "../../../components/button/ChipButton";

const ServiceList = styled.div`
  width: 100%;
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
`;

const OptionItems = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 30px;
`;

function CarInfo({ postData }) {
  return (
    <>
      <Carousel imgList={postData.imageList} size={"large"} thumbnail={true} />
      <OptionType title={"차량 정보"}>
        <OptionItems>
          <OptionItem title={"차종"}>
            <p>승용</p>
          </OptionItem>
          <OptionItem title={"모델"}>
            <p>제네시스</p>
          </OptionItem>
          <OptionItem title={"마감 기한"}>
            <p>3일 남음</p>
          </OptionItem>
          <OptionItem title={"지역"}>
            <p>서울시 강남구</p>
          </OptionItem>
          <OptionItem title={"수리 서비스"}>
            <ServiceList>
              {postData.repairList.map((service, index) => (
                <ChipButton type={service} key={index} block />
              ))}
            </ServiceList>
          </OptionItem>
          <OptionItem title={"정비 서비스"}>
            <ServiceList>
              {postData.tuneUpList.map((service, index) => (
                <ChipButton type={service} key={index} block />
              ))}
            </ServiceList>
          </OptionItem>
        </OptionItems>
      </OptionType>
    </>
  );
}

export default CarInfo;
