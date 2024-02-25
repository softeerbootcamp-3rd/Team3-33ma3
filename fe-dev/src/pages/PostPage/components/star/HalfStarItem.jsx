import React from "react";
import styled from "styled-components";
import StarImg from "../../../../assets/star.svg";

const StarLabel = styled.label`
  filter: brightness(0) saturate(100%) invert(79%) sepia(0%) saturate(768%)
    hue-rotate(101deg) brightness(107%) contrast(103%);
`;

const StarCrop = styled.div`
  position: relative;
  width: 15px;
  height: 30px;
  overflow: hidden;
  margin-left: ${({ direction }) => direction === "left" && "3px"};
  margin-right: ${({ direction }) => direction === "right" && "3px"};

  & > img {
    position: absolute;
    top: 0;
    left: ${({ direction }) => (direction === "left" ? "0px" : "-15px")};
    width: 30px;
    height: 30px;
  }
`;

function HalfStarItem({ name, value, onChange }) {
  return (
    <>
      <input
        name={name}
        type="radio"
        id={value}
        value={value}
        onChange={onChange}
      />
      <StarLabel htmlFor={value}>
        <StarCrop direction={Number(value) % 1 === 0.5 ? "left" : "right"}>
          <img src={StarImg} />
        </StarCrop>
      </StarLabel>
    </>
  );
}

export default HalfStarItem;
