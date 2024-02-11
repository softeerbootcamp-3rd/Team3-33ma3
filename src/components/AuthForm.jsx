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
  const isSubmitting = navigation.state === "submitting";

  return (
    <>
      <Form method="post">
        <p>
          <label htmlFor="email">Email</label>
          <input id="email" type="email" name="email" required />
        </p>
        <p>
          <label htmlFor="password">Password</label>
          <input id="password" type="password" name="password" required />
        </p>
        <button disabled={isSubmitting}>
          {isSubmitting ? "전송중..." : "저장하기"}
        </button>
      </Form>
    </>
  );
}
