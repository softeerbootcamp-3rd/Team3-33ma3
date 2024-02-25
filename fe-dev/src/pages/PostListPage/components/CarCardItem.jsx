import React from "react";
import styled from "styled-components";
import Carousel from "../../../components/image/Carousel";
import ChipButton from "../../../components/button/ChipButton";
import CommentImg from "../../../assets/comment.svg";
import DeadLineCompleteImg from "../../../assets/deadline_completed.svg";
import useTimer from "../../../hooks/useTimer";

const CarCardContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 30px;
  width: 340px;
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

const RemainTime = styled.div`
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

const CommentChip = styled.div`
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  padding: 5px 10px;
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
  white-space: nowrap;
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
  const serviceList = [...cardInfo.repairList, ...cardInfo.tuneUpList].map(
    (service, index) => <ChipButton type={service} block key={index} />
  );

  return (
    <CarCardContainer>
      <CardHeader>
        <HeaderContext>
          <ModelName>{cardInfo.modelName}</ModelName>
          <CreateTime>{cardInfo.createTime}</CreateTime>
        </HeaderContext>
        {cardInfo.dday === -1 ? (
          <img
            src={DeadLineCompleteImg}
            style={{ width: "50px", height: "50px" }}
          />
        ) : cardInfo.dday > 0 ? (
          <RemainTime>D-{cardInfo.dday}</RemainTime>
        ) : (
          <RemainTime>{useTimer()}</RemainTime>
        )}
      </CardHeader>
      <CarouselContainer>
        <Carousel imgList={cardInfo.imageList} size={"medium"} />
      </CarouselContainer>
      <CardFooter>
        <Services>
          <ServiceList>{serviceList}</ServiceList>
        </Services>
        <ButtonContainer>
          <CommentChip>
            <img src={CommentImg} />
            <p>{cardInfo.offerCount}</p>
          </CommentChip>
        </ButtonContainer>
      </CardFooter>
    </CarCardContainer>
  );
}

export default CarCardItem;
