import React, { useState } from "react";
import styled from "styled-components";
import Comment from "../../../components/post/Comment";
import AuctionPrice from "../../../components/post/AuctionPrice";

const AuctionList = styled.div`
  width: 100%;
  height: 330px;
  overflow-x: auto;
  display: flex;
  align-items: center;
  gap: 5px;
`;

function OfferList({ offerList, disabled }) {
  const [focusOffer, setFocusOffer] = useState();

  function clickOffer(index) {
    setFocusOffer(index);
  }

  console.log(offerList);
  const offers = offerList.map((offer, index) => (
    <AuctionPrice
      price={offer.price}
      centerName={offer.centerName}
      key={offer.offerId}
      onClick={() => clickOffer(index)}
      isActive={focusOffer === index}
    />
  ));

  return (
    <>
      <AuctionList>{offers}</AuctionList>
      {focusOffer !== undefined && (
        <Comment
          centerName={!disabled && offerList[focusOffer].centerName}
          contents={offerList[focusOffer].contents}
          disabled={disabled}
        />
      )}
    </>
  );
}

export default OfferList;
