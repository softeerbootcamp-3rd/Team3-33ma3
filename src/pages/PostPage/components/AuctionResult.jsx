import React, { useState } from "react";
import styled from "styled-components";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";
import SubmitButton from "../../../components/button/SubmitButton";
import CreateReviewModal from "./CreateReviewModal";

const AuctionContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 55px;
`;

function AuctionResult({ isWriter, offerList }) {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
    <>
      {isModalOpen && (
        <CreateReviewModal handleClose={() => setIsModalOpen(false)} />
      )}
      <AuctionContainer>
        <OptionType title={"경매 결과"}>
          <OfferList offerList={offerList} disabled />
        </OptionType>
        {isWriter && (
          <SubmitButton onClick={() => setIsModalOpen(true)}>
            후기 작성하기
          </SubmitButton>
        )}
      </AuctionContainer>
    </>
  );
}

export default AuctionResult;
