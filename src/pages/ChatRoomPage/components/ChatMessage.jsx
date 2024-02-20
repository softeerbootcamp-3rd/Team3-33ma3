import styled from "styled-components";
import { getMemberId } from "../../../utils/auth";

const ChatBox = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  padding: 20px;
  justify-content: ${(props) => (props.mode === "true" ? "end;" : "start;")};
`;

const Message = styled.div`
  max-width: 300px;
  width: auto;
  height: auto;
  font-size: 18px;
  padding: 20px;
  border-radius: 14px;
  overflow: hidden;
  word-wrap: break-word;
  white-space: normal;
  border: 1px solid
    ${(props) => (props.mode === "true" ? "#9398be;" : "black;")};
  background: ${(props) => (props.mode === "true" ? "#dfe2fa" : "#ffffff")};
`;

const StatusContainer = styled.div`
  font-size: 10px;
`;

const TimeContainer = styled.div`
  color: ${(props) => props.theme.colors.border_strong};
  font-size: 16px;
`;

const ReadContainer = styled.div`
  display: flex;
  color: ${(props) => props.theme.colors.surface_brand};
  justify-content: ${(props) => (props.mode === "true" ? "end;" : "start;")};
`;

const ChatWrapper = styled.div`
  display: flex;
  width: 440px;
  justify-content: ${(props) => (props.mode === "true" ? "end;" : "start;")};
  align-items: end;
  gap: 10px;
`;

function ChatMessage({ info }) {
  const memberId = getMemberId();
  const isSender = info.senderId === Number(memberId);

  return (
    <ChatBox mode={isSender.toString()}>
      <ChatWrapper mode={isSender.toString()}>
        {isSender && (
          <StatusContainer>
            {info.readDone && (
              <ReadContainer mode={isSender.toString()}>1</ReadContainer>
            )}
            <TimeContainer>{info.createTime}</TimeContainer>
          </StatusContainer>
        )}
        <Message mode={isSender.toString()}>{info.contents}</Message>
        {!isSender && (
          <StatusContainer>
            {info.readDone && (
              <ReadContainer mode={isSender.toString()}>1</ReadContainer>
            )}
            <TimeContainer>{info.createTime}</TimeContainer>
          </StatusContainer>
        )}
      </ChatWrapper>
    </ChatBox>
  );
}
export { ChatMessage };