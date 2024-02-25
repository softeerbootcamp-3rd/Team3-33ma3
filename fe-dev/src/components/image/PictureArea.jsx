import React from "react";
import styled from "styled-components";

// size 별 크기 지정
const sizeList = {
  small: {
    width: "195px",
    height: "195px",
  },
  medium: {
    width: "340px",
    height: "240px",
  },
  large: {
    width: "480px",
    height: "480px",
  },
};

const PictureContainer = styled.div`
  border-radius: ${(props) =>
    props.size === "small" && props.theme.radiuses.radius_s};
  width: ${({ size }) => sizeList[size].width};
  height: ${({ size }) => sizeList[size].height};
  overflow: hidden;
  vertical-align: top;
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
  user-drag: none;
`;

function PictureArea({ size, img }) {
  return (
    <>
      <PictureContainer size={size}>
        <Image src={img} />
      </PictureContainer>
    </>
  );
}

export default PictureArea;
