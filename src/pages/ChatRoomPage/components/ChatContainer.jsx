import styled from "styled-components";
import { ChatList } from "./ChatList";

const MessageContainer = styled.div`
  width: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
`;

function ChatContainer() {
  return (
    <MessageContainer>
      <ChatList />
    </MessageContainer>
  );
}

export { ChatContainer };
