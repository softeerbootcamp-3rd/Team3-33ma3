import { useState, useRef, useEffect } from "react";

function useWebSocket(url, quitMessage) {
  const [responseMessage, setResponseMessage] = useState({
    data: "",
    message: "",
    status: "",
  });
  const webSocket = useRef();
  const reconnectionTimeout = useRef();

  useEffect(() => {
    connectWebSocket();

    // 브라우저 새로고침, 탭 닫을 시 정상 종료
    window.addEventListener("beforeunload", () => {
      closeWebSocket();
    });

    // clean up 함수
    // unmount 될 때 webSocket 연결 해제
    return () => {
      // umount 시 재연결 요청도 종료
      closeWebSocket();
    };
  }, []);

  // webSocket 연결
  function connectWebSocket() {
    webSocket.current = new WebSocket(url);

    // socket 연결 시 이벤트
    webSocket.current.onopen = () => {
      console.log("webSocket connect success");
    };

    // unmounted되지 않았을 때, 소켓이 닫힌다면 0.3초마다 재연결 시도
    webSocket.current.onclose = (event) => {
      console.log("webSocket close", event.code);
      if (event.code !== 4000) {
        console.log("webSocket reconnection");
        reconnectionTimeout.current = setTimeout(connectWebSocket, 300);
      }
    };

    // socket에서 메세지 전달 시 이벤트
    webSocket.current.onmessage = (event) => {
      setResponseMessage(JSON.parse(event.data));
    };

    // socket 에러 발생 시 이벤트
    webSocket.current.error = (error) => {
      console.log("webSocket error", error);
    };
  }

  // webSocket 메세지 전송
  function sendMessage(message) {
    if (webSocket.current.readyState === WebSocket.OPEN) {
      webSocket.current.send(JSON.stringify(message));
    }
  }

  // webSocket 연결 종료
  function closeWebSocket() {
    if (reconnectionTimeout.current) {
      clearTimeout(reconnectionTimeout.current);
    }
    if (webSocket.current.readyState === WebSocket.OPEN) {
      webSocket.current.send(JSON.stringify(quitMessage));
      // 4000: unmounted 종료 커스텀 코드
      webSocket.current.close(4000, "close");
    }
  }

  return { responseMessage, sendMessage };
}

export default useWebSocket;
