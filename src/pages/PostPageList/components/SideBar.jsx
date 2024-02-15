import React from "react";
import styled from "styled-components";
import SideBarContainer from "../../../components/sidebar/SideBarContainer";
import ToggleButton from "../../../components/button/ToggleButton";
import DropDownButton from "../../../components/button/DropDownButton";
import DropDownItemButton from "../../../components/button/DropDownItemButton";
import ChipButton from "../../../components/button/ChipButton";
import {
  DISTRICTS,
  REPAIR_SERVICE_OPTIONS,
  TUNEUP_SERVICE_OPTIONS,
} from "../../../constants/options";

const ServiceList = styled.div`
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
  gap: 5px;
`;

function SideBar() {
  const districts = DISTRICTS.map((item, index) => (
    <DropDownItemButton content={item} key={index} />
  ));

  const repairServices = REPAIR_SERVICE_OPTIONS.map((item, index) => (
    <ChipButton type={item} key={index} />
  ));
  const tuneUpServices = TUNEUP_SERVICE_OPTIONS.map((item, index) => (
    <ChipButton type={item} key={index} />
  ));

  return (
    <SideBarContainer title={"검색 조건"}>
      <ToggleButton title={"수리 완료 여부"} />
      <DropDownButton title={"지역"}>{districts}</DropDownButton>
      <DropDownButton title={"정비 서비스"}>
        <ServiceList>{repairServices}</ServiceList>
      </DropDownButton>
      <DropDownButton title={"수리 서비스"}>
        <ServiceList>{tuneUpServices}</ServiceList>
      </DropDownButton>
    </SideBarContainer>
  );
}

export default SideBar;
