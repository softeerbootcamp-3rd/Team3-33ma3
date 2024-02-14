import React from "react";
import styled from "styled-components";
import Carousel from "./image/Carousel";
import ChipButton from "./button/ChipButton";
import Car from "../assets/person.svg";
import Comment from "../assets/comment.svg";
import Timer from "./Timer";
import DeadLineComplete from "../assets/deadline_completed.svg";

const CarCardContainer = styled.button`
  display: flex;
  flex-direction: column;
  padding: 30px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.surface_white_weak};
  border-radius: ${({ theme }) => theme.radiuses.radius_m};
  gap: 23px;
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.01);
  }
`;

const CardHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 23px;
  width: 100%;
`;

const HeaderContext = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: flex-start;
`;

const ModelName = styled.p`
  font-size: ${(props) => props.theme.fontSize.large};
  font-weight: 700;
  color: ${(props) => props.theme.colors.text_strong};
`;

const CreateTime = styled.p`
  font-size: ${(props) => props.theme.fontSize.small};
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_strong};
`;

const RemainTime = styled.p`
  font-size: ${(props) => props.theme.fontSize.large};
  font-weight: 700;
  color: ${(props) => props.theme.colors.text_red};
`;

const ServiceList = styled.div`
  overflow-x: auto;
  display: flex;
  padding: 5px 2px;
  gap: 5px;
`;

const CommentButton = styled.button`
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  padding: 2px 14px;
  border-radius: ${(props) => props.theme.radiuses.radius_m};
  background: ${(props) => props.theme.colors.surface_brand};
  box-shadow: 0px 0px 0px 1.5px ${(props) => props.theme.colors.surface_brand};
  display: flex;
  gap: 5px;
  align-items: center;
`;

const CarouselContainer = styled.div`
  width: 100%;
`;

const CardFooter = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: flex-end;
  gap: 30px;
`;

const Services = styled.div`
  width: 0;
  flex: 1;
  overflow-x: auto;
  -ms-overflow-style: none;
  scrollbar-width: none;

  ::-webkit-scrollbar {
    display: none;
  }
`;

const ButtonContainer = styled.div`
  padding: 5px 2px;
`;

function CarCardItem({ cardInfo }) {
  const images = [Car, Car];
  console.log(cardInfo.dDay);

  const repairServices = cardInfo.repairService.map((service) => (
    <ChipButton type={service} block />
  ));

  const tuneUpServices = cardInfo.tuneUpService.map((service) => (
    <ChipButton type={service} block />
  ));

  return (
    <CarCardContainer>
      <CardHeader>
        <HeaderContext>
          <ModelName>{cardInfo.modelName}</ModelName>
          <CreateTime>{cardInfo.createTime}</CreateTime>
        </HeaderContext>
        <RemainTime>
          {cardInfo.dDay === -1 ? (
            <img src={DeadLineComplete} />
          ) : cardInfo.dDay > 0 ? (
            <p>D-{cardInfo.dDay}</p>
          ) : (
            <Timer remainTime={cardInfo.remainTime} />
          )}
        </RemainTime>
      </CardHeader>
      <CarouselContainer>
        <Carousel imgList={images} size={"medium"} />
      </CarouselContainer>
      <CardFooter>
        <Services>
          <ServiceList>{repairServices}</ServiceList>
          <ServiceList>{tuneUpServices}</ServiceList>
        </Services>
        <ButtonContainer>
          <CommentButton>
            <img src={Comment} />
            <p>{cardInfo.offerCount}</p>
          </CommentButton>
        </ButtonContainer>
      </CardFooter>
    </CarCardContainer>
  );
}

export default CarCardItem;
