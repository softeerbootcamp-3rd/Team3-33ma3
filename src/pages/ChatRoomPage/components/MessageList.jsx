import { useEffect, useState } from "react";
import { BASE_URL, IP } from "../../../constants/url";
import {
  useRouteLoaderData,
  useSearchParams,
  useLoaderData,
} from "react-router-dom";
import styled from "styled-components";
import { Message } from "./Message";
import { MessageHeader } from "./MessageHeader";
import { getMemberId } from "../../../utils/auth";

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
  const [isChatMode, setIsChatMode] = useState(false);
  const data = {
    name: "민우",
    content: "안녕하세요~.",
    count: 2,
  };

  const [messages, setMessages] = useState([]);
  const [webSocket, setWebSocket] = useState(null);

  const [searchParams] = useSearchParams();
  const authData = useLoaderData();
  const accessToken = authData.accessToken;

  const memberId = 1;
  const WebSocketServerUrl = `ws://${IP}/connect/chatRoom/all/${memberId}`;
  useEffect(() => {
    const ws = new WebSocket(WebSocketServerUrl);
    setWebSocket(ws);
    ws.onopen = () => {
      console.log("웹소켓 연결 성공");
      fetch(`${BASE_URL}chatRoom/all`, {
        headers: {
          Authorization: accessToken,
          Accept: "application/json",
        },
      })
        .then((res) => res.json())
        .then((data) => {
          setMessages(data.data);
        });
    };

    ws.onmessage = (event) => {
      console.log("메시지 수신:", JSON.parse(event.data));
      setMessages((prev) => {
        const newData = JSON.parse(event.data);
        const newDataArray = prev.filter(
          (item) => item.roomId !== newData.roomId
        );
        console.log(newData, newDataArray);
        return [newData, ...newDataArray];
      });
    };

    ws.onclose = () => {
      console.log("웹소켓 연결 종료");
    };

    ws.onerror = (error) => {
      console.error("웹소켓 오류 발생:", error);
    };

    // 컴포넌트 언마운트 시 웹소켓 연결 종료
    return () => {
      if (ws.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "chat",
          roomId: postId,
          memberId: memberId,
        };
        ws.send(JSON.stringify(closeMessage));
        ws.close();
      }
    };
  }, []);

  console.log(messages);
  return (
    <MessageContainer chatmode={isChatMode.toString()}>
      <MessageHeader chatmode={isChatMode.toString()} />
      <MessageBody chatmode={isChatMode.toString()}>
        {messages.map((item, index) => {
          return <Message key={index} info={item} onClick={setIsChatMode} />;
        })}
      </MessageBody>
    </MessageContainer>
  );
}

export { MessageList };
