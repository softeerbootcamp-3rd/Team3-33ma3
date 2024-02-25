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

const SubmitItems = styled.div`
  display: flex;
  flex-direction: column;
  gap: 3px;
  width: 100%;

  & > span {
    font-size: ${({ theme }) => theme.fontSize.small};
    color: ${({ theme }) => theme.colors.text_red};
    font-weight: 500;
  }
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
  const [errorForm, setErrorForm] = useState({});

  // 경매 참여
  function handleSubmitOffer(e) {
    e.preventDefault();

    const formData = new FormData(e.target);
    const newOffer = { ...Object.fromEntries(formData.entries()) };

    if (!validateForm(newOffer)) {
      return;
    }
    // 경매 참여
    if (!offerDetail) {
      console.log("경매 참여");
      submitOffer(newOffer);
    }
    // 기존 댓글 수정
    else {
      console.log("경매 수정");
      editOffer(newOffer);
    }
  }

  // 경매 참여
  function submitOffer(newOffer) {
    setIsLoading(true);
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
        //   throw new Error("입찰에 오류가 발생했습니다.");
        // }
        return res.json();
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail({ ...newOffer, offerId: json.data });
        setIsLoading(false);
        handleClose();
      })
      .catch((error) => alert(error.message));
  }

  // 입찰 댓글 수정
  function editOffer(newOffer) {
    setIsLoading(true);

    fetch(BASE_URL + `post/${postId}/offer/${offerDetail.offerId}`, {
      method: "PATCH",
      headers: {
        Authorization: accessToken,
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ ...offerDetail, ...newOffer }),
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        } else {
          throw new Error("입찰 수정에 오류가 발생했습니다.");
        }
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail({ ...offerDetail, ...newOffer });
        setIsLoading(false);
        handleClose();
      })
      .catch((error) => alert(error.message));
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
        if (res.ok) {
          return res.json();
        } else {
          throw new Error("입찰 댓글 삭제에 오류가 발생했습니다.");
        }
      })
      .then((json) => {
        console.log(json);
        updateOfferDetail(null);
        handleClose();
      })
      .catch((error) => alert(error.message));
  }

  function validateForm(offer) {
    const errors = {};

    if (offer.price <= 0 || offer.price > 1000) {
      errors.price = "1~1000 사이의 금액을 입력해주세요.";
    }

    if (offerDetail && Number(offer.price) > Number(offerDetail.price)) {
      errors.price = `기존 금액보다 낮은 금액만 입력 가능합니다. ${offerDetail.price}보다 낮은 금액을 입력해주세요.`;
    }

    if (offer.contents.length <= 0) {
      errors.contents = "내용을 입력해주세요.";
    }

    setErrorForm(errors);
    return Object.keys(errors).length === 0;
  }

  return (
    <ModalPortal title={"제시 금액"} width={"750px"} handleClose={handleClose}>
      <OfferContainer onSubmit={handleSubmitOffer}>
        <SubmitItems>
          <OfferPrice>
            <InputText
              placeholder={"1~1000"}
              name="price"
              type="number"
              min="1"
              max="1000"
              defaultValue={offerDetail && offerDetail.price}
            />
            <p>만원</p>
          </OfferPrice>
          {errorForm.price && <span>{errorForm.price}</span>}
        </SubmitItems>
        <SubmitItems>
          {errorForm.contents && <span>{errorForm.contents}</span>}
          <TextArea
            maxLength={200}
            name="contents"
            placeholder={"견적 제시 방법, 수리 방법 등 자세하게 써주세요."}
            defaultValue={offerDetail && offerDetail.contents}
          />
        </SubmitItems>
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
