import React from "react";
import styled from "styled-components";
import SubmitButton from "../button/SubmitButton";
import Logo from "../../assets/33MA3_logo.png";

const CommentContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
  font-size: ${(props) => props.theme.fontSize.regular};
  padding: 20px;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  box-shadow: ${(props) => props.theme.boxShadow.up};
  box-sizing: border-box;
`;

const WriterContainer = styled.div`
  display: flex;
  gap: 30px;
  align-items: center;
`;

const Writer = styled.p`
  display: flex;
  font-weight: 500;
  color: ${(props) => props.theme.colors.text_weak};
  gap: 10px;
  align-items: center;
`;

const Description = styled.p`
  width: 100%;
  font-weight: 700;
  line-height: normal;
`;

function Comment({ centerName, contents, disabled }) {
  // TODO: 문의 기능 구현

  return (
    <CommentContainer>
      <WriterContainer>
        <Writer>
          <img src={Logo} style={{ width: "28px", height: "36px" }} />
          {centerName ? centerName : "익명"}
        </Writer>
        {!disabled && <SubmitButton size={"small"} children={"문의"} />}
      </WriterContainer>
      <Description>{contents}</Description>
    </CommentContainer>
  );
}

export default Comment;
