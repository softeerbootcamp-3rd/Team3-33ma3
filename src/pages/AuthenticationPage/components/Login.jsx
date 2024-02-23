import { useEffect, useRef } from "react";
import { BASE_URL } from "../../../constants/url";
import styled from "styled-components";
import InputText from "../../../components/input/InputText";
import SubmitButton from "../../../components/button/SubmitButton";
import { Link, useNavigate } from "react-router-dom";

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

function Login() {
  const formRef = useRef(null);
  const navigate = useNavigate();

  function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData(formRef.current);
    const authData = Object.fromEntries(formData.entries());

    fetch(`${BASE_URL}login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(authData),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data.data);
        const resData = data.data;
        const status = data.status;
        const message = data.message;

        if (status !== "ERROR") {
          localStorage.setItem("accessToken", resData.jwtToken.accessToken);
          localStorage.setItem("refreshToken", resData.jwtToken.refreshToken);
          localStorage.setItem("memberId", resData.memberId);
          localStorage.setItem("memberType", resData.memberType);
          navigate("/");
        }
      });
  }

  return (
    <Form ref={formRef} onSubmit={handleSubmit}>
      <AuthContainer>
        <AuthHeader>{"로그인"}</AuthHeader>
        <AuthInputContainer>
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
        </AuthInputContainer>
        <SubmitContainer>
          <SubmitButton>{"로그인"}</SubmitButton>
        </SubmitContainer>
        <ChangeTypeContainer>
          <AuthLink to={"?mode=setting"}>{"회원가입"}</AuthLink>
        </ChangeTypeContainer>
      </AuthContainer>
    </Form>
  );
}

export { Login };
