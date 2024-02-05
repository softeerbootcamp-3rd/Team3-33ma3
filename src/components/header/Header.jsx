import styled from "styled-components";
import Logo from "../../assets/33MA3_logo.png";
import NavItem from "./NavItem";

const HeaderContainer = styled.header`
  display: flex;
  width: 100%;
  height: 90px;
  padding: 0px 30px;
  justify-content: space-between;
  align-items: center;
  box-sizing: border-box;
  background: linear-gradient(
    180deg,
    #0e1d4f 0%,
    #515975 50%,
    #878ea5 73%,
    #fff 100%
  );
`;

function Header() {
  return (
    <HeaderContainer>
      <img src={Logo} alt="33MA3" style={{ width: "58px", height: "80px" }} />
      <NavItem />
    </HeaderContainer>
  );
}

export default Header;
