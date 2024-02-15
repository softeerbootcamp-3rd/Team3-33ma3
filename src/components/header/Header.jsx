import styled from "styled-components";
import Logo from "../../assets/33MA3_logo.png";
import NavItem from "./NavItem";
import { CONTENT_MAX_WIDTH } from "../../constants/layouts";
import { Link } from "react-router-dom";
import Alarm from "../../assets/alarm.png";
import AuthenticationModal from "../../pages/AuthenticationPage/AuthenticationPage";
import { useRef } from "react";

const HeaderContainer = styled.header`
  display: flex;
  width: 100%;
  height: 90px;
  padding: 0px 30px;
  align-items: center;
  justify-content: center;
  box-sizing: border-box;
  background: linear-gradient(
    180deg,
    #161e3b 0%,
    #212840 23.5%,
    #2b303e 50%,
    #676c7e 73%,
    #fff 100%
  );
`;

const HeaderItems = styled.div`
  display: flex;
  width: 100%;
  max-width: ${CONTENT_MAX_WIDTH};
  justify-content: space-between;
`;

const LoginButton = styled.button`
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

const NavItems = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
`;

function Header() {
  const modalRef = useRef();
  // Header login Button
  function handleModal() {
    modalRef.current.open();
  }

  return (
    <>
      <AuthenticationModal ref={modalRef} />
      <HeaderContainer>
        <HeaderItems>
          <Link to={"/"}>
            <img
              src={Logo}
              alt="33MA3"
              style={{ width: "58px", height: "80px" }}
            />
          </Link>
          <NavItems>
            <NavItem />
            <LoginButton onClick={handleModal}>로그인</LoginButton>
            <img src={Alarm} style={{ width: "30px", height: "30px" }} />
          </NavItems>
        </HeaderItems>
      </HeaderContainer>
    </>
  );
}

export default Header;
