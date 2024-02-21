import React, { useEffect, useRef, useState } from "react";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";
import { BASE_URL, IP } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";

function AuctionStatus({ postId, curOfferDetails }) {
  const webSocket = useRef(null);
  const [offerList, setOfferList] = useState(curOfferDetails);
  const { memberId, accessToken } = useRouteLoaderData("root");
  const prevOfferList = useRef([
    new Set(curOfferDetails.map((offer) => offer.offerId)),
  ]);
  console.log(prevOfferList.current);

  useEffect(() => {
    function connectWebSocket() {
      console.log("websocket 연결 시도!");
      webSocket.current = new WebSocket(
        `ws://${IP}/connect/post/${postId}/${memberId}`
      );

      // socket 연결 시 이벤트
      webSocket.current.onopen = () => {
        console.log("연결 성공");
        // socket 연결 시 memberId, postId 전송
      };

      // unmounted되지 않았을 때, 소켓이 닫힌다면 0.5초마다 재연결 시도
      webSocket.current.onclose = (event) => {
        if (event.code !== 1000 && event.message !== "close") {
          setTimeout(connectWebSocket, 500);
        }
      };

      // socket에서 메세지 전달 시 이벤트
      webSocket.current.onmessage = (event) => {
        const data = JSON.parse(event.data);
        prevOfferList.current.push(new Set(data.map((offer) => offer.offerId)));
        prevOfferList.current.shift();
        setOfferList(data);
      };

      // socket 에러 발생 시 이벤트
      webSocket.current.error = (error) => {
        alert("에러가 발생했습니다.", error);
      };
    }

    connectWebSocket();

    // clean up 함수
    // unmount 될 때 webSocket 연결 해제
    return () => {
      if (webSocket.current.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "post",
          roomId: postId,
          memberId: memberId,
        };
        webSocket.current.send(JSON.stringify(closeMessage));
        webSocket.current.close();
      }
    };
  }, []);

  // TODO: util이나 api로 분리?
  // 댓글 낙찰
  function handleSelectOffer(offerId) {
    fetch(BASE_URL + `post/${postId}/offer/${offerId}/select`, {
      method: "GET",
      headers: {
        Authorization: accessToken,
        "Content-type": "application/json",
      },
    })
      .then((res) => res.json())
      .then((json) => console.log(json))
      .finally(() => {
        window.location.reload();
      });
  }

  return (
    <OptionType title={"경매 현황"}>
      <OfferList
        offerList={offerList}
        prevOfferList={prevOfferList.current[0]}
        handleSelectOffer={handleSelectOffer}
      />
    </OptionType>
  );
}

export default AuctionStatus;
