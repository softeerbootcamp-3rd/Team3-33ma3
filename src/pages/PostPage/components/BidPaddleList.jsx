import React, { useState } from "react";
import styled from "styled-components";
import BidPaddle from "../../../components/BidPaddle";
import Comment from "../../../components/Comment";

const BidList = styled.div`
  width: 100%;
  height: 300px;
  overflow-x: auto;
  display: flex;
  align-items: center;
  gap: 5px;
`;

function BidPaddleList({ bidList, disabled }) {
  const [focusBid, setFocusBid] = useState();

  const dummy = [
    {
      offerId: 0,
      price: 10,
      centerName: "강남 서비스 센터1",
      contents: "설명11",
    },
    {
      offerId: 1,
      price: 10,
      centerName: "강남 서비스 센터2",
      contents: "설명22",
    },
    {
      offerId: 2,
      price: 10,
      centerName: "강남 서비스 센터3",
      contents: "설명33",
    },
    {
      offerId: 3,
      price: 10,
      centerName: "강남 서비스 센터4",
      contents: "설명44",
    },
    {
      offerId: 4,
      price: 10,
      centerName: "강남 서비스 센터5",
      contents: "설명55",
    },
  ];

  function clickBidPaddle(index) {
    setFocusBid(index);
  }

  const BidPaddles = dummy.map((bid, index) => (
    <BidPaddle
      price={bid.price}
      centerName={bid.centerName}
      key={bid.offerId}
      onClick={() => clickBidPaddle(index)}
      isActive={focusBid === index}
    />
  ));

  return (
    <>
      <BidList>{BidPaddles}</BidList>
      {focusBid !== undefined && (
        <Comment
          centerName={!disabled && dummy[focusBid].centerName}
          contents={dummy[focusBid].contents}
          disabled={disabled}
        />
      )}
    </>
  );
}

export default BidPaddleList;
