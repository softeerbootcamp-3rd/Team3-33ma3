import AuthForm from "../../components/AuthForm";
import { redirect, useSearchParams } from "react-router-dom";
import {
  Dialog,
  Title,
  TopContainer,
  Wrapper,
} from "../../components/LocationModal";
import { useRef } from "react";
import styled from "styled-components";
import { BASE_URL } from "../../constants/url";

const MiddleContainer = styled.div``;

const AuthWrapper = styled(Wrapper)`
  display: flex;
  gap: 80px;
`;

const TitleContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 30px;
  width: 350px;
`;

const BorderContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 200px;
`;

function AuthenticationPage({ props }, ref) {
  const [searchParams] = useSearchParams();

  const isLogin = searchParams.get("mode") === "login";
  const isSignUp = !isLogin;
  const userType = searchParams.get("type");

  return (
    <BorderContainer>
      <AuthWrapper>
        <TitleContainer>
          <Title>{isLogin ? "로그인" : "회원가입"}</Title>
        </TitleContainer>
        <MiddleContainer>
          <AuthForm />
        </MiddleContainer>
      </AuthWrapper>
    </BorderContainer>
  );
}

export default AuthenticationPage;

// AuthForm의 사용자 입력을 처리하고 서버에 데이터를 전송하는 함수
export async function action({ request }) {
  const searchParams = new URL(request.url).searchParams;
  const mode = searchParams.get("mode") || "login";
  const type = searchParams.get("type");

  if (mode !== "login" && mode !== "signUp") {
    throw json({ message: "Unsupported mode." }, { status: 422 });
  }

  const data = await request.formData();
  const authData = {
    loginId: data.get("loginId"),
    password: data.get("password"),
  };

  if (type === "center") {
    authData.centerName = data.get("centerName");
    authData.latitude = data.get("latitude");
    authData.longitude = data.get("longitude");
  }

  const urlParameter = mode === "login" ? mode : `${type}/${mode}`;

  const response = await fetch(BASE_URL + urlParameter, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(authData),
  });

  if (response.status === 422 || response.status === 401) {
    return response;
  }

  if (!response.ok) {
    throw json({ message: "Could not authenticate user." }, { status: 500 });
  }

  const resData = await response.json();
  console.log(resData);
  if (mode == "login") {
    const accessToken = resData.data.accessToken;
    const refreshToken = resData.data.refreshToken;

    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
  }
  return redirect("/");
}
