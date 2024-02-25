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
  const webSocket = useRef();

  const scrollRef = useRef();

  const WebSocketServerUrl = `wss://${IP}/connect/chat/${props.roomId}/${props.memberId}`;

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
    let timer = null;
    function connectWebSocket() {
      webSocket.current = new WebSocket(WebSocketServerUrl);
      webSocket.current.onopen = () => {
        console.log(`웹소켓 연결 성공`);
      };

      webSocket.current.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log(`메시지 수신:`, data);
        const newData = {
          senderId: Number(props.receiverId),
          contents: data.contents,
          createTime: data.createTime,
          readDone: true,
        };
        setChatHistory((prev) => [...prev, newData]);
      };

      webSocket.current.onclose = (event) => {
        console.log(`웹소켓 연결 종료`);
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

    fetch(`${BASE_URL}chat/history/${props.roomId}`, {
      headers: {
        Authorization: props.accessToken,
        Accept: "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => {
        const newData = data.data;
        setChatHistory((prev) => newData);
      })
      .catch((error) => console.log(error));

    // 컴포넌트 언마운트 시 웹소켓 연결 종료
    return () => {
      if (timer) {
        clearTimeout(timer);
      }
      if (webSocket.current.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "chat",
          roomId: props.roomId,
          memberId: props.memberId,
        };
        console.log(closeMessage);
        webSocket.current.send(JSON.stringify(closeMessage));
        webSocket.current.close(4000, "close");
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
        webSocket={webSocket.current}
        roomId={props.roomId}
        senderId={props.memberId}
        receiverId={props.receiverId}
        updateChatHistory={updateChatHistory}
      />
    </ChatContainer>
  );
}

export { ChatList };
