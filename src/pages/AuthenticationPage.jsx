import AuthForm from "../components/AuthForm";
import { redirect, useSearchParams } from "react-router-dom";
import Header from "../components/header/header";
import { createPortal } from "react-dom";
import {
  CloseButton,
  Dialog,
  Title,
  TopContainer,
  Wrapper,
} from "../components/LocationModal";
import { useEffect, useRef } from "react";
import styled from "styled-components";
import { BASE_URL } from "../constants/url";

const AuthDialog = styled(Dialog)`
  width: 400px;
  text-align: center;
`;

const MiddleContainer = styled.div``;

const AuthWrapper = styled(Wrapper)`
  display: flex;
  gap: 80px;
  width: 400px;
`;

function AuthenticationPage() {
  const [searchParams] = useSearchParams();

  const isLogin = searchParams.get("mode") === "login";
  const isSignUp = !isLogin;
  const userType = searchParams.get("type");
  const dialog = useRef();

  function handleCloseModal() {
    dialog.current.close();
  }

  useEffect(() => {
    if (isLogin || isSignUp) {
      dialog.current.showModal();
    }
  }, []);

  return createPortal(
    <>
      <Header />
      <AuthDialog ref={dialog} className={"auth-modal"}>
        <AuthWrapper>
          <TopContainer>
            <Title>{isLogin ? "로그인" : "회원가입"}</Title>
            <CloseButton onClick={handleCloseModal}>X</CloseButton>
          </TopContainer>
          <MiddleContainer>
            <AuthForm />
          </MiddleContainer>
        </AuthWrapper>
      </AuthDialog>
    </>,
    document.getElementById("auth-modal")
  );
}

export default AuthenticationPage;

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
