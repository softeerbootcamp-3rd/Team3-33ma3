import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";
import { useNavigate } from "react-router-dom";

const Logo = styled.img`
  width: 45px;
  height: 60px;
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
  align-items: center;
  border-bottom: 1px solid ${(props) => props.theme.colors.surface_weak};
  &:hover {
    background: ${(props) => props.theme.colors.surface_weak};
  }
`;

const KeyContent = styled.div`
  display: flex;
  flex-direction: row;
  gap: 20px;
`;

function Message(props) {
  const navigate = useNavigate();
  function handleOnClick() {
    navigate(
      `/chat-room?mode=chat&client-id=${props.clientId}&center-id=${props.centerId}&room-id=${props.info.roomId}&center-name=${props.info.memberName}`
    );
  }

  return (
    <>
      <MessageBox onClick={handleOnClick}>
        <KeyContent>
          <Logo src={CenterLogo} />
          <OpponentInfo>
            <OpponentName>{props.info.memberName}</OpponentName>
            <OpponentMessage>{props.info.lastMessage}</OpponentMessage>
          </OpponentInfo>
        </KeyContent>
        {props.info.noReadCount !== 0 && (
          <MessageCount>{props.info.noReadCount}</MessageCount>
        )}
      </MessageBox>
    </>
  );
}

export { Message };
