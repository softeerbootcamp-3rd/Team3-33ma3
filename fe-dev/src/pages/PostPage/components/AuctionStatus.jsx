import React, { useEffect, useRef, useState } from "react";
import OptionType from "../../../components/post/OptionType";
import OfferList from "./OfferList";
import useWebSocket from "../../../hooks/useWebSocket";
import { IP } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";

function AuctionStatus({ postId, curOfferDetails }) {
  const [offerList, setOfferList] = useState(curOfferDetails);
  const [recentOfferId, setRecentOfferId] = useState();
  const { memberId } = useRouteLoaderData("root");
  const quitMessage = {
    type: "post",
    roomId: postId,
    memberId: memberId,
  };
  const { responseMessage } = useWebSocket(
    `wss://${IP}/connect/post/${postId}/${memberId}`,
    quitMessage
  );

  useEffect(() => {
    console.log(responseMessage);
    switch (responseMessage.message) {
      case "CREATE":
        setRecentOfferId(responseMessage.data.offerId);
        createOffer(responseMessage.data);
        return;
      case "UPDATE":
        setRecentOfferId(responseMessage.data.offerId);
        updateOffer(responseMessage.data);
        return;
      case "DELETE":
        deleteOffer(responseMessage.data);
        return;
    }
  }, [responseMessage]);

  // 댓글 생성
  function createOffer(data) {
    setOfferList((prevState) =>
      sortOffer([...prevState, { ...data, animation: "CREATE" }])
    );
  }

  // 댓글 수정
  function updateOffer(data) {
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
      <OfferList offerList={offerList} recentOfferId={recentOfferId} />
    </OptionType>
  );
}

export default AuctionStatus;
