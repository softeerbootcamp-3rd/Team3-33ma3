import React, { useRef, useState } from "react";
import styled from "styled-components";
import Camera from "../../assets/camera.svg";
import PictureArea from "./PictureArea";

const ImageUploadContainer = styled.div`
  display: flex;
  width: 100%;
`;

const ImageUploadButton = styled.button`
  border-radius: ${(props) => props.theme.radiuses.radius_s};
  background: ${(props) => props.theme.colors.surface_white_weak};
  width: 195px;
  height: 195px;

  &:hover {
    background: ${(props) => props.theme.colors.surface_weak};
  }
`;

const ImageList = styled.div`
  flex: 1;
  width: 0%;
`;

const Images = styled.div`
  overflow-x: auto;
  white-space: nowrap;
`;

function ImageUpload() {
  const imageInputRef = useRef();
  // TODO: 부모 컴포넌트에서 state 받아옴
  const [postImgList, setPostImgList] = useState([]);

  // 버튼 클릭시 file input태그에 클릭이벤트 발생
  function onCickImageUpload() {
    imageInputRef.current.click();
  }

  // image 업로드
  function onImageUpload(e) {
    const fileList = e.target.files;
    const url = URL.createObjectURL(fileList[0]);

    setPostImgList([...postImgList, url]);
  }

  const images = postImgList.map((image) => (
    <PictureArea size={"small"} img={image} />
  ));

  return (
    <ImageUploadContainer>
      <ImageUploadButton onClick={onCickImageUpload}>
        <img
          src={Camera}
          style={{ width: "48px", height: "48px", margin: "auto" }}
        />
      </ImageUploadButton>
      <input
        type="file"
        accept="image/jpg, image/jpeg, image/png"
        style={{ display: "none" }}
        ref={imageInputRef}
        onChange={onImageUpload}
      />
      <ImageList>
        <Images>{images}</Images>
      </ImageList>
    </ImageUploadContainer>
  );
}

export default ImageUpload;
