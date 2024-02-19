import React from "react";
import styled from "styled-components";
import AuctionPrice from "../../../components/post/AuctionPrice";
import Comment from "../../../components/post/Comment";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";

function AuctionResult({ offerList }) {
  console.log("result");
  return (
    <OptionType title={"경매 결과"}>
      <OfferList offerList={offerList} disabled />
    </OptionType>
  );
}

export default AuctionResult;
