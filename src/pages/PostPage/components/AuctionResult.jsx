import React from "react";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";

function AuctionResult({ offerList }) {
  return (
    <OptionType title={"경매 결과"}>
      <OfferList offerList={offerList} disabled />
    </OptionType>
  );
}

export default AuctionResult;
