import React, { useRef, useState } from "react";
import styled from "styled-components";
import Camera from "../../../assets/camera.svg";
import ImageUploadCard from "./ImageUploadCard";
import { MAX_FILE_COUNT } from "../../../constants/options";

const ImageUploadContainer = styled.div`
  display: flex;
  width: 100%;
  height: 195px;
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

const ImageUploadCardContainer = styled.div`
  margin-left: 40px;
  position: relative;
  display: inline-block;
`;

function ImageUpload({ imageFiles }) {
  const imageInputRef = useRef();
  // TODO: 부모 컴포넌트에서 state 받아옴
  const [previewImageList, setPreviewImageList] = useState([]);

  // 버튼 클릭시 file input태그에 클릭이벤트 발생
  function onClickImageUpload() {
    imageInputRef.current.click();
  }

  // image 업로드
  function onImageUpload(e) {
    const fileList = e.target.files;

    if (!validateImageFilesLength(fileList.length)) {
      return;
    }

    const newPreviewImages = Array.from(fileList).map((file) =>
      URL.createObjectURL(file)
    );

    setPreviewImageList([...previewImageList, ...newPreviewImages]);
    imageFiles.current = [...imageFiles.current, ...fileList];
  }

  // 이미지 업로드 5장 이상일 시 업로드 불가능
  function validateImageFilesLength(newFileLength) {
    if (imageFiles.current.length + newFileLength > MAX_FILE_COUNT) {
      alert("사진은 최대 " + MAX_FILE_COUNT + "장까지 업로드 가능합니다.");
      return false;
    }
    return true;
  }

  // 이미지 삭제
  function deleteImage(key) {
    setPreviewImageList(previewImageList.filter((img, index) => index !== key));
    imageFiles.current = imageFiles.current.filter(
      (file, index) => index !== key
    );
  }

  const images = previewImageList.map((image, index) => (
    <ImageUploadCardContainer key={index}>
      <ImageUploadCard image={image} onClick={() => deleteImage(index)} />
    </ImageUploadCardContainer>
  ));

  return (
    <ImageUploadContainer>
      <ImageUploadButton onClick={onClickImageUpload} type="button">
        <img
          src={Camera}
          style={{ width: "48px", height: "48px", margin: "auto" }}
        />
      </ImageUploadButton>
      <input
        type="file"
        multiple
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
