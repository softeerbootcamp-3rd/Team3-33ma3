import React from "react";
import { MessageList } from "./components/MessageList";
import { ChatList } from "./components/ChatList";
import { useSearchParams } from "react-router-dom";
import styled from "styled-components";

const ChatMessageContainer = styled.div`
  display: ${(props) => (props.mode === "chat" ? "flex;" : ";")}
  flex-direction: row;
`;

function ChatRoomPage() {
  const [searchParams] = useSearchParams();
  const urlMode = searchParams.get("mode");
  const urlRoomId = searchParams.get("room-id");
  const urlCenterName = searchParams.get("center-name");
  return (
    <ChatMessageContainer mode={urlMode}>
      <MessageList />
      {urlMode === "chat" && (
        <ChatList roomId={urlRoomId} centerName={urlCenterName} />
      )}
    </ChatMessageContainer>
  );
}

export default ChatRoomPage;
