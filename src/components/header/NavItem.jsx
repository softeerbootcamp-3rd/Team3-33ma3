import React from "react";
import styled from "styled-components";
import Alarm from "../../assets/alarm.png";
import { Link } from "react-router-dom";

const NavList = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
`;

const NavTitle = styled.a`
  justify-content: center;
  align-items: center;
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

function NavItem() {
  const navItemList = [
    { title: "견적 문의", link: "/post/create" },
    { title: "센터 후기", link: "/center-review/info" },
  ];

  const navItems = navItemList.map((navItem, index) => (
    <Link to={navItem.link} key={index}>
      <NavTitle>{navItem.title}</NavTitle>
    </Link>
  ));

  return (
    <NavList>
      {navItems}
      <img src={Alarm} style={{ width: "30px", height: "30px" }} />
    </NavList>
  );
}

export default NavItem;
