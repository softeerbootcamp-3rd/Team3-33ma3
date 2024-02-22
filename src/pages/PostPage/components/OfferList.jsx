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
`;

function OfferList({ offerList, offerState, disabled }) {
  const [focusOffer, setFocusOffer] = useState();

  function clickOffer(index) {
    setFocusOffer(index);
  }

  // 기존에도 존재했던 offerId라면 hetch 애니메이션 실행
  const offers = offerList.map((offer, index) => (
    <AuctionPrice
      price={offer.price}
      centerName={offer.centerName}
      key={offer.offerId + "/" + offer.price + "/" + offer.contents}
      onClick={() => clickOffer(index)}
      isActive={focusOffer === index}
      isEdited={false}
      isEnd={disabled}
      isSelected={offer.selected}
      animation={
        offerState && offer.offerId === offerState.offerId && offerState.state
      }
    />
  ));

  return (
    <>
      <AuctionList>{offers}</AuctionList>
      {focusOffer !== undefined && (
        <Comment offerInfo={offerList[focusOffer]} disabled={disabled} />
      )}
    </>
  );
}

export default OfferList;
