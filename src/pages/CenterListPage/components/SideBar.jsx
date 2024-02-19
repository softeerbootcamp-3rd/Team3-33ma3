import React, { useRef } from "react";
import styled from "styled-components";
import SideBarContainer from "../../../components/sidebar/SideBarContainer";
import ToggleButton from "../../../components/button/ToggleButton";
import DropDownButton from "../../../components/button/DropDownButton";
import ChipButton from "../../../components/button/ChipButton";
import {
  REPAIR_SERVICE_OPTIONS,
  TUNEUP_SERVICE_OPTIONS,
} from "../../../constants/options";

const ServiceList = styled.div`
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
  gap: 5px;
`;

function SideBar({ setRepairList, setTuneUpList, setIsDone }) {
  const regionList = useRef([]);
  const repairList = useRef([]);
  const tuneUpList = useRef([]);

  const repairServices = REPAIR_SERVICE_OPTIONS.map((item, index) => (
    <ChipButton
      type={item}
      key={index}
      onClick={() => toggle(repairList, setRepairList, item)}
    />
  ));

  const tuneUpServices = TUNEUP_SERVICE_OPTIONS.map((item, index) => (
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

  return (
    <SideBarContainer title={"검색 조건"}>
      <ToggleButton
        title={"경매 완료 여부"}
        name={"done"}
        setIsDone={setIsDone}
      />
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
