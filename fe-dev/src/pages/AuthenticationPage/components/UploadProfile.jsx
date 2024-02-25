import React, { useRef, useState } from "react";
import styled from "styled-components";
import Camera from "../../../assets/camera.svg";
import ImageUploadCard from "../../PostCreatePage/components/ImageUploadCard";

const MAX_FILE_COUNT = 1;

const ImageUploadContainer = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: center;
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
`;

const Images = styled.div`
  white-space: nowrap;
`;

function UploadProfile({ imageFiles }) {
  const imageInputRef = useRef();
  const [previewImageList, setPreviewImageList] = useState([]);
  const [existImage, setExistImage] = useState(false);

  function onClickImageUpload() {
    imageInputRef.current.click();
  }

  function onImageUpload(e) {
    const fileList = e.target.files;
    setExistImage(true);

    if (!validateImageFilesLength(fileList.length)) {
      return;
    }

    const newPreviewImages = Array.from(fileList).map((file) =>
      URL.createObjectURL(file)
    );

    setPreviewImageList([...previewImageList, ...newPreviewImages]);
    imageFiles.current = [...imageFiles.current, ...fileList];
  }

  function validateImageFilesLength(newFileLength) {
    if (imageFiles.current.length + newFileLength > MAX_FILE_COUNT) {
      alert("사진은 최대 " + MAX_FILE_COUNT + "장까지 업로드 가능합니다.");
      return false;
    }
    return true;
  }

  function deleteImage(key) {
    setExistImage(false);
    setPreviewImageList(previewImageList.filter((img, index) => index !== key));
    imageFiles.current = imageFiles.current.filter(
      (file, index) => index !== key
    );
  }

  const images = previewImageList.map((image, index) => (
    <ImageUploadCard
      image={image}
      key={index}
      onClick={() => deleteImage(index)}
    />
  ));

  return (
    <ImageUploadContainer>
      {!existImage && (
        <>
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
        </>
      )}
      {existImage && (
        <ImageList>
          <Images>{images}</Images>
        </ImageList>
      )}
    </ImageUploadContainer>
  );
}

export default UploadProfile;
