import Header from "../components/header/header";
import { Outlet, useLoaderData } from "react-router-dom";

function RootLayout() {
  const token = useLoaderData();

  return (
    <>
      <Header token={token} />
      <main>
        <Outlet />
      </main>
    </>
  );
}

export default RootLayout;
