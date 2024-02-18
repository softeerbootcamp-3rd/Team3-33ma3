import { useEffect, useState } from "react";
import { BASE_URL } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";
import styled from "styled-components";
import { ChatMessage } from "./ChatMessage";
import { ChatHeader } from "./ChatHeader";

const ChatMessageBody = styled.ul`
  width: 1000px;
  height: 700px;
  background: #f8f8fa;
  border-radius: 0px 0px 14px 14px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  overflow-y: auto;
  overflow-x: hidden;
`;

function ChatList() {
  const { accessToken } = useRouteLoaderData("root");
  return (
    <>
      <ChatHeader />
      <ChatMessageBody>
        <ChatMessage name={"민우"} content={"안녕하세요~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
        <ChatMessage name={"지홍"} content={"네~"} count={2} />
      </ChatMessageBody>
    </>
  );
}

export { ChatList };
