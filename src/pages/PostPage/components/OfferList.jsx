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

function OfferList({ prevOfferList, offerList, disabled, handleSelectOffer }) {
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
      isEdited={!disabled && prevOfferList && prevOfferList.has(offer.offerId)}
      isEnd={disabled}
      isSelected={offer.selected}
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
          handleSelectOffer={() =>
            handleSelectOffer(offerList[focusOffer].offerId)
          }
          centerId={offerList[0].memberId}
          postId={offerList.postId}
        />
      )}
    </>
  );
}

export default OfferList;
