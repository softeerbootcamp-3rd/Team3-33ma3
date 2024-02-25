import styled from "styled-components";
import ChatIcon from "/src/assets/chatIcon.svg";

const Header = styled.header`
  display: flex;
  width: ${(props) => (props.chatmode === "true" ? "400px" : "100%")};
  padding: 20px 0px;
  background: ${(props) => props.theme.colors.surface_brand};
  justify-content: center;
  align-items: center;
  border-radius: 14px 14px 0px 0px;
  font-size: ${({ theme }) => theme.fontSize.regular};
  font-weight: 700;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
`;

const Icon = styled.img`
  width: 30px;
  height: 30px;
`;

const IconContainer = styled.div`
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5px;
`;

function MessageHeader(props) {
  return (
    <Header chatmode={props.chatmode}>
      <IconContainer>
        <Icon src={ChatIcon} />
        전체
      </IconContainer>
    </Header>
  );
}

export { MessageHeader };
