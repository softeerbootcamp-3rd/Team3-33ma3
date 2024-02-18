import AuthForm from "../../components/AuthForm";
import { redirect, useSearchParams, Navigate } from "react-router-dom";
import styled from "styled-components";
import { BASE_URL } from "../../constants/url";
import { removeAuthToken } from "../../utils/auth";

const MiddleContainer = styled.div``;

const AuthWrapper = styled.div`
  display: flex;
  gap: 80px;
  align-items: center;
  gap: 10px;
  flex-direction: column;
`;

const Title = styled.p`
  color: ${(props) => props.theme.colors.surface_black};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
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

function AuthenticationPage() {
  const [searchParams] = useSearchParams();

  const urlMode = searchParams.get("mode");

  if (urlMode === "logout") {
    removeAuthToken();
    return <Navigate to="/" />;
  }

  return (
    <BorderContainer>
      <AuthWrapper>
        <TitleContainer>
          <Title>{urlMode === "login" ? "로그인" : "회원가입"}</Title>
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

  // TODO: 백엔드에서 유효하지 않은 로그인, 회원가입 요청시 다른 에러 코드 받도록 요구
  // ex) 401 : 아이디 양식 누락, 402 : 비밀번호 양식 누락, 403 : 아이디, 비밀번호 양식 모두 누락, 410 : 서비스 센터 이름 누락, 411 : 서비스 센터 위치 설정 누락 (유효하지 않은 위치의 경우도 고려?)
  if (!response.ok) {
    if (mode === "login") {
      alert("로그인 정보가 없습니다. 다시 입력해주세요.");
      return redirect("/auth?mode=login");
    } else if (mode === "signUp") {
      alert("이미 존재하는 회원 정보입니다. 다시 입력해주세요.");
      return redirect(`/auth?mode=signUp&type=${type}`);
    }
  }

  const resData = await response.json();
  if (mode == "login") {
    const accessToken = resData.data.jwtToken.accessToken;
    const refreshToken = resData.data.jwtToken.refreshToken;
    const memberId = resData.data.memberId;
    const memberType = resData.data.memberType;

    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    localStorage.setItem("memberId", memberId);
    localStorage.setItem("memberType", memberType);
  }
  if (mode === "signUp") {
    return redirect("/auth?mode=login");
  }
  if (mode === "login") {
    return redirect("/");
  }
}
