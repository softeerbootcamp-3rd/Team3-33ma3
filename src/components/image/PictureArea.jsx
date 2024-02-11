import React from "react";
import styled from "styled-components";

// size 별 크기 지정
const sizeList = {
  small: {
    width: "195px",
    height: "195px",
  },
  medium: {
    width: "100%",
    height: "240px",
  },
  large: {
    width: "480px",
    height: "480px",
  },
};

const PictureContainer = styled.div`
  border-radius: ${(props) =>
    props.size === "small"
      ? props.theme.radiuses.radius_s
      : props.theme.radiuses.radius_m};
  width: ${({ size }) => sizeList[size].width};
  height: ${({ size }) => sizeList[size].height};
  overflow: hidden;
  margin-left: ${(props) => (props.size === "small" ? "40px" : "0px")};
  vertical-align: top;
`;

const Image = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
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
