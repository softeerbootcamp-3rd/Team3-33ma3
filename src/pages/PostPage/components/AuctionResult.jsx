import React from "react";
import styled from "styled-components";
import BidPaddle from "../../../components/BidPaddle";
import Comment from "../../../components/Comment";
import BidPaddleList from "./BidPaddleList";
import OptionType from "../../../components/post/OptionType";

const AwardedBid = styled.div`
  display: flex;
  flex-direction: row;
  gap: 30px;
`;

function AuctionResult({ bidList }) {
  return (
    <OptionType title={"경매 결과"}>
      <AwardedBid>
        <BidPaddle centerName={"강남"} price={"12"} isActive />
        <Comment centerName={"강남"} contents={"경매 성공자!"} disabled />
      </AwardedBid>
      <BidPaddleList bidList={bidList} disabled />
    </OptionType>
  );
}

export default AuctionResult;
