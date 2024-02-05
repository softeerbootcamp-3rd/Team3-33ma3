import React from "react";
import styled from "styled-components";
import Alarm from "../../assets/alarm.png";

const NavList = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
`;

const Link = styled.a`
  justify-content: center;
  align-items: center;
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

function NavItem() {
  const navItemList = ["견적 문의", "센터 후기", "로그인"];

  const navItems = navItemList.map((navItem, index) => (
    <Link key={index}>{navItem}</Link>
  ));

  return (
    <NavList>
      {navItems}
      <img src={Alarm} style={{ width: "30px", height: "30px" }} />
    </NavList>
  );
}

export default NavItem;
