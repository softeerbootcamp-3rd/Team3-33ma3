import styled from "styled-components";
import { useEffect, useRef, useState } from "react";
import { BASE_URL, IP } from "../../../constants/url";
import { ChatHeader } from "./ChatHeader";
import { ChatMessage } from "./ChatMessage";
import { ChatInput } from "./ChatInput";
import { getCurrentTimeFormatted } from "../../../utils/dateTimeHelper";

const ChatContainer = styled.div`
  width: 100%;
  padding-top: 20px;
  display: flex;
  flex-direction: column;
`;

const ChatBodyContainer = styled.div`
  flex: 1;
  overflow-y: auto;
  background: #f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  padding: 0px 30px;
`;

const ChatBody = styled.ul`
  display: flex;
  align-items: center;
  flex-direction: column;
  height: 0;
`;

function scrollToBottom(scroll) {
  if (scroll) {
    scroll.scrollTop = scroll.scrollHeight;
  }
}

function ChatList(props) {
  const [chatHistory, setChatHistory] = useState([]);
  const [webSocket, setWebSocket] = useState(null);

  const scrollRef = useRef();

  const WebSocketServerUrl = `ws://${IP}/connect/chat/${props.roomId}/${props.memberId}`;

  function updateChatHistory(newChat) {
    const newChatData = {
      senderId: Number(props.memberId),
      contents: newChat,
      createTime: getCurrentTimeFormatted(),
      readDone: false,
    };
    setChatHistory((prev) => [...prev, newChatData]);
  }

  useEffect(() => {
    const ws = new WebSocket(WebSocketServerUrl);
    setWebSocket(ws);
    ws.onopen = () => {
      console.log(`웹소켓 연결 성공`);
      fetch(`${BASE_URL}chat/history/${props.roomId}`, {
        headers: {
          Authorization: props.accessToken,
          Accept: "application/json",
        },
      })
        .then((res) => res.json())
        .then((data) => {
          setChatHistory(data.data);
        })
        .catch((error) => console.log(error));
    };

    ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log(`메시지 수신:`, data);

      setChatHistory((prev) => [...prev, data]);
    };

    ws.onclose = () => {
      console.log(`웹소켓 연결 종료`);
    };

    ws.onerror = (error) => {
      console.error("웹소켓 오류 발생:", error);
    };

    // 컴포넌트 언마운트 시 웹소켓 연결 종료
    return () => {
      if (ws.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "chat",
          roomId: props.roomId,
          memberId: props.memberId,
        };
        console.log(closeMessage);
        ws.send(JSON.stringify(closeMessage));
        ws.close();
      }
    };
  }, [props.roomId]);

  useEffect(() => {
    scrollToBottom(scrollRef.current);
  }, [chatHistory]);

  return (
    <ChatContainer>
      <ChatHeader roomName={props.roomName} />
      <ChatBodyContainer ref={scrollRef}>
        <ChatBody>
          {chatHistory &&
            chatHistory.map((item, index) => {
              return <ChatMessage key={index} info={item} />;
            })}
        </ChatBody>
      </ChatBodyContainer>
      <ChatInput
        webSocket={webSocket}
        roomId={props.roomId}
        senderId={props.memberId}
        receiverId={props.receiverId}
        updateChatHistory={updateChatHistory}
      />
    </ChatContainer>
  );
}

export { ChatList };
