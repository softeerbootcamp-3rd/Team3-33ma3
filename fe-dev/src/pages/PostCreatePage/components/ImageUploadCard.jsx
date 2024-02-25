import React from "react";
import styled from "styled-components";
import PictureArea from "../../../components/image/PictureArea";
import Close from "../../../assets/close.svg";

const ImageContainer = styled.div`
  position: relative;
  display: inline-block;
`;

const RemoveImage = styled.button`
  position: absolute;
  width: 195px;
  height: 195px;
  background: ${(props) => props.theme.colors.surface_black};
  top: 0;
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  opacity: 0;

  &:hover {
    opacity: 0.5;
  }
`;

const closeImage = {
  width: "150px",
  height: "150px",
  filter:
    "brightness(0) saturate(100%) invert(100%) sepia(0%) saturate(7500%) hue-rotate(266deg) brightness(107%) contrast(104%)",
};

function ImageUploadCard({ image, onClick }) {
  return (
    <ImageContainer>
      <PictureArea size={"small"} img={image} />
      <RemoveImage type="button" onClick={onClick}>
        <img src={Close} style={closeImage} />
      </RemoveImage>
    </ImageContainer>
  );
}

export default ImageUploadCard;
