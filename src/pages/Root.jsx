import Header from "../components/header/header";
import { Outlet } from "react-router-dom";

function RootLayout() {
  return (
    <>
      <Header />
      <main>
        <Outlet />
      </main>
    </>
  );
}

export default RootLayout;
