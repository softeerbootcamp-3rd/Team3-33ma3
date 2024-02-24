import { useEffect, useState, useRef } from "react";
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
  const webSocket = useRef();

  const isChatMode = (props.mode === "chat").toString();

  const WebSocketServerUrl = `wss://${IP}/connect/chatRoom/all/${props.memberId}`;

  function updateMessages(newData) {
    setMessages((prev) => {
      const newDataArray = prev.filter(
        (item) => item.roomId !== newData.roomId
      );
      newData.noReadCount = 0;

      return [newData, ...newDataArray];
    });
  }
  useEffect(() => {
    let timer = null;
    function connectWebSocket() {
      webSocket.current = new WebSocket(WebSocketServerUrl);
      webSocket.current.onopen = () => {
        console.log("웹소켓 연결 성공");
      };

      webSocket.current.onmessage = (event) => {
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

      webSocket.current.onclose = (event) => {
        console.log("웹소켓 연결 종료");
        console.log(event.code);
        if (event.code !== 4000 && event.code !== 1000) {
          console.log("재연결");
          timer = setTimeout(connectWebSocket, 500);
        }
      };

      webSocket.current.onerror = (error) => {
        console.error("웹소켓 오류 발생:", error);
      };
    }

    connectWebSocket();

    // 컴포넌트 언마운트 시 웹소켓 연결 종료
    return () => {
      if (timer) {
        clearTimeout(timer);
      }
      if (webSocket.current.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "chatRoom",
          memberId: props.memberId,
        };
        webSocket.current.send(JSON.stringify(closeMessage));
        webSocket.current.close(4000, "close");
      }
    };
  }, []);

  useEffect(() => {
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
  }, []);

  return (
    <MessageContainer chatmode={isChatMode}>
      <MessageHeader chatmode={isChatMode} />
      <MessageBodyContainer>
        <MessageBody chatmode={isChatMode}>
          {messages.map((item, index) => {
            return (
              <Message key={index} info={item} updateData={updateMessages} />
            );
          })}
        </MessageBody>
      </MessageBodyContainer>
    </MessageContainer>
  );
}

export { MessageList };
