import React from "react";
import styled from "styled-components";
import AuctionPrice from "../../../components/post/AuctionPrice";
import Comment from "../../../components/post/Comment";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";

const AwardedBid = styled.div`
  display: flex;
  flex-direction: row;
  gap: 30px;
`;

function AuctionResult({ offerList }) {
  console.log("result");
  return (
    <OptionType title={"경매 결과"}>
      {/* <AwardedBid>
        <AuctionPrice centerName={"강남"} price={"12"} isActive />
        <Comment centerName={"강남"} contents={"경매 성공자!"} disabled />
      </AwardedBid> */}
      <OfferList offerList={offerList} disabled />
    </OptionType>
  );
}

export default AuctionResult;
