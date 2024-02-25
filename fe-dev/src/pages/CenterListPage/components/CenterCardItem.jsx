import React from "react";
import styled from "styled-components";
import DummyImg from "../../../assets/person.svg";
import StarRating from "../../../components/post/StarRating";

const CenterCardContainer = styled.div`
  display: flex;
  padding: 30px;
  width: 100%;
  background-color: ${({ theme }) => theme.colors.surface_default};
  border-radius: ${({ theme }) => theme.radiuses.radius_m};
  box-shadow: ${({ theme }) => theme.boxShadow.up};
  box-sizing: border-box;
  gap: 30px;
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.01);
  }
`;

const ImgContainer = styled.div`
  width: 123px;
  height: 123px;
  border-radius: ${({ theme }) => theme.radiuses.radius_s};
  overflow: hidden;
`;

const Img = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const ContentContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: ${({ theme }) => theme.colors.text_strong};
`;

const TextContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px;

  & > h1 {
    font-size: ${({ theme }) => theme.fontSize.medium};
    font-weight: 700;
  }

  & > p {
    font-size: ${({ theme }) => theme.fontSize.regular};
    font-weight: 500;
  }
`;

const BottomContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: ${({ theme }) => theme.fontSize.regular};
  font-weight: 500;
`;

function CenterCardItem({ centerInfo }) {
  return (
    <CenterCardContainer>
      <ImgContainer>
        <Img src={centerInfo.centerImage} />
      </ImgContainer>
      <ContentContainer>
        <TextContainer>
          <h1>{centerInfo.centerName}</h1>
        </TextContainer>
        <BottomContainer>
          <StarRating score={centerInfo.scoreAvg} />
          <p>리뷰 {centerInfo.reviewCount}개</p>
        </BottomContainer>
      </ContentContainer>
    </CenterCardContainer>
  );
}

export default CenterCardItem;
