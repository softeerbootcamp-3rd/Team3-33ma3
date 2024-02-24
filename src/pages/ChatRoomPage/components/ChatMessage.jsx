import styled from "styled-components";
import { getMemberId } from "../../../utils/auth";

const ChatBox = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  padding: 10px;
  justify-content: ${(props) => (props.mode === "true" ? "end;" : "start;")};
`;

const Message = styled.div`
  max-width: 300px;
  font-size: ${({ theme }) => theme.fontSize.regular};
  font-weight: 500;
  padding: 10px 20px;
  border-radius: 14px;
  overflow: hidden;
  word-wrap: break-word;
  white-space: normal;
  background: ${(props) => (props.mode === "true" ? "#dfe2fa" : "#ffffff")};
  box-shadow: ${({ theme }) => theme.boxShadow.normal};
`;

const StatusContainer = styled.div`
  font-size: ${({ theme }) => theme.fontSize.small};
  font-weight: 500;
`;

const TimeContainer = styled.div`
  padding-top: 3px;
  color: ${(props) => props.theme.colors.border_strong};
`;

const ReadContainer = styled.div`
  display: flex;
  color: ${({ theme }) => theme.colors.text_brand};
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
            {/* {!info.readDone && (
              <ReadContainer mode={isSender.toString()}>1</ReadContainer>
            )} */}
            <TimeContainer>{info.createTime}</TimeContainer>
          </StatusContainer>
        )}
        <Message mode={isSender.toString()}>{info.contents}</Message>
        {!isSender && (
          <StatusContainer>
            {/* {!info.readDone && (
              <ReadContainer mode={isSender.toString()}>1</ReadContainer>
            )} */}
            <TimeContainer>{info.createTime}</TimeContainer>
          </StatusContainer>
        )}
      </ChatWrapper>
    </ChatBox>
  );
}
export { ChatMessage };
