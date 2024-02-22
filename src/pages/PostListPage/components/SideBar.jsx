import React, { useRef } from "react";
import styled from "styled-components";
import SideBarContainer from "../../../components/sidebar/SideBarContainer";
import ToggleButton from "../../../components/button/ToggleButton";
import DropDownButton from "../../../components/button/DropDownButton";
import DropDownItemButton from "../../../components/button/DropDownItemButton";
import ChipButton from "../../../components/button/ChipButton";
import {
  DISTRICTS,
  MEMBER_TYPE,
  REPAIR_SERVICE_OPTIONS,
  TUNEUP_SERVICE_OPTIONS,
} from "../../../constants/options";
import { useRouteLoaderData } from "react-router-dom";

const ServiceList = styled.div`
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
  gap: 5px;
`;

function SideBar({
  setIsMine,
  setIsDone,
  setRegionList,
  setRepairList,
  setTuneUpList,
}) {
  const regionList = useRef([]);
  const repairList = useRef([]);
  const tuneUpList = useRef([]);
  const { memberType } = useRouteLoaderData("root");

  const districts = DISTRICTS.map((item, index) => (
    <DropDownItemButton
      content={item}
      key={index}
      onClick={() => toggleBefore3(regionList, setRegionList, item)}
    />
  ));

  const repairServices = REPAIR_SERVICE_OPTIONS.filter(
    (item) => item !== "기타"
  ).map((item, index) => (
    <ChipButton
      type={item}
      key={index}
      onClick={() => toggle(repairList, setRepairList, item)}
    />
  ));

  const tuneUpServices = TUNEUP_SERVICE_OPTIONS.filter(
    (item) => item !== "기타"
  ).map((item, index) => (
    <ChipButton
      type={item}
      key={index}
      onClick={() => toggle(tuneUpList, setTuneUpList, item)}
    />
  ));

  function toggle(ref, setState, value) {
    if (ref.current.includes(value)) {
      ref.current = ref.current.filter((item) => item !== value);
    } else {
      ref.current = [...ref.current, value];
    }
    setState(ref.current);
  }

  function toggleBefore3(ref, setState, value) {
    if (ref.current.includes(value)) {
      ref.current = ref.current.filter((item) => item !== value);
    } else if (ref.current.length < 3) {
      ref.current = [...ref.current, value];
    } else {
      alert("지역은 최대 3개까지 선택 가능합니다.");
      return false;
    }
    setState(ref.current);
    return true;
  }

  return (
    <SideBarContainer title={"검색 조건"}>
      {Number(memberType) === MEMBER_TYPE && (
        <ToggleButton
          title={"내가 작성한 게시글"}
          name={"mine"}
          setIsDone={setIsMine}
        />
      )}
      <ToggleButton
        title={"경매 완료 여부"}
        name={"done"}
        setIsDone={setIsDone}
      />
      {(!memberType || Number(memberType) === MEMBER_TYPE) && (
        <DropDownButton title={"지역"} number={DISTRICTS.length}>
          {districts}
        </DropDownButton>
      )}
      <DropDownButton
        title={"정비 서비스"}
        number={REPAIR_SERVICE_OPTIONS.length}
      >
        <ServiceList>{repairServices}</ServiceList>
      </DropDownButton>
      <DropDownButton
        title={"수리 서비스"}
        number={TUNEUP_SERVICE_OPTIONS.length}
      >
        <ServiceList>{tuneUpServices}</ServiceList>
      </DropDownButton>
    </SideBarContainer>
  );
}

export default SideBar;
