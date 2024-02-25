import React, { useState } from "react";
import styled from "styled-components";
import ModalPortal from "../modal/ModalPortal";
import { useRouteLoaderData, useSearchParams } from "react-router-dom";
import SubmitButton from "../button/SubmitButton";
import { BASE_URL } from "../../constants/url";

const Content = styled.div`
  padding-top: 30px;
  display: flex;
  flex-direction: column;
  gap: 40px;
  width: 450px;
  align-items: center;
`;

const TextContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;

  & > h1 {
    font-weight: 700;
    font-size: ${({ theme }) => theme.fontSize.medium};
  }

  & > p {
    font-weight: 500;
    font-size: ${({ theme }) => theme.fontSize.medium};
  }
`;

const SuccessTextContent = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  width: 100%;
  align-items: center;

  & > h1 {
    font-weight: 700;
    font-size: ${({ theme }) => theme.fontSize.medium};
  }

  & > p {
    font-weight: 500;
    font-size: ${({ theme }) => theme.fontSize.regular};
  }
`;

function OfferSelectModal({ offerInfo, handleClose }) {
  const { accessToken } = useRouteLoaderData("root");
  const [searchParams] = useSearchParams();
  const [isSuccess, setIsSuccess] = useState(false);
  const postId = searchParams.get("post_id");

  // 댓글 낙찰
  function handleSelectOffer() {
    fetch(`${BASE_URL}post/${postId}/offer/${offerInfo.offerId}/select`, {
      method: "GET",
      headers: {
        Authorization: accessToken,
        "Content-type": "application/json",
      },
    })
      .then((res) => {
        if (res.ok) {
          res.json();
        } else {
          throw new Error("낙찰에 실패했습니다.");
        }
      })
      .then((json) => {
        console.log(json);
        setIsSuccess(true);
      });
  }

  function handleSuccess() {
    window.location.reload();
  }

  return (
    <ModalPortal
      title={isSuccess ? "낙찰에 성공하셨습니다!" : "낙찰하시겠습니까?"}
      handleClose={isSuccess ? handleSuccess : handleClose}
      animation={isSuccess}
    >
      <Content>
        {!isSuccess ? (
          <>
            <TextContent>
              <h1>가격: {offerInfo.price}</h1>
              <p>{offerInfo.contents}</p>
            </TextContent>
            <SubmitButton onClick={handleSelectOffer}>낙찰</SubmitButton>
          </>
        ) : (
          <>
            <SuccessTextContent>
              <h1>서비스 센터를 이용한 후</h1>
              <h1>후기를 남겨주시면 감사하겠습니다.</h1>
              <p>당신의 소중한 의견은 저희에게 큰 도움이 됩니다.</p>
            </SuccessTextContent>
            <SubmitButton onClick={handleSuccess}>확인</SubmitButton>
          </>
        )}
      </Content>
    </ModalPortal>
  );
}

export default OfferSelectModal;
