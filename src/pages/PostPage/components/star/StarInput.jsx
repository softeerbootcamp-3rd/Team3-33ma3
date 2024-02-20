import React, { useState } from "react";
import styled from "styled-components";
import HalfStarItem from "./HalfStarItem";

const StarScoreContainer = styled.div`
  display: flex;
  gap: 5px;
  align-items: center;

  & > p {
    font-size: ${({ theme }) => theme.fontSize.regular};
    font-weight: 500;
  }
`;

const StarInputContainer = styled.div`
  display: flex;
  flex-direction: row-reverse;
  padding-bottom: 3px;

  input[type="radio"] {
    display: none;
  }

  input:checked ~ label,
  label:hover,
  label:hover ~ label {
    filter: none;
  }
`;

const StarLabel = styled.label`
  filter: brightness(0) saturate(100%) invert(79%) sepia(0%) saturate(768%)
    hue-rotate(101deg) brightness(107%) contrast(103%);
`;

const StarLeftCrop = styled.div`
  position: relative;
  width: 15px;
  height: 30px;
  overflow: hidden;
  margin-left: 3px;

  & > img {
    position: absolute;
    top: 0;
    left: 0;
    width: 30px;
    height: 30px;
  }
`;

function StarInput({ name }) {
  const [score, setScore] = useState(0);
  console.log(score);

  const stars = [5, 4.5, 4, 3.5, 3, 2.5, 2, 1.5, 1, 0.5].map((score) => (
    <>
      <HalfStarItem
        name={name}
        value={score}
        onChange={() => setScore(score)}
      />
    </>
  ));

  return (
    <StarScoreContainer>
      <StarInputContainer>{stars}</StarInputContainer>
      <p>{score}</p>
    </StarScoreContainer>
  );
}

export default StarInput;
