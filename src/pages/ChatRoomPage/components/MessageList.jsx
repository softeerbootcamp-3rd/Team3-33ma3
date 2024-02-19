import { useEffect, useState } from "react";
import { BASE_URL } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";
import styled from "styled-components";
import { Message } from "./Message";
import { MessageHeader } from "./MessageHeader";

const MessageBody = styled.ul`
  width: ${(props) => (props.chatmode === "true" ? "400px;" : "1000px;")}
  height: 700px;
  background: #f8f8fa;
  border-radius: 0px 0px 14px 14px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  overflow-y: auto;
  overflow-x: hidden;
`;

const MessageContainer = styled.div`
  width: ${(props) => (props.chatmode === "true" ? "400px;" : "auto;")}
  display: ${(props) => (props.chatmode === "true" ? ";" : "flex;")}
  flex-direction: column;
  align-items: center;
  padding: 20px;
`;

function MessageList() {
  const { accessToken } = useRouteLoaderData("root");

  const [isChatMode, setIsChatMode] = useState(false);
  const data = {
    name: "민우",
    content: "안녕하세요~.",
    count: 2,
  };
  return (
    <MessageContainer chatmode={isChatMode.toString()}>
      <MessageHeader chatmode={isChatMode.toString()} />
      <MessageBody chatmode={isChatMode.toString()}>
        <Message info={data} onClick={setIsChatMode} />
      </MessageBody>
    </MessageContainer>
  );
}

export { MessageList };
