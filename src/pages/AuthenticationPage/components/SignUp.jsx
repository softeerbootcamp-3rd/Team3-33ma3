import { useEffect, useRef } from "react";
import { BASE_URL } from "../../../constants/url";
import styled from "styled-components";
import InputText from "../../../components/input/InputText";
import SubmitButton from "../../../components/button/SubmitButton";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { CenterAuthForm } from "./CenterAuthForm";
import UploadProfile from "./UploadProfile";
import { getMemberId } from "../../../utils/auth";

const Form = styled.form``;

const AuthContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 80vh;
  gap: 30px;
  font-weight: 500;
`;

const AuthHeader = styled.h1`
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

const AuthInputContainer = styled.div`
  display: flex;
  flex-direction: column;
  gap: 30px;
  width: 310px;
`;

const SubmitContainer = styled.div``;

const ChangeTypeContainer = styled.div``;

const AuthLink = styled(Link)`
  text-decoration: none;
  color: ${(props) => props.theme.colors.text_weak};
`;

function SignUp() {
  const formRef = useRef(null);
  const imageFiles = useRef([]);
  const navigate = useNavigate();
  const [searchParam] = useSearchParams();
  const type = searchParam.get("type");
  const memberId = getMemberId();

  function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(formRef.current);
    const authData = Object.fromEntries(formData.entries());
    console.log(authData);

    fetch(`${BASE_URL}${type}/signUp`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(authData),
    })
      .then((res) => res.json())
      .then((data) => {
        const status = data.status;
        const message = data.message;
        localStorage.setItem("memberId", data.data);
        if (status !== "ERROR") {
          console.log("signUp success");
        }
      });

    const fd = new FormData();
    imageFiles.current.forEach((file) => {
      fd.append("profile", file);
    });

    fetch(`${BASE_URL}profile/${memberId}`, {
      method: "POST",
      headers: {
        Accept: "application/json",
      },
      body: fd,
    })
      .then((res) => res.json())
      .then((data) => {
        const status = data.status;
        const message = data.message;
        if (status === "ERROR") {
          alert("프로필을 다시 등록해주세요.");
          return navigate(`?mode=signUp&type=${type}`);
        }
        console.log("signUp success");
        return navigate("?mode=login");
      });
  }

  useEffect(() => {}, []);
  return (
    <Form ref={formRef} onSubmit={handleSubmit}>
      <AuthContainer>
        <AuthHeader>{"회원가입"}</AuthHeader>
        <AuthInputContainer>
          <UploadProfile imageFiles={imageFiles} />
          <InputText
            id="loginId"
            type="text"
            name="loginId"
            placeholder="아이디"
            size="small"
            required
          />
          <InputText
            id="password"
            type="password"
            name="password"
            placeholder="비밀번호"
            size="small"
            required
          />
          {type === "center" && <CenterAuthForm />}
        </AuthInputContainer>
        <SubmitContainer>
          <SubmitButton>{"회원가입"}</SubmitButton>
        </SubmitContainer>
        <ChangeTypeContainer>
          <AuthLink to="?mode=login">{"로그인"}</AuthLink>
        </ChangeTypeContainer>
      </AuthContainer>
    </Form>
  );
}

export { SignUp };
