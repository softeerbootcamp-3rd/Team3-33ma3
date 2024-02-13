import styled from "styled-components";
import Logo from "../../assets/33MA3_logo.png";
import NavItem from "./NavItem";
import { CONTENT_MAX_WIDTH } from "../../constants/layouts";

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
    #0e1d4f 0%,
    #515975 50%,
    #878ea5 73%,
    #fff 100%
  );
`;

const HeaderItems = styled.div`
  display: flex;
  width: 100%;
  max-width: ${CONTENT_MAX_WIDTH};
  justify-content: space-between;
`;

function Header() {
  return (
    <HeaderContainer>
      <HeaderItems>
        <img src={Logo} alt="33MA3" style={{ width: "58px", height: "80px" }} />
        <NavItem />
      </HeaderItems>
    </HeaderContainer>
  );
}

export default Header;
