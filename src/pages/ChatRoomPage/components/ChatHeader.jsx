import styled from "styled-components";
import ChatIcon from "/src/assets/chatIcon.svg";

const Header = styled.header`
  display: flex;
  width: 1000px;
  height: 120px;
  background: ${(props) => props.theme.colors.surface_brand};
  justify-content: center;
  align-items: center;
  border-radius: 14px 14px 0px 0px;
`;

const Icon = styled.img`
  width: 40px;
  height: 40px;
`;

const IconContainer = styled.div`
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
`;

function ChatHeader() {
  return (
    <Header>
      <IconContainer>
        <Icon src={ChatIcon} />
        전체
      </IconContainer>
    </Header>
  );
}

export { ChatHeader };
