import React from "react";
import { MessageList } from "./components/MessageList";
import { ChatList } from "./components/ChatList";
import { useLoaderData, useSearchParams } from "react-router-dom";
import styled from "styled-components";
import { getMemberId } from "../../utils/auth";

const ChatMessageContainer = styled.div`
  display: ${(props) => (props.mode === "chat" ? "flex;" : ";")}
  flex-direction: row;
`;

function ChatRoomPage() {
  const authData = useLoaderData();
  const accessToken = authData.accessToken;

  const [searchParams] = useSearchParams();
  const urlMode = searchParams.get("mode");
  const urlRoomId = searchParams.get("room-id");
  const urlClientId = searchParams.get("client-id");
  const urlCenterId = searchParams.get("center-id");
  const urlCenterName = searchParams.get("center-name");
  const memberId = getMemberId();
  const receiverId = memberId === urlClientId ? urlCenterId : urlClientId;

  return (
    <ChatMessageContainer mode={urlMode}>
      <MessageList
        mode={urlMode}
        memberId={memberId}
        roomId={urlRoomId}
        accessToken={accessToken}
      />
      {urlMode === "chat" && (
        <ChatList
          memberId={memberId}
          receiverId={receiverId}
          clientId={urlClientId}
          centerId={urlCenterId}
          roomId={urlRoomId}
          centerName={urlCenterName}
          accessToken={accessToken}
        />
      )}
    </ChatMessageContainer>
  );
}

export default ChatRoomPage;
