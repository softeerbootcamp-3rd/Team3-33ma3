import ClientImage from "/src/assets/client_type.svg";
import CenterImage from "/src/assets/center_type.svg";
import styled from "styled-components";
import { Link } from "react-router-dom";

const AuthHeader = styled.h1`
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

const AuthLink = styled(Link)`
  text-decoration: none;
  color: ${(props) => props.theme.colors.text_weak};
`;

const TypeContainer = styled.div`
  display: flex;
  flex-direction: row;
  gap: 20px;
  width: 100%;
  padding: 30px;
  justify-content: center;
  align-items: center;
  height: 70vh;
`;

const Type = styled.div`
  display: flex;
  width: 200px;
  height: 200px;
  border-radius: 14px;
  background: #f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 20px;
`;

const Image = styled.img`
  width: 100px;
  height: 80px;
`;

function SettingType() {
  return (
    <TypeContainer>
      <AuthLink to="?mode=signUp&type=client">
        <Type>
          <Image src={ClientImage} />
          사용자 회원가입
        </Type>
      </AuthLink>
      <AuthLink to="?mode=signUp&type=center">
        <Type>
          <Image src={CenterImage} />
          센터 회원가입
        </Type>
      </AuthLink>
    </TypeContainer>
  );
}

export { SettingType };
