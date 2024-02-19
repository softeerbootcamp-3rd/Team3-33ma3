import React from "react";
import styled from "styled-components";
import ModalPortal from "../../../components/modal/ModalPortal";
import TextArea from "../../../components/input/TextArea";
import SubmitButton from "../../../components/button/SubmitButton";

const Content = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 20px;
  align-items: center;
`;

function CreateReviewModal({ handleClose }) {
  return (
    <ModalPortal title={"후기 작성"} width="750px" handleClose={handleClose}>
      <Content>
        <TextArea maxLength={500} />
        <SubmitButton>저장하기</SubmitButton>
      </Content>
    </ModalPortal>
  );
}

export default CreateReviewModal;
