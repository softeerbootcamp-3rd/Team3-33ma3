import {
  Form,
  Link,
  useSearchParams,
  useActionData,
  useNavigation,
} from "react-router-dom";

function AuthForm() {
  const data = useActionData();
  const navigation = useNavigation();

  const [searchParams] = useSearchParams();
  const isLogin = searchParams.get("mode") === "login";
  const userType = searchParams.get("type");
  const isSubmitting = navigation.state === "submitting";

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
              <label htmlFor="email">Email</label>
              <input id="email" type="email" name="email" required />
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
                  <input id="address" type="text" name="address" required />
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
