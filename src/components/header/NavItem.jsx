import React from "react";
import styled from "styled-components";
import { Link, useRouteLoaderData } from "react-router-dom";
import { MEMBER_TYPE } from "../../constants/options";

const NavList = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
`;

const NavTitle = styled.p`
  justify-content: center;
  align-items: center;
  color: ${(props) => props.theme.colors.text_white_default};
  font-size: ${(props) => props.theme.fontSize.medium};
  font-weight: 700;
`;

function NavItem() {
  const { memberType, accessToken } = useRouteLoaderData("root");

  const navItemList = [
    MEMBER_TYPE === Number(memberType) && {
      title: "견적 문의",
      link: "/post/create",
    },
    accessToken !== null && {
      title: "상담 내역",
      link: "/chat-room?mode=message",
    },
    { title: "센터 후기", link: "/center-review/list" },
  ];

  const navItems = navItemList.map((navItem, index) => (
    <Link to={navItem.link} key={index}>
      <NavTitle>{navItem.title}</NavTitle>
    </Link>
  ));

  return <NavList>{navItems}</NavList>;
}

export default NavItem;
