import { Navigate, useSearchParams } from "react-router-dom";
import Page from "../../components/post/Page";
import { Login } from "./components/Login";
import { SignUp } from "./components/SignUp";
import { removeAuthToken } from "../../utils/auth";
import { SettingType } from "./components/SettingType";
import UploadProfile from "./components/UploadProfile";
import { useRef } from "react";

function AuthenticationPage() {
  const [searchParams] = useSearchParams();
  const mode = searchParams.get("mode");
  const isLogin = mode === "login";
  const isSignUp = mode === "signUp";
  const isLogOut = mode === "logout";
  const isSettingType = mode === "setting";

  if (isLogOut) {
    removeAuthToken();
    return <Navigate to="/" />;
  }

  return (
    <Page>
      {isLogin && <Login />}
      {isSettingType && <SettingType />}
      {isSignUp && <SignUp />}
    </Page>
  );
}

export { AuthenticationPage };
