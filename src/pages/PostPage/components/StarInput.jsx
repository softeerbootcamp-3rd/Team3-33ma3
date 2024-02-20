import React, { useState } from "react";
import styled from "styled-components";
import StarImg from "../../../assets/star.svg";

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

const StarRightCrop = styled.div`
  position: relative;
  width: 15px;
  height: 30px;
  overflow: hidden;
  margin-right: 3px;

  & > img {
    position: absolute;
    top: 0;
    left: -15px;
    width: 30px;
    height: 30px;
  }
`;

function StarInput({ name }) {
  const [score, setScore] = useState(0);

  return (
    <StarScoreContainer>
      <StarInputContainer>
        <input
          name={name}
          type="radio"
          id="rate5"
          src={StarImg}
          value={5}
          onChange={() => setScore(5)}
        />
        <StarLabel htmlFor="rate5">
          <StarRightCrop>
            <img src={StarImg} />
          </StarRightCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate4.5"
          src={StarImg}
          value={4.5}
          onChange={() => setScore(4.5)}
        />
        <StarLabel htmlFor="rate4.5">
          <StarLeftCrop>
            <img src={StarImg} />
          </StarLeftCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate4"
          src={StarImg}
          value={4}
          onChange={() => setScore(4)}
        />
        <StarLabel htmlFor="rate4">
          <StarRightCrop>
            <img src={StarImg} />
          </StarRightCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate3.5"
          src={StarImg}
          value={3.5}
          onChange={() => setScore(3.5)}
        />
        <StarLabel htmlFor="rate3.5">
          <StarLeftCrop>
            <img src={StarImg} />
          </StarLeftCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate3"
          src={StarImg}
          value={3}
          onChange={() => setScore(3)}
        />
        <StarLabel htmlFor="rate3">
          <StarRightCrop>
            <img src={StarImg} />
          </StarRightCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate2.5"
          src={StarImg}
          value={2.5}
          onChange={() => setScore(2.5)}
        />
        <StarLabel htmlFor="rate2.5">
          <StarLeftCrop>
            <img src={StarImg} />
          </StarLeftCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate2"
          src={StarImg}
          value={2}
          onChange={() => setScore(2)}
        />
        <StarLabel htmlFor="rate2">
          <StarRightCrop>
            <img src={StarImg} />
          </StarRightCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate1.5"
          src={StarImg}
          value={1.5}
          onChange={() => setScore(1.5)}
        />
        <StarLabel htmlFor="rate1.5">
          <StarLeftCrop>
            <img src={StarImg} />
          </StarLeftCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate1"
          src={StarImg}
          value={1}
          onChange={() => setScore(1)}
        />
        <StarLabel htmlFor="rate1">
          <StarRightCrop>
            <img src={StarImg} />
          </StarRightCrop>
        </StarLabel>

        <input
          name={name}
          type="radio"
          id="rate0.5"
          src={StarImg}
          value={0.5}
          onChange={() => setScore(0.5)}
        />
        <StarLabel htmlFor="rate0.5">
          <StarLeftCrop>
            <img src={StarImg} />
          </StarLeftCrop>
        </StarLabel>
      </StarInputContainer>
      <p>{score}</p>
    </StarScoreContainer>
  );
}

export default StarInput;
