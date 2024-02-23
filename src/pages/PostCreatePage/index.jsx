import React, { useRef, useState } from "react";
import styled from "styled-components";
import Page from "../../components/post/Page";
import ImageUpload from "./components/ImageUpload";
import InputText from "../../components/input/InputText";
import TextArea from "../../components/input/TextArea";
import SubmitButton from "../../components/button/SubmitButton";
import InputRange from "./components/InputRange";
import OptionType from "../../components/post/OptionType";
import OptionItem from "../../components/post/OptionItem";
import SelectCategory from "./components/SelectCategory";
import ServiceList from "./components/ServiceList";
import {
  REPAIR_SERVICE_OPTIONS,
  TUNEUP_SERVICE_OPTIONS,
} from "../../constants/options";

import { BASE_URL } from "../../constants/url";
import { useNavigate, useRouteLoaderData } from "react-router-dom";
import MapModal from "./components/MapModal";

const Form = styled.form`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 55px;
  align-items: center;
`;

const PostRegister = styled.div`
  display: flex;
  flex-direction: column;
  gap: 45px;
  width: 100%;
`;

const Grid = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-column-gap: 70px;
  grid-row-gap: 25px;
`;

const Button = styled.button`
  display: flex;
  align-items: start;
  font-size: ${(props) => props.theme.fontSize.regular};
  font-weight: 500;
  flex-direction: column;
  gap: 10px;
`;

function PostCreatePage() {
  const repairService = useRef([]);
  const tuneUpService = useRef([]);
  const imageFiles = useRef([]);
  const modal = useRef();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [address, setAddress] = useState("");
  const [centerList, setCenterList] = useState([]);
  const [radius, setRadius] = useState(0);
  const { accessToken, memberId } = useRouteLoaderData("root");
  const navigate = useNavigate();

  // Form 제출 버튼 클릭 시 실행
  function onSubmit(e) {
    e.preventDefault();
    setLoading(true);

    // TODO: API 전송
    // 동작 되는지 확인용으로 작성 -> 추후 변경
    const fd = new FormData();
    if (imageFiles.current.length > 0) {
      imageFiles.current.forEach((file) => {
        fd.append("images", file);
      });
    }

    const formData = new FormData(e.target);
    const newPost = {
      ...Object.fromEntries(formData.entries()),
      repairService: repairService.current.join(","),
      tuneUpService: tuneUpService.current.join(","),
      centers: centerList.map((item) => item.centerId),
      location: address,
    };

    console.log(newPost);
    console.log(fd.get("images"));

    // TODO: 에러처리 필요
    if (!validateOptions(newPost)) {
      alert("게시물 작성 형식이 올바르지 않습니다.");
      setLoading(false);
      return;
    }

    fd.append(
      "request",
      new Blob([JSON.stringify(newPost)], { type: "application/json" })
    );

    fetch(BASE_URL + "post/create", {
      method: "POST",
      headers: {
        Authorization: accessToken,
        Accept: "application/json",
      },
      body: fd,
    })
      .then((response) => {
        if (response.ok) {
          console.log("success!");
        } else {
          console.log("error");
          alert("게시물 작성에 실패했습니다.");
        }
        return response.json();
      })
      .then((json) => {
        console.log(json);
      })
      .finally(() => {
        navigate("/");
        setLoading(false);
      });
  }

  // 올바른 게시물 작성 형태인지 확인
  function validateOptions(post) {
    const errors = {};
    // 수리, 정비 중 하나도 고르지 않은 경우
    if (repairService.current.length < 1 && tuneUpService.current.length < 1) {
      errors.service =
        "수리 서비스 혹은 정비 서비스 중 하나는 반드시 선택해야 합니다.";
    }

    // 모델명에 아무것도 안썼을 경우
    if (post.modelName.length < 1) {
      errors.modelName = "모델명을 입력해주세요.";
    }

    // 지역을 설정하지 않았을 경우 경우
    if (post.location.length < 1) {
      errors.location = "현재 위치를 설정해주세요.";
    }

    // 반경을 설정하지 않았을 경우 경우
    if (radius <= 0) {
      errors.radius = "반경을 1이상 10 이하로 설정해주세요.";
    }

    // 반경을 설정하지 않았을 경우 경우
    if (errors.location && errors.radius) {
      errors.locationRadius = "위치와 반경을 설정해주세요.";
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  }

  function toggle(state, value) {
    if (state.current.includes(value)) {
      state.current = state.current.filter((item) => item !== value);
    } else {
      state.current = [...state.current, value];
    }
  }

  function handleSaveAddress(address) {
    setAddress(address);
  }

  function handleSaveCenterList(centerList) {
    setCenterList(centerList);
  }

  function handleSaveRadius(radius) {
    setRadius(radius);
  }

  return (
    <>
      {isModalOpen && (
        <MapModal
          onSave={handleSaveAddress}
          onSaveList={handleSaveCenterList}
          onSaveRadius={handleSaveRadius}
          handleClose={() => setIsModalOpen(false)}
        />
      )}
      <Page title={"게시글 작성"}>
        <Form onSubmit={onSubmit}>
          <PostRegister>
            <ImageUpload imageFiles={imageFiles} />
            <OptionType title={"차량 정보"}>
              <Grid>
                <OptionItem title={"모델"}>
                  <InputText size={"small"} name={"modelName"} />
                  {formErrors.modelName && <span>{formErrors.modelName}</span>}
                </OptionItem>
                <OptionItem title={"차종"}>
                  <SelectCategory name={"carType"} />
                </OptionItem>
                <OptionItem title={"마감 기한"}>
                  <InputRange name={"deadline"} />
                </OptionItem>
                <OptionItem title={"지역"}>
                  <Button type="button" onClick={() => setIsModalOpen(true)}>
                    <p>{address ? address : "지역과 반경을 선택해주세요."}</p>
                    <p>{radius !== 0 && `반경 ${radius / 1000}km 이내`}</p>
                  </Button>
                  {formErrors.locationRadius && (
                    <span>{formErrors.locationRadius}</span>
                  )}
                  {!formErrors.locationRadius && formErrors.location && (
                    <span>{formErrors.location}</span>
                  )}
                  {!formErrors.locationRadius && formErrors.radius && (
                    <span>{formErrors.radius}</span>
                  )}
                </OptionItem>
                <OptionItem title={"수리 서비스"}>
                  <ServiceList
                    optionList={REPAIR_SERVICE_OPTIONS}
                    serviceList={repairService}
                    onClick={toggle}
                  />
                  {formErrors.service && <span>{formErrors.service}</span>}
                </OptionItem>
                <OptionItem title={"정비 서비스"}>
                  <ServiceList
                    optionList={TUNEUP_SERVICE_OPTIONS}
                    serviceList={tuneUpService}
                    onClick={toggle}
                  />
                  {formErrors.service && <span>{formErrors.service}</span>}
                </OptionItem>
              </Grid>
            </OptionType>
            <OptionType title={"세부 정보"}>
              <TextArea maxLength={"500"} name={"contents"} />
            </OptionType>
          </PostRegister>
          <SubmitButton type="submit" disabled={loading}>
            저장하기
          </SubmitButton>
        </Form>
      </Page>
    </>
  );
}

export default PostCreatePage;
