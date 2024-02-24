import React, { useEffect, useState, useRef } from "react";
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

function OfferList({ offerList, recentOfferId, disabled }) {
  const [focusOffer, setFocusOffer] = useState(null);
  const scrollRef = useRef();
  const newOffer = useRef();

  // 가장 최근에 바뀐 견적 제시로 포커스
  useEffect(() => {
    if (newOffer && newOffer.current) {
      newOffer.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [offerList]);

  useEffect(() => {
    if (scrollRef.current) {
      scrollRef.current.scrollIntoView({
        behavior: "smooth",
        block: "end",
        inline: "nearest",
      });
    }
  }, [focusOffer]);

  function clickOffer(index) {
    setFocusOffer((prevState) => {
      if (prevState === index) {
        return null;
      }
      return index;
    });
  }

  // 기존에도 존재했던 offerId라면 hetch 애니메이션 실행
  const offers = offerList.map((offer, index) => {
    const ref = offer.offerId === recentOfferId ? newOffer : null;
    return (
      <div
        ref={ref}
        key={offer.offerId + "/" + offer.price + "/" + offer.contents}
      >
        <AuctionPrice
          price={offer.price}
          centerName={offer.centerName}
          onClick={() => clickOffer(index)}
          isActive={focusOffer === index}
          isEdited={false}
          isEnd={disabled}
          isSelected={offer.selected}
          animation={offer.animation}
        />
      </div>
    );
  });

  return (
    <div ref={scrollRef}>
      <AuctionList>{offers}</AuctionList>
      {focusOffer !== null && (
        <Comment offerInfo={offerList[focusOffer]} disabled={disabled} />
      )}
    </div>
  );
}

export default OfferList;
