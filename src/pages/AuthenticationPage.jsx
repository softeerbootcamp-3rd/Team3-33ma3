import AuthForm from "../components/AuthForm";
import { redirect } from "react-router-dom";
import Header from "../components/header/header";

function AuthenticationPage() {
  return (
    <>
      <Header />
      <AuthForm />
    </>
  );
}

const tURL = "http://192.168.1.141:8080/";

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
    authData[centerName] = data.get("centerName");
    authData[latitude] = data.get("latitude");
    authData[longitude] = data.get("longitude");
  }

  const urlParameter = mode === "login" ? mode : `${type}/${mode}`;

  const response = await fetch(tURL + urlParameter, {
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
  if (mode == "login") {
    const accessToken = resData.data.accessToken;
    const refreshToken = resData.data.refreshToken;

    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
  }

  return redirect("/");
}
