import PostCreatePage from "../pages/PostCreatePage";
import PostPage from "../pages/PostPage";
import PostListPage from "../pages/PostListPage";
import RootLayout from "../pages/Root";
import ChatRoomPage from "../pages/ChatRoomPage";
import InquiryHistoryPage from "../pages/InquiryHistoryPage";
import CenterListPage from "../pages/CenterListPage";
import CenterInfoPage from "../pages/CenterInfoPage";
import {
  Navigate,
  Outlet,
  createBrowserRouter,
  useRouteLoaderData,
} from "react-router-dom";
import { tokenLoader } from "./auth";
import AuthenticationPage, {
  action,
} from "../pages/AuthenticationPage/AuthenticationPage";
import { CENTER_TYPE } from "../constants/options";

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    // errorElement: <ErrorPage />,
    id: "root",
    loader: tokenLoader,
    children: [
      {
        // path가 존재하지 않는 컴포넌트들 <Outlet/>에 해당 컴포넌트들 들어감
        index: true,
        element: <PostListPage />,
      },
      {
        path: "auth",
        action: action,
        element: <AuthenticationPage />,
      },
      {
        path: "post/info",
        element: <PostPage />,
      },
      {
        path: "post/list",
        element: <PostListPage />,
      },
      {
        path: "post/create",
        element: (
          <RequireAuth>
            <PostCreatePage />
          </RequireAuth>
        ),
      },
      {
        path: "inquiry-history",
        element: (
          <RequireAuth>
            <InquiryHistoryPage />
          </RequireAuth>
        ),
      },
      {
        path: "chat-room",
        element: (
          <RequireAuth>
            <ChatRoomPage />
          </RequireAuth>
        ),
      },
      {
        path: "center-review/list",
        element: <CenterListPage />,
      },
      {
        path: "center-review/info",
        element: <CenterInfoPage />,
      },
    ],
  },
]);

function RequireAuth({ children }) {
  const { accessToken, memberType } = useRouteLoaderData("root");

  if (!accessToken || Number(memberType) === CENTER_TYPE) {
    return <Navigate to={"/auth"} />;
  }
  return children;
}

export default router;
