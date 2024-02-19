import React from "react";
import styled from "styled-components";
import StarImg from "../../assets/star.svg";

const Img = styled.img`
  width: 20px;
  height: 20px;
`;

const StarContainer = styled.div`
  display: flex;
  gap: 5px;
  align-items: center;
`;

function StarRating({ score }) {
  return (
    <StarContainer>
      <Img src={StarImg} />
      <p>{score} / 5</p>
    </StarContainer>
  );
}

export default StarRating;
