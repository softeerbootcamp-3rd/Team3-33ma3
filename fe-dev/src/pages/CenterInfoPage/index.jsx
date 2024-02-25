import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import Carousel from "../../components/image/Carousel";
import OptionType from "../../components/post/OptionType";
import OptionItem from "../../components/post/OptionItem";
import StarRating from "../../components/post/StarRating";
import ReviewComment from "./components/ReviewComment";
import PictureArea from "../../components/image/PictureArea";
import { useParams, useSearchParams } from "react-router-dom";
import { BASE_URL } from "../../constants/url";

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
  flex: 1;
`;

function CenterInfoPage() {
  const [isLoading, setLoading] = useState(true);
  const [centerInfo, setCenterInfo] = useState();
  const [query, setQuery] = useSearchParams();
  const centerId = query.get("center_id");

  useEffect(() => {
    fetch(`${BASE_URL}review/${centerId}`)
      .then((res) => {
        if (res.ok) {
          return res.json();
        }
      })
      .then((json) => {
        console.log(json.data);
        setCenterInfo(json.data);
        setLoading(false);
      });
  }, []);

  return (
    <Page>
      <Content>
        {!isLoading && (
          <>
            <TopContainer>
              <PictureArea size="small" img={centerInfo.centerImage} />
              <TopContentContainer>
                <OptionType title={"센터 정보"}>
                  <OptionItem title={"센터 이름"}>
                    {centerInfo.centerName}
                  </OptionItem>
                  <OptionItem title={"별점"}>
                    <StarRating score={centerInfo.scoreAvg} />
                  </OptionItem>
                </OptionType>
              </TopContentContainer>
            </TopContainer>
            <OptionType title={"후기 목록"}>
              {centerInfo.reviews.map((review, index) => (
                <ReviewComment reviewInfo={review} key={index} />
              ))}
            </OptionType>
          </>
        )}
      </Content>
    </Page>
  );
}

export default CenterInfoPage;
