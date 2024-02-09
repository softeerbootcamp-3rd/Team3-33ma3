import React, { useRef, useState } from "react";
import styled from "styled-components";
import HeadTitle from "../../components/title/HeadTitle";
import ImageUpload from "./components/ImageUpload";
import InputText from "../../components/input/InputText";
import TextArea from "../../components/input/TextArea";
import SubmitButton from "../../components/button/SubmitButton";
import ChipButton from "../../components/button/ChipButton";
import InputRange from "./components/InputRange";
import Option from "./components/Option";
import OptionItem from "./components/OptionItem";
import SelectCategory from "./components/SelectCategory";
import {
  REPAIR_SERVICE_OPTIONS,
  TUNEUP_SERVICE_OPTIONS,
} from "../../constants/options";
import { CONTENT_MAX_WIDTH } from "../../constants/layouts";

const Page = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Form = styled.form`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 55px;
  padding: 30px;
  box-sizing: border-box;
  align-items: center;
`;

const Content = styled.div`
  max-width: ${CONTENT_MAX_WIDTH};
  display: flex;
  flex-direction: column;
  gap: 45px;
`;

const ServiceList = styled.div`
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
`;

const Grid = styled.div`
  width: 100%;
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-column-gap: 70px;
  grid-row-gap: 25px;
`;

function PostCreatePage() {
  const repairService = useRef([]);
  const tuneUpService = useRef([]);
  const imageFiles = useRef([]);
  const [formErrors, setFormErrors] = useState({});
  const [loading, setLoading] = useState(false);

  // Form 제출 버튼 클릭 시 실행
  function onSubmit(e) {
    e.preventDefault();
    setLoading(true);

    // TODO: API 전송
    // 동작 되는지 확인용으로 작성 -> 추후 변경
    const fd = new FormData();
    imageFiles.current.forEach((file) => {
      fd.append("images", file);
    });

    const formData = new FormData(e.target);
    const newPost = {
      ...Object.fromEntries(formData.entries()),
      repairService: repairService.current.join(","),
      tuneUpService: tuneUpService.current.join(","),
      centers: [1, 2, 3],
      memberId: 1,
    };

    // TODO: 에러처리 필요
    if (!validateOptions(newPost)) {
      console.log("게시물 작성 형식 안맞음");
      setLoading(false);
      return;
    }

    fd.append(
      "request",
      new Blob([JSON.stringify(newPost)], { type: "application/json" })
    );

    fetch("http://192.168.1.141:8080/post/create", {
      method: "POST",
      headers: { Accept: "application/json" },
      body: fd,
    })
      .then((response) => {
        if (response.status === 200) {
          console.log("success!");
          return response.json();
        } else {
          console.log("error");
        }
      })
      .then((json) => {
        console.log(json);
      })
      .finally(() => {
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

    setFormErrors(errors);
    if (Object.keys(errors).length !== 0) {
      return false;
    }
  }

  function toggle(state, value) {
    if (state.current.includes(value)) {
      state.current = state.current.filter((item) => item !== value);
    } else {
      state.current = [...state.current, value];
    }
  }

  const Repair = (
    <OptionItem title={"수리 서비스"} layout={"start"}>
      <ServiceList>
        {REPAIR_SERVICE_OPTIONS.map((item, index) => (
          <ChipButton
            type={item}
            key={index}
            onClick={() => toggle(repairService, item)}
          />
        ))}
      </ServiceList>
      {formErrors.service && <span>{formErrors.service}</span>}
    </OptionItem>
  );

  const TuneUp = (
    <OptionItem title={"정비 서비스"} layout={"start"}>
      <ServiceList>
        {TUNEUP_SERVICE_OPTIONS.map((item, index) => (
          <ChipButton
            type={item}
            key={index}
            onClick={() => toggle(tuneUpService, item)}
          />
        ))}
      </ServiceList>
      {formErrors.service && <span>{formErrors.service}</span>}
    </OptionItem>
  );

  return (
    <Page>
      <HeadTitle title={"게시물 작성"} />
      <Form onSubmit={onSubmit}>
        <Content>
          <ImageUpload imageFiles={imageFiles} />
          <Option title={"차량 정보"}>
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
                <button type="button">지역과 반경을 선택해주세요</button>
              </OptionItem>
              {Repair}
              {TuneUp}
            </Grid>
          </Option>
          <Option title={"세부 정보"}>
            <TextArea maxLength={"500"} name={"contents"} />
          </Option>
        </Content>
        <SubmitButton type="submit" disabled={loading}>
          저장하기
        </SubmitButton>
      </Form>
    </Page>
  );
}

export default PostCreatePage;
