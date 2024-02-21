import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";
import { useNavigate } from "react-router-dom";
import { getCurrentTimeFormatted } from "../../../utils/dateTimeHelper";

const Logo = styled.img`
  width: 45px;
  height: 60px;
`;

const OpponentInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  justify-content: center;
  flex: 1;
`;

const OpponentName = styled.p`
  color: ${(props) => props.theme.colors.surface_black};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
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
  width: 100%;
  box-sizing: border-box;
  padding: 15px;
  align-items: center;
  border-bottom: 1px solid ${(props) => props.theme.colors.surface_weak};
  &:hover {
    background: ${(props) => props.theme.colors.surface_weak};
  }
  font-weight: 500;
  font-size: ${({ theme }) => theme.fontSize.regular};
`;

const KeyContent = styled.div`
  display: flex;
  flex-direction: row;
  gap: 20px;
  flex: 1;
`;

const TimeContainer = styled.div`
  color: ${(props) => props.theme.colors.border_strong};
`;

const NameTimeWrapper = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  justify-content: space-between;
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
            <NameTimeWrapper>
              <OpponentName>{props.info.memberName}</OpponentName>
              <TimeContainer>{props.info.createTime}</TimeContainer>
            </NameTimeWrapper>
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
