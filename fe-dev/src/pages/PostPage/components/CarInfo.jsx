import React from "react";
import styled from "styled-components";
import Carousel from "../../../components/image/Carousel";
import OptionType from "../../../components/post/OptionType";
import OptionItem from "../../../components/post/OptionItem";
import ChipButton from "../../../components/button/ChipButton";
import useTimer from "../../../hooks/useTimer";

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
  function getDDay(dDay) {
    if (dDay === -1) {
      return "마감 완료";
    }
    if (dDay === 0) {
      return useTimer();
    }
    return dDay;
  }

  return (
    <>
      <Carousel imgList={postData.imageList} size={"large"} thumbnail />
      <OptionType title={"차량 정보"}>
        <OptionItems>
          <OptionItem title={"차종"}>
            <p>{postData.carType}</p>
          </OptionItem>
          <OptionItem title={"모델"}>
            <p>{postData.modelName}</p>
          </OptionItem>
          <OptionItem title={"마감 기한"}>
            <p>{getDDay(postData.dday)}</p>
          </OptionItem>
          <OptionItem title={"지역"}>
            <p>{postData.regionName}</p>
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
