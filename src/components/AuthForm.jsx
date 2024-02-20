import { useEffect, useState, useRef } from "react";
import {
  Form,
  Link,
  useSearchParams,
  useActionData,
  useNavigation,
} from "react-router-dom";
import InputText from "./input/InputText";
import styled from "styled-components";
import SubmitButton from "./button/SubmitButton";
import ClientImage from "../assets/client_mode.svg";
import CenterImage from "../assets/center_mode.svg";
import { generateKeyBasedOnCurrentTime } from "./LocationModal";
import { searchAddressToCoordinate } from "../utils/locationUtils";

const AuthInputContainer = styled.div`
  display: flex;
  gap: 15px;
  flex-direction: column;
  align-items: center;
`;

const AuthBottomContainer = styled.div`
  display: flex;
  gap: 15px;
  flex-direction: column;
  align-items: center;
`;

const AuthContainer = styled.div`
  display: flex;
  gap: 40px;
  flex-direction: column;
  width: 350px;
`;

const AuthLink = styled(Link)`
  text-decoration: none;
  color: ${(props) => props.theme.colors.text_weak};
`;

const ModeContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 20px;
  width: 100%;
  padding: 30px;
`;

const Mode = styled.div`
  display: flex;
  width: 200px;
  height: 200px;
  border-radius: 14px;
  background: #f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 20px;
`;

const Image = styled.img`
  width: 100px;
  height: 80px;
`;

const FormContainer = styled.div``;

function AuthForm() {
  const [autoCompleteKey, setAutoCompleteKey] = useState(
    generateKeyBasedOnCurrentTime()
  );
  const [centerInformation, setCenterInformation] = useState();
  const [autoCompleteAddress, setAutoCompleteAddress] = useState("");
  const [checkAddress, setCheckAddress] = useState(false);
  const [coords, setCoords] = useState({ latitude: 0, longitude: 0 });
  const [inputId, setInputId] = useState("");
  const [inputPassword, setInputPassword] = useState("");
  const [searchParams] = useSearchParams();

  const data = useActionData();
  const navigation = useNavigation();

  const isLogin = searchParams.get("mode") === "login";
  const userType = searchParams.get("type");
  const isSubmitting = navigation.state === "submitting";

  function handleInputCenterName(e) {
    searchAddressToCoordinate(e.target.value)
      .then((res) => {
        if (res !== null) {
          setCenterInformation(res.address.roadAddress);
          setCheckAddress(true);
          setCoords((prev) => ({
            ...prev,
            latitude: res.point.y,
            longitude: res.point.x,
          }));
        }
        console.log(res); // 성공적으로 주소 정보를 받아왔을 때의 처리
      })
      .catch((error) => {
        console.error(error); // 오류 발생 시 처리
      });
  }

  function handleAutoComplete(e) {
    setAutoCompleteAddress(e.target.innerHTML);
    setCheckAddress(false);
    setAutoCompleteKey(generateKeyBasedOnCurrentTime());
  }

  function handleInputId(e) {
    setInputId(e.target.value);
  }

  function handleInputPassword(e) {
    setInputPassword(e.target.value);
  }

  useEffect(() => {
    setInputId("");
    setInputPassword("");
  }, [isLogin]);

  return (
    <FormContainer>
      <Form method="post">
        {data && data.errors && (
          <ul>
            {Object.values(data.errors).map((err) => (
              <li key={err}>{err}</li>
            ))}
          </ul>
        )}
        {data && data.message && <p>{data.message}</p>}
        {!isLogin && !userType && (
          <ModeContainer>
            <AuthLink to="?mode=signUp&type=client">
              <Mode>
                <Image src={ClientImage} />
                사용자 회원가입
              </Mode>
            </AuthLink>
            <AuthLink to="?mode=signUp&type=center">
              <Mode>
                <Image src={CenterImage} />
                센터 회원가입
              </Mode>
            </AuthLink>
          </ModeContainer>
        )}
        {(isLogin || userType) && (
          <AuthContainer>
            <AuthInputContainer>
              <InputText
                id="loginId"
                type="text"
                name="loginId"
                placeholder="아이디"
                size="small"
                value={inputId}
                onChange={handleInputId}
                required
              />

              <InputText
                id="password"
                type="password"
                name="password"
                placeholder="비밀번호"
                size="small"
                value={inputPassword}
                onChange={handleInputPassword}
                required
              />

              {userType === "center" && (
                <>
                  <input
                    type="hidden"
                    name="latitude"
                    value={coords.latitude}
                  />
                  <input
                    type="hidden"
                    name="longitude"
                    value={coords.longitude}
                  />
                  <InputText
                    id="centerName"
                    type="text"
                    name="centerName"
                    placeholder="센터이름"
                    size="small"
                    required
                  />
                  <InputText
                    id="address"
                    type="text"
                    name="address"
                    onChange={handleInputCenterName}
                    placeholder="위치"
                    size="small"
                    key={autoCompleteKey}
                    defaultValue={autoCompleteAddress}
                    required
                  />
                  {checkAddress && (
                    <div onClick={handleAutoComplete}>{centerInformation}</div>
                  )}
                </>
              )}
            </AuthInputContainer>
            <AuthBottomContainer>
              <AuthLink to={`?mode=${isLogin ? "signUp" : "login"}`}>
                {isLogin ? "회원가입" : "로그인"}
              </AuthLink>
              <div>
                <SubmitButton disabled={isSubmitting}>
                  {isSubmitting ? "전송중..." : isLogin ? "로그인" : "가입"}
                </SubmitButton>
              </div>
            </AuthBottomContainer>
          </AuthContainer>
        )}
      </Form>
    </FormContainer>
  );
}

export default AuthForm;
