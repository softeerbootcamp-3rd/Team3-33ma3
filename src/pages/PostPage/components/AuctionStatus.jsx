import React, { useEffect, useRef, useState } from "react";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";

function AuctionStatus() {
  const webSocket = useRef(null);
  const [offerList, setOfferList] = useState([]);

  useEffect(() => {
    // /connect/{postId}/{memberId}
    webSocket.current = new WebSocket("ws://192.168.1.192:8080/connect/3/2");

    // socket 연결 시 이벤트
    webSocket.current.onopen = () => {
      console.log("WebSocket 연결!!!!!");
      // socket 연결 시 memberId, postId 전송
    };

    // socket 해제 시 이벤트
    webSocket.current.onclose = () => {
      console.log("closed");
    };

    // socket에서 메세지 전달 시 이벤트
    webSocket.current.onmessage = (event) => {
      console.log(event.data);
      setOfferList(JSON.parse(event.data));
    };

    // socket 에러 발생 시 이벤트
    webSocket.current.error = (event) => {
      console.log("error");
    };

    // clean up 함수
    // unmount 될 때 webSocket 연결 해제
    return () => {
      console.log("unmounted");
      webSocket.current.close();
    };
  }, []);

  const sendMessage = (message) => {
    if (webSocket.current.readyState === WebSocket.OPEN) {
      webSocket.current.send(message);
    }
  };

  return (
    <OptionType title={"경매 현황"}>
      <OfferList offerList={offerList} />
    </OptionType>
  );
}

export default AuctionStatus;
