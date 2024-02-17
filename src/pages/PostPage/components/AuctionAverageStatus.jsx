import React, { useEffect, useRef, useState } from "react";
import styled from "styled-components";
import OptionType from "../../../components/post/OptionType";
import { IP } from "../../../constants/url";
import { useRouteLoaderData } from "react-router-dom";
import SubmitButton from "../../../components/button/SubmitButton";
import OfferModal from "./OfferModal";
import { CENTER_TYPE, MEMBER_TYPE } from "../../../constants/options";

const AverageContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 55px;
`;

const TextContainer = styled.div`
  color: ${({ theme }) => theme.colors.text_strong};
  font-size: ${({ theme }) => theme.fontSize.medium};
  font-weight: 500;
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const BoldText = styled.p`
  color: ${(props) =>
    props.color === "red"
      ? props.theme.colors.text_red
      : props.theme.colors.text_strong};
  font-weight: 700;
`;

const Text = styled.div`
  display: flex;
`;

function AuctionAverageStatus({ curAvgPrice, curOfferDetail, postId }) {
  const webSocket = useRef();
  const [avgPrice, setAvgPrice] = useState(curAvgPrice);
  const [offerDetail, setOfferDetail] = useState(curOfferDetail);
  const { memberId, memberType } = useRouteLoaderData("root");
  const [isModalOpen, setIsModalOpen] = useState(false);

  // 기존 경매가 수정
  function handleSubmit2() {}

  // TODO: 웹 소켓 연결
  useEffect(() => {
    webSocket.current = new WebSocket(
      `ws://${IP}connect/post/${postId}/${memberId}`
    );

    webSocket.current.onopen = () => {
      console.log("WebSocket 연결! - 평균 버전");
    };

    webSocket.current.onclose = () => {
      console.log("closed");
    };

    webSocket.current.onmessage = (event) => {
      console.log(event.data);
      setAvgPrice(JSON.parse(event.data).avgPrice);
    };

    return () => {
      if (webSocket.current.readyState === WebSocket.OPEN) {
        const closeMessage = {
          type: "post",
          postId: postId,
          memberId: memberId,
        };
        webSocket.current.send(JSON.stringify(closeMessage));
        webSocket.current.close();
      }
    };
  }, []);

  // const sendMessage = (message) => {
  //   if (webSocket.current.readyState === WebSocket.OPEN) {
  //     webSocket.current.send(message);
  //   }
  // };

  // type 이 center인 경우 경매 참여하기 or 수정하기 버튼 보여주기
  // type 이 user인 경우 평균만 보여주기
  return (
    <>
      {isModalOpen && (
        <OfferModal
          postId={postId}
          handleClose={() => setIsModalOpen(false)}
          offerDetail={offerDetail}
          updateOfferDetail={setOfferDetail}
        />
      )}
      <AverageContainer>
        {(Number(memberType) === MEMBER_TYPE || offerDetail) && (
          <OptionType title={"경매 현황"}>
            <TextContainer>
              {offerDetail && (
                <Text>
                  <p>내가 제시한 금액은 </p>
                  <BoldText>{offerDetail.price}만원</BoldText>
                  <p>입니다.</p>
                </Text>
              )}
              <Text>
                <p>현재 제시 금액의 </p>
                <BoldText color="red">평균가는 {avgPrice}만원</BoldText>
                <p>입니다. {offerDetail && "수정하시겠습니까?"}</p>
              </Text>
            </TextContainer>
          </OptionType>
        )}
        {Number(memberType) === CENTER_TYPE &&
          (offerDetail ? (
            <SubmitButton onClick={() => setIsModalOpen(true)}>
              수정하기
            </SubmitButton>
          ) : (
            <SubmitButton onClick={() => setIsModalOpen(true)}>
              경매 참여하기
            </SubmitButton>
          ))}
      </AverageContainer>
    </>
  );
}

export default AuctionAverageStatus;
