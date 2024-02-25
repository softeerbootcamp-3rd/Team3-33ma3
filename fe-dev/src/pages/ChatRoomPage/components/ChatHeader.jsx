import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";
import SubmitButton from "../../../components/button/SubmitButton";
import { getOpponentName } from "../../../utils/auth";

const Header = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 90px;
  width: 100%;
  padding: 0px 20px;
  background: #f8f8fa;
  border-radius: 14px 14px 0px 0px;
  border-bottom: 2px solid ${(props) => props.theme.colors.surface_weak};
  box-sizing: border-box;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
`;

const CenterContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
`;

const Logo = styled.img`
  width: 58px;
  height: 60px;
`;

const CenterInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const CenterName = styled.div`
  font-size: ${({ theme }) => theme.fontSize.medium};
  font-weight: 700;
`;

const CenterStatus = styled.div`
  font-size: ${({ theme }) => theme.fontSize.regular};
  font-weight: 500;
`;

function ChatHeader(props) {
  const opponentName = getOpponentName();
  const roomName = opponentName ? opponentName : props.roomName;
  return (
    <Header>
      <CenterContainer>
        <Logo src={CenterLogo} />
        <CenterInfo>
          <CenterName>{roomName}</CenterName>
          <CenterStatus></CenterStatus>
        </CenterInfo>
      </CenterContainer>
      <SubmitButton size={"small"}>낙찰</SubmitButton>
    </Header>
  );
}

export { ChatHeader };
