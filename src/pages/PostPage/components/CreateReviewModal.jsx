import React, { useState } from "react";
import styled from "styled-components";
import ModalPortal from "../../../components/modal/ModalPortal";
import TextArea from "../../../components/input/TextArea";
import SubmitButton from "../../../components/button/SubmitButton";
import StarInput from "./star/StarInput";
import { BASE_URL } from "../../../constants/url";
import { useRouteLoaderData, useSearchParams } from "react-router-dom";

const SubmitContent = styled.form`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 20px;
  align-items: center;
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

const StarInputContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;

  & > p {
    font-size: ${({ theme }) => theme.fontSize.medium};
    font-weight: 500;
  }
`;

function CreateReviewModal({ handleClose }) {
  const [query, setQuery] = useSearchParams();
  const [isLoading, setIsLoading] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const { accessToken } = useRouteLoaderData("root");

  function handleSubmitForm(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const review = Object.fromEntries(formData.entries());

    if (!validateForm(review)) {
      return;
    }
    console.log(review);
    setIsLoading(true);
    fetch(`${BASE_URL}review/${query.get("post_id")}`, {
      method: "POST",
      headers: {
        Authorization: accessToken,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(review),
    })
      .then((res) => {
        if (res.ok) {
          return res.json();
        } else if (res.status === 400) {
          throw new Error("이미 리뷰를 작성했습니다.");
        }
      })
      .then((json) => console.log(json))
      .catch((error) => alert(error.message))
      .finally(() => {
        setIsLoading(false);
        handleClose();
      });
  }

  function validateForm(review) {
    const errors = {};

    if (!review.score) {
      errors.score = "별점을 입력해주세요.";
    }

    if (review.contents.length <= 0) {
      errors.contents = "내용을 입력해주세요.";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  }

  return (
    <ModalPortal
      title={"서비스 센터에 만족하셨나요?"}
      width="750px"
      handleClose={handleClose}
    >
      <SubmitContent onSubmit={handleSubmitForm}>
        <SubmitItems>
          <StarInputContainer>
            <p>별점</p>
            <StarInput name={"score"} />
          </StarInputContainer>
          {formErrors.score && <span>{formErrors.score}</span>}
        </SubmitItems>
        <SubmitItems>
          {formErrors.contents && <span>{formErrors.contents}</span>}
          <TextArea maxLength={500} name={"contents"} />
        </SubmitItems>
        <SubmitButton disabled={isLoading}>저장하기</SubmitButton>
      </SubmitContent>
    </ModalPortal>
  );
}

export default CreateReviewModal;
