import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";
import { useNavigate } from "react-router-dom";
import { MAX_MESSAGE_LENGTH } from "../../../constants/options";

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

const OpponentMessage = styled.div`
  display: flex;
  justify-content: space-between;
  color: ${(props) => props.theme.colors.border_strong};
  font-size: 15px;
  height: 20px;
  align-items: center;
`;

const MessageCount = styled.div`
  display: flex;
  color: white;
  background: red;
  border-radius: 100%;
  width: 20px;
  height: 20px;
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

function TruncatedText(text, maxLength) {
  const displayText =
    text.length > maxLength
      ? `${text.substring(0, maxLength - 3)} . . .`
      : text;

  return displayText;
}

function Message(props) {
  const navigate = useNavigate();
  function handleOnClick() {
    const data = props.info;
    if (data.noReadCount !== 0) {
      props.updateData(data);
    }
    localStorage.setItem("opponentName", data.memberName);
    navigate(
      `?mode=chat&client-id=${data.clientId}&center-id=${data.centerId}&room-id=${data.roomId}`
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
            <OpponentMessage>
              {TruncatedText(props.info.lastMessage, MAX_MESSAGE_LENGTH)}
              {props.info.noReadCount !== 0 && (
                <MessageCount>{props.info.noReadCount}</MessageCount>
              )}
            </OpponentMessage>
          </OpponentInfo>
        </KeyContent>
      </MessageBox>
    </>
  );
}

export { Message };
