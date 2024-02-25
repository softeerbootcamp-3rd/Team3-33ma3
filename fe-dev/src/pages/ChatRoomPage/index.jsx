import React from "react";
import { MessageList } from "./components/MessageList";
import { ChatList } from "./components/ChatList";
import { useLoaderData, useSearchParams } from "react-router-dom";
import styled from "styled-components";
import { getMemberId, getMemberType } from "../../utils/auth";
import Page from "/src/components/post/Page.jsx";
import { MEMBER_TYPE } from "../../constants/options";

const ChatMessageContainer = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  flex-direction: row;
  gap: 20px;
`;

function ChatRoomPage() {
  const authData = useLoaderData();
  const accessToken = authData.accessToken;

  const [searchParams] = useSearchParams();
  const urlMode = searchParams.get("mode");
  const urlRoomId = searchParams.get("room-id");
  const urlClientId = searchParams.get("client-id");
  const urlCenterId = searchParams.get("center-id");
  const urlRoomName = searchParams.get("room-name");
  const memberId = getMemberId();
  const memberType = getMemberType();
  const receiverId =
    Number(memberType) === MEMBER_TYPE ? urlCenterId : urlClientId;
  console.log(
    `senderId: ${memberId} receiverId: ${receiverId} memberType: ${memberType}`
  );

  return (
    <Page>
      <ChatMessageContainer mode={urlMode}>
        <MessageList
          mode={urlMode}
          memberId={memberId}
          accessToken={accessToken}
        />
        {urlMode === "chat" && (
          <ChatList
            memberId={memberId}
            receiverId={receiverId}
            clientId={urlClientId}
            centerId={urlCenterId}
            roomId={urlRoomId}
            roomName={urlRoomName}
            accessToken={accessToken}
          />
        )}
      </ChatMessageContainer>
    </Page>
  );
}

export default ChatRoomPage;
