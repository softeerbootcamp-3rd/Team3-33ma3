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

function OfferList({ offerList, disabled }) {
  const [focusOffer, setFocusOffer] = useState(null);

  function clickOffer(index) {
    setFocusOffer((prevState) => {
      if (prevState === index) {
        return null;
      }
      return index;
    });
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
      animation={offer.animation}
    />
  ));

  return (
    <>
      <AuctionList>{offers}</AuctionList>
      {focusOffer !== null && (
        <Comment offerInfo={offerList[focusOffer]} disabled={disabled} />
      )}
    </>
  );
}

export default OfferList;
