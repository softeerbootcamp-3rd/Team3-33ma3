import React, { useEffect, useRef, useState } from "react";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";
import { IP } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";

function AuctionStatus({ postId, curOfferDetails }) {
  const webSocket = useRef(null);
  const [offerList, setOfferList] = useState(curOfferDetails);
  const recentOffer = useRef();
  const { memberId } = useRouteLoaderData("root");

  useEffect(() => {
    function connectWebSocket() {
      console.log("websocket 연결 시도!");
      webSocket.current = new WebSocket(
        `wss://${IP}/connect/post/${postId}/${memberId}`
      );

      // socket 연결 시 이벤트
      webSocket.current.onopen = () => {
        console.log("연결 성공");
        // socket 연결 시 memberId, postId 전송
      };

      // unmounted되지 않았을 때, 소켓이 닫힌다면 0.5초마다 재연결 시도
      webSocket.current.onclose = (event) => {
        console.log("close");
        console.log(event.code);
        if (event.code !== 4000 && event.code !== 1000) {
          console.log("재연결");
          setTimeout(connectWebSocket, 500);
        }
      };

      // socket에서 메세지 전달 시 이벤트
      webSocket.current.onmessage = (event) => {
        const data = JSON.parse(event.data);
        console.log(data);

        switch (data.message) {
          case "CREATE":
            recentOffer.current = data.data.offerId;
            createOffer(data.data);
            return;
          case "UPDATE":
            recentOffer.current = data.data.offerId;
            updateOffer(data.data);
            return;
          case "DELETE":
            deleteOffer(data.data);
            return;
        }
      };

      // socket 에러 발생 시 이벤트
      webSocket.current.error = (error) => {
        alert("에러가 발생했습니다.", error);
      };
    }

    connectWebSocket();

    // 브라우저 새로고침, 탭 닫을 시 정상 종료
    window.addEventListener("beforeunload", () => {
      if (webSocket.current.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "post",
          roomId: postId,
          memberId: memberId,
        };
        webSocket.current.send(JSON.stringify(closeMessage));
        webSocket.current.close(4000, "close");
      }
    });

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
        webSocket.current.close(4000, "close");
      }
    };
  }, []);

  // 댓글 생성
  function createOffer(data) {
    setOfferList((prevState) =>
      sortOffer([...prevState, { ...data, animation: "CREATE" }])
    );
  }

  // 댓글 수정
  function updateOffer(data) {
    console.log("update", data);
    setOfferList((prevState) => {
      const prevList = prevState.filter(
        (offer) => offer.offerId != data.offerId
      );
      return sortOffer([...prevList, { ...data, animation: "UPDATE" }]);
    });
  }

  // 댓글 삭제
  function deleteOffer(offerId) {
    setOfferList((prev) =>
      prev.filter((offer) => {
        return offerId !== offer.offerId;
      })
    );
  }

  // 댓글 정렬
  function sortOffer(list) {
    return [...list].sort((o1, o2) => {
      if (o1.price !== o2.price) {
        return o1.price - o2.price;
      }
      return o2.score - o1.score;
    });
  }

  return (
    <OptionType title={"경매 현황"}>
      <OfferList offerList={offerList} recentOffer={recentOffer} />
    </OptionType>
  );
}

export default AuctionStatus;
