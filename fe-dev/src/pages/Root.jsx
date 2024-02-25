import Header from "../components/header/Header";
import { Outlet, useLoaderData } from "react-router-dom";

function RootLayout() {
  const data = useLoaderData();
  return (
    <>
      <Header token={data.accessToken} />
      <main>
        <Outlet />
      </main>
    </>
  );
}

export default RootLayout;
