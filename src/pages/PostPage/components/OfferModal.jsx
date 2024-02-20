import React, { useState } from "react";
import styled from "styled-components";
import ModalPortal from "../../../components/modal/ModalPortal";
import InputText from "../../../components/input/InputText";
import TextArea from "../../../components/input/TextArea";
import SubmitButton from "../../../components/button/SubmitButton";
import { BASE_URL } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";

const OfferContainer = styled.form`
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  width: 100%;
`;

const OfferPrice = styled.div`
  display: flex;
  align-items: center;
  align-self: flex-start;
  font-size: ${({ theme }) => theme.fontSize.medium};
  color: ${({ theme }) => theme.colors.text_strong};
  font-weight: 700;
  gap: 10px;
`;

const ButtonContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 10px;
`;

function OfferModal({ handleClose, postId, updateOfferDetail, offerDetail }) {
  const { accessToken } = useRouteLoaderData("root");
  const [isLoading, setIsLoading] = useState(false);

  // 경매 참여
  function handleSubmitOffer(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    // 경매 참여
    if (!offerDetail) {
      console.log("경매 참여");
      submitOffer(formData);
    }
    // 기존 댓글 수정
    else {
      console.log("경매 수정");
      editOffer(formData);
    }
  }

  // 경매 참여
  function submitOffer(formData) {
    setIsLoading(true);
    const newOffer = { ...Object.fromEntries(formData.entries()) };
    console.log(newOffer);
    fetch(BASE_URL + `post/${postId}/offer`, {
      method: "POST",
      headers: {
        Authorization: accessToken,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newOffer),
    })
      .then((res) => {
        // if (res.ok) {
        //   return res.json();
        // } else {
        //   throw new Error("not ok");
        // }
        return res.json();
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail({ ...newOffer, offerId: json.data });
        setIsLoading(false);
        handleClose();
      });
  }

  // 입찰 댓글 수정
  function editOffer(formData) {
    setIsLoading(true);
    const newOffer = { ...Object.fromEntries(formData.entries()) };
    console.log(newOffer);
    console.log("수정");

    fetch(BASE_URL + `post/${postId}/offer/${offerDetail.offerId}`, {
      method: "PATCH",
      headers: {
        Authorization: accessToken,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ ...offerDetail, ...newOffer }),
    })
      .then((res) => {
        // if (res.ok) {
        //   return res.json();
        // } else {
        //   throw new Error("not ok");
        // }
        return res.json();
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail({ ...offerDetail, ...newOffer });
        setIsLoading(false);
        handleClose();
      });
  }

  // 입찰 댓글 삭제
  function handleDeleteOffer() {
    fetch(BASE_URL + `post/${postId}/offer/${offerDetail.offerId}`, {
      method: "DELETE",
      headers: {
        Authorization: accessToken,
        "Content-type": "application/json",
      },
    })
      .then((res) => {
        return res.json();
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail(null);
        handleClose();
      });
  }

  return (
    <ModalPortal title={"제시 금액"} width={"750px"} handleClose={handleClose}>
      <OfferContainer onSubmit={handleSubmitOffer}>
        <OfferPrice>
          <InputText
            placeholder={"1~1000"}
            name="price"
            type="number"
            defaultValue={offerDetail && offerDetail.price}
          />
          <p>만원</p>
        </OfferPrice>
        <TextArea
          maxLength={200}
          name="contents"
          placeholder={"견적 제시 방법, 수리 방법 등 자세하게 써주세요."}
          defaultValue={offerDetail && offerDetail.contents}
        />
        {offerDetail ? (
          <ButtonContainer>
            <SubmitButton type="button" onClick={handleDeleteOffer}>
              삭제
            </SubmitButton>
            <SubmitButton disabled={isLoading}>수정</SubmitButton>
          </ButtonContainer>
        ) : (
          <SubmitButton disabled={isLoading}>응찰</SubmitButton>
        )}
      </OfferContainer>
    </ModalPortal>
  );
}

export default OfferModal;
