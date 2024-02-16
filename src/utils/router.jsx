import PostCreatePage from "../pages/PostCreatePage";
import PostPage from "../pages/PostPage";
import PostListPage from "../pages/PostListPage";
import RootLayout from "../pages/Root";
import ChatRoomPage from "../pages/ChatRoomPage";
import InquiryHistoryPage from "../pages/InquiryHistoryPage";
import CenterListPage from "../pages/CenterListPage";
import CenterInfoPage from "../pages/CenterInfoPage";
import { Outlet, createBrowserRouter } from "react-router-dom";
import { tokenLoader } from "./auth";
import AuthenticationPage, {
  action,
} from "../pages/AuthenticationPage/AuthenticationPage";

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
        // 로그인 안 할 시 접근 불가능
        path: "post/create",
        element: <PostCreatePage />,
      },
      {
        path: "inquiry-history",
        element: <InquiryHistoryPage />,
      },
      {
        path: "chat-room",
        element: <ChatRoomPage />,
      },
      {
        path: "center-review",
        element: <Outlet />,
        children: [
          {
            path: "info",
            element: <CenterInfoPage />,
          },
          {
            path: "list",
            element: <CenterListPage />,
          },
        ],
      },
    ],
  },
]);

export default router;