import { useState } from "react";
import {
  Form,
  Link,
  useSearchParams,
  useActionData,
  useNavigation,
} from "react-router-dom";

function searchAddressToCoordinate(address) {
  // Promise 객체를 반환합니다.
  return new Promise((resolve, reject) => {
    naver.maps.Service.geocode(
      {
        query: address ? address : "DEFAULT",
      },
      function (status, response) {
        if (status === naver.maps.Service.Status.ERROR) {
          // 오류 상태일 경우 reject를 호출하여 Promise를 reject 상태로 만듭니다.
          return reject("Something went Wrong!");
        }

        if (response.v2.meta.totalCount === 0) {
          // 주소 검색 결과가 없을 경우
          return resolve(null);
        }

        const item = response.v2.addresses[0]; // 찾은 주소 정보
        // 성공적으로 주소 정보를 찾았을 경우 resolve를 호출하여 Promise를 resolve 상태로 만듭니다.
        resolve(item);
      }
    );
  });
}

function AuthForm() {
  const [centerInformation, setCenterInformation] = useState();
  const data = useActionData();
  const navigation = useNavigation();

  const [searchParams] = useSearchParams();
  const isLogin = searchParams.get("mode") === "login";
  const userType = searchParams.get("type");
  const isSubmitting = navigation.state === "submitting";

  function handleInputCenterName(e) {
    searchAddressToCoordinate(e.target.value)
      .then((res) => {
        if (res !== null) {
          setCenterInformation();
        }
        console.log(res); // 성공적으로 주소 정보를 받아왔을 때의 처리
      })
      .catch((error) => {
        console.error(error); // 오류 발생 시 처리
      });
  }

  return (
    <>
      <Form method="post">
        <h1>{isLogin ? "Log in" : `Create a new ${userType}`}</h1>
        {data && data.errors && (
          <ul>
            {Object.values(data.errors).map((err) => (
              <li key={err}>{err}</li>
            ))}
          </ul>
        )}
        {data && data.message && <p>{data.message}</p>}
        {!isLogin && !userType && (
          <>
            <Link to="?mode=signUp&type=client">Client</Link>
            <Link to="?mode=signUp&type=center">Center</Link>
          </>
        )}
        {(userType || isLogin) && (
          <>
            <p>
              <label htmlFor="loginId">Id</label>
              <input id="loginId" type="text" name="loginId" required />
            </p>
            <p>
              <label htmlFor="password">Password</label>
              <input id="password" type="password" name="password" required />
            </p>
            {userType === "center" && (
              <>
                <p>
                  <label htmlFor="centerName">Center name</label>
                  <input
                    id="centerName"
                    type="text"
                    name="centerName"
                    required
                  />
                </p>
                <p>
                  <label htmlFor="address">Address</label>
                  <input
                    id="address"
                    type="text"
                    name="address"
                    onChange={handleInputCenterName}
                    required
                  />
                </p>
              </>
            )}
            <div>
              <Link to={`?mode=${isLogin ? "signUp" : "login"}`}>
                {isLogin ? "Create new user" : "Login"}
              </Link>
              <button disabled={isSubmitting}>
                {isSubmitting ? "전송중..." : "저장하기"}
              </button>
            </div>
          </>
        )}
      </Form>
    </>
  );
}

export default AuthForm;
