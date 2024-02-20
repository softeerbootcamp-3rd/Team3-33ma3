import styled from "styled-components";
import { useEffect, useState } from "react";
import { BASE_URL, IP } from "../../../constants/url";
import { useLoaderData, useSearchParams } from "react-router-dom";
import { ChatHeader } from "./ChatHeader";
import { ChatMessage } from "./ChatMessage";
import { getMemberId } from "../../../utils/auth";
import { ChatInput } from "./ChatInput";

const ChatContainer = styled.div`
  width: 970px;
  padding: 20px;
`;

const ChatBody = styled.ul`
  display: flex;
  align-items: center;
  flex-direction: column;
  height: 620px;
  background: #f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  padding: 0px 30px;
  overflow-y: auto;
  overflow-x: hidden;
`;

const DateContainer = styled.div``;

function ChatList(props) {
  const [chatHistory, setChatHistory] = useState([]);
  const [webSocket, setWebSocket] = useState(null);

  const authData = useLoaderData();
  const accessToken = authData.accessToken;
  const [searchParams] = useSearchParams();
  const urlRoomId = searchParams.get("room-id");
  const memberId = getMemberId();

  const WebSocketServerUrl = `ws://${IP}/connect/chat/${urlRoomId}/${memberId}`;

  useEffect(() => {
    const ws = new WebSocket(WebSocketServerUrl);
    setWebSocket(ws);
    ws.onopen = () => {
      console.log("웹소켓 연결 성공");
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log("메시지 수신:", data);

      setChatHistory((prev) => [...prev, data]);
    };

    ws.onclose = () => {
      console.log("웹소켓 연결 종료");
    };

    ws.onerror = (error) => {
      console.error("웹소켓 오류 발생:", error);
    };

    fetch(`${BASE_URL}chat/history/${urlRoomId}`, {
      headers: {
        Authorization: accessToken,
        Accept: "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        setChatHistory(data.data);
      });
    // 컴포넌트 언마운트 시 웹소켓 연결 종료
    return () => {
      if (ws.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "chatRoom",
          roomId: urlRoomId,
          memberId: memberId,
        };
        ws.send(JSON.stringify(closeMessage));
        ws.close();
      }
    };
  }, []);

  return (
    <ChatContainer>
      <ChatHeader centerName={props.centerName} />
      <ChatBody>
        {chatHistory &&
          chatHistory.map((item, index) => {
            return <ChatMessage key={index} info={item} />;
          })}
      </ChatBody>
      <ChatInput />
    </ChatContainer>
  );
}

export { ChatList };
