import { Navigate, useSearchParams } from "react-router-dom";
import Page from "../../components/post/Page";
import { Login } from "./components/Login";
import { SignUp } from "./components/SignUp";
import { removeAuthToken } from "../../utils/auth";

function AuthenticationPage() {
  const [searchParams] = useSearchParams();
  const mode = searchParams.get("mode");
  const isLogin = mode === "login";
  const isSignUp = mode === "signUp";
  const isLogOut = mode === "logout";

  if (isLogOut) {
    removeAuthToken();
    return <Navigate to="/" />;
  }

  return (
    <Page>
      {isLogin && <Login />}
      {isSignUp && <SignUp />}
    </Page>
  );
}

export { AuthenticationPage };
