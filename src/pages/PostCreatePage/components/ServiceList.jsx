import React from "react";
import styled from "styled-components";
import ChipButton from "../../../components/button/ChipButton";

const ServiceContainer = styled.div`
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
`;

function ServiceList({ optionList, serviceList, onClick }) {
  return (
    <ServiceContainer>
      {optionList.map((item, index) => (
        <ChipButton
          type={item}
          key={index}
          onClick={() => onClick(serviceList, item)}
        />
      ))}
    </ServiceContainer>
  );
}

export default ServiceList;
