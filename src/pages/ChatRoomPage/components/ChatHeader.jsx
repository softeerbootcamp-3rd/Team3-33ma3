import styled from "styled-components";
import CenterLogo from "/src/assets/33MA3_logo.png";

const Header = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f8f8fa;
  border-radius: 14px 14px 0px 0px;
  border-bottom: 2px solid ${(props) => props.theme.colors.surface_weak};
  gap: 550px;
`;

const CenterContainer = styled.div`
  display: flex;
  align-items: center;
  gap: 20px;
`;

const Logo = styled.img`
  width: 60px;
  height: 75px;
`;

const CenterInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`;

const SuccessfulBidButton = styled.button`
  color: white;
  width: 70px;
  height: 40px;
  background: ${(props) => props.theme.colors.surface_brand};
  border-radius: 14px;
  font-weight: 500;
`;

const CenterName = styled.div`
  font-size: 30px;
  font-weight: 700;
`;

const CenterStatus = styled.div`
  font-size: 16px;
`;

function ChatHeader(props) {
  return (
    <Header>
      <CenterContainer>
        <Logo src={CenterLogo} />
        <CenterInfo>
          <CenterName>{props.centerName}</CenterName>
          <CenterStatus>부재중</CenterStatus>
        </CenterInfo>
      </CenterContainer>
      <SuccessfulBidButton>낙찰</SuccessfulBidButton>
    </Header>
  );
}

export { ChatHeader };
