import styled from "styled-components";

const Logo = styled.img`
  width: 100px;
  height: 80px;
`;

const OpponentInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
`;

const OpponentName = styled.p`
  color: ${(props) => props.theme.colors.surface_black};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 350;
`;

const OpponentMessage = styled.p`
  color: ${(props) => props.theme.colors.border_strong};
`;

const MessageCount = styled.div`
  display: flex;
  color: white;
  background: red;
  border-radius: 100%;
  width: 25px;
  height: 25px;
  align-items: center;
  justify-content: center;
`;

const MessageBox = styled.li`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 15px;
  border-bottom: 1px solid ${(props) => props.theme.colors.surface_weak};
`;

const KeyContent = styled.div`
  display: flex;
  flex-direction: row;
  gap: 20px;
`;

function ChatMessage({ name, content, count }) {
  return (
    <>
      <MessageBox>
        <KeyContent>
          <Logo />
          <OpponentInfo>
            <OpponentName>{name}</OpponentName>
            <OpponentMessage>{content}~</OpponentMessage>
          </OpponentInfo>
        </KeyContent>
        <MessageCount>{count}</MessageCount>
      </MessageBox>
    </>
  );
}

export { ChatMessage };
