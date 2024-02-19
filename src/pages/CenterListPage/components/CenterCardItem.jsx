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
`;

const Img = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

const ContentContainer = styled.div`
  width: 100%;
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

function CenterCardItem() {
  return (
    <CenterCardContainer>
      <ImgContainer>
        <Img src={DummyImg} />
      </ImgContainer>
      <ContentContainer>
        <TextContainer>
          <h1>민우 센터</h1>
          <p>경기도 평택시 안중읍 송담 1로 65</p>
        </TextContainer>
        <BottomContainer>
          <StarRating score={4} />
          <p>리뷰 43개</p>
        </BottomContainer>
      </ContentContainer>
    </CenterCardContainer>
  );
}

export default CenterCardItem;
