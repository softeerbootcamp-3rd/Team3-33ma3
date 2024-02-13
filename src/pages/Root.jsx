import { useEffect } from "react";
import { Link, useLoaderData } from "react-router-dom";
import Header from "../components/header/header";

function RootLayout() {
  const token = useLoaderData();

  useEffect(() => {
    if (!token) {
      return;
    }
  }, [token]);

  return (
    <>
      <Header />
      {!token && (
        <p>
          <Link to="/auth?mode=login">Login</Link>
          <Link to="/auth?mode=signUp">Sign Up</Link>
        </p>
      )}
    </>
  );
}

export default RootLayout;
