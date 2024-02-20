import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";
import { useEffect } from "react";
import { BASE_URL } from "../../../constants/url";
import { useLoaderData } from "react-router-dom";
const ChatContainer = styled.div`
  width: 970px;
  padding: 20px;
`;

const ChatHeader = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f8f8fa;
  border-radius: 14px 14px 0px 0px;
  border-bottom: 2px solid ${(props) => props.theme.colors.surface_weak};
  gap: 550px;
`;

const Logo = styled.img`
  width: 70px;
  height: 80px;
`;

const CenterInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const CenterName = styled.div``;

const CenterStatus = styled.div``;

const SuccessfulBidButton = styled.button`
  color: white;
  width: 70px;
  height: 40px;
  background: ${(props) => props.theme.colors.surface_brand};
  border-radius: 14px;
`;

const ChatBody = styled.div`
  height: 700px;
  background: #f8f8fa;
  border-radius: 0px 0px 14px 14px;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
`;

const ChatBox = styled.div``;

const ChatMessage = styled.div``;

const DateContainer = styled.div``;

const InputContainer = styled.div``;

const InputText = styled.input``;

const SubmitText = styled.button`
  width: 100px;
  height: 50px;
`;

const CenterContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
`;

function ChatList(props) {
  console.log(props.roomId);

  const authData = useLoaderData();
  const accessToken = authData.accessToken;

  useEffect(() => {
    fetch(`${BASE_URL}chat/history/${props.roomId}`, {
      headers: {
        Authorization: accessToken,
        Accept: "application/json",
      },
    })
      .then((res) => res.json())
      .then((data) => console.log(data));
  }, []);

  return (
    <ChatContainer>
      <ChatHeader>
        <CenterContainer>
          <Logo src={CenterLogo} />
          <CenterInfo>
            <CenterName>민우 센타</CenterName>
            <CenterStatus>부재중</CenterStatus>
          </CenterInfo>
        </CenterContainer>
        <SuccessfulBidButton>낙찰</SuccessfulBidButton>
      </ChatHeader>
      <ChatBody></ChatBody>
    </ChatContainer>
  );
}

export { ChatList };
