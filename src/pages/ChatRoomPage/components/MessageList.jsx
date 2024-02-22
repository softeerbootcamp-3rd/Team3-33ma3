import { useEffect, useState } from "react";
import { BASE_URL, IP } from "../../../constants/url";
import styled from "styled-components";
import { Message } from "./Message";
import { MessageHeader } from "./MessageHeader";

const MessageBodyContainer = styled.div`
  flex: 1;
  width: 100%;
  background: #f8f8fa;
  border-radius: 0px 0px 14px 14px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  overflow: auto;
`;

const MessageBody = styled.ul`
  width: ${(props) => (props.chatmode === "true" ? "400px" : "100%")};
  height: 0;
`;

const MessageContainer = styled.div`
  width: ${(props) => (props.chatmode === "true" ? "400px" : "100%")};
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0px 0px 0px;
  box-sizing: border-box;
`;

function MessageList(props) {
  const [messages, setMessages] = useState([]);
  const [webSocket, setWebSocket] = useState(null);

  const isChatMode = (props.mode === "chat").toString();

  const WebSocketServerUrl = `ws://${IP}/connect/chatRoom/all/${props.memberId}`;
  useEffect(() => {
    const ws = new WebSocket(WebSocketServerUrl);
    setWebSocket(ws);
    ws.onopen = () => {
      console.log("웹소켓 연결 성공");
      fetch(`${BASE_URL}chatRoom/all`, {
        headers: {
          Authorization: props.accessToken,
          Accept: "application/json",
        },
      })
        .then((res) => res.json())
        .then((data) => {
          setMessages(data.data);
        });
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log("메시지 수신:", data);
      setMessages((prev) => {
        const newData = data;
        const newDataArray = prev.filter(
          (item) => item.roomId !== newData.roomId
        );
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
          type: "chatRoom",
          memberId: props.memberId,
        };
        ws.send(JSON.stringify(closeMessage));
        ws.close();
      }
    };
  }, []);

  return (
    <MessageContainer chatmode={isChatMode}>
      <MessageHeader chatmode={isChatMode} />
      <MessageBodyContainer>
        <MessageBody chatmode={isChatMode}>
          {messages.map((item, index) => {
            return <Message key={index} info={item} />;
          })}
        </MessageBody>
      </MessageBodyContainer>
    </MessageContainer>
  );
}

export { MessageList };
