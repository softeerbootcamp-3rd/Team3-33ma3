import React from "react";
import styled from "styled-components";

const Container = styled.div`
  text-align: center;
  overflow: hidden;
  padding-top: 40px;

  .box {
    display: inline-block;
    height: 200px;
    width: 100%;
    float: left;
    position: relative;
    transition: all 0.2s ease;
  }
`;

const LoderContainer = styled.div`
  .loader10:before {
    content: "";
    position: absolute;
    top: 0px;
    left: -25px;
    height: 12px;
    width: 12px;
    border-radius: 12px;
    -webkit-animation: loader10g 3s ease-in-out infinite;
    animation: loader10g 3s ease-in-out infinite;
  }

  .loader10 {
    position: relative;
    width: 12px;
    height: 12px;
    top: 46%;
    left: 46%;
    border-radius: 12px;
    -webkit-animation: loader10m 3s ease-in-out infinite;
    animation: loader10m 3s ease-in-out infinite;
  }

  .loader10:after {
    content: "";
    position: absolute;
    top: 0px;
    left: 25px;
    height: 10px;
    width: 10px;
    border-radius: 10px;
    -webkit-animation: loader10d 3s ease-in-out infinite;
    animation: loader10d 3s ease-in-out infinite;
  }
  @-webkit-keyframes loader10g {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 1);
    }
    50% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    75% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }
  @keyframes loader10g {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 1);
    }
    50% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    75% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }

  @-webkit-keyframes loader10m {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    50% {
      background-color: rgba(128, 128, 128, 1);
    }
    75% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }
  @keyframes loader10m {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    50% {
      background-color: rgba(128, 128, 128, 1);
    }
    75% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }

  @-webkit-keyframes loader10d {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    50% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    75% {
      background-color: rgba(128, 128, 128, 1);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }
  @keyframes loader10d {
    0% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    25% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    50% {
      background-color: rgba(128, 128, 128, 0.2);
    }
    75% {
      background-color: rgba(128, 128, 128, 1);
    }
    100% {
      background-color: rgba(128, 128, 128, 0.2);
    }
  }
`;

function Loading() {
  return (
    <Container>
      <div className="box">
        <LoderContainer>
          <div className="loader10"></div>
        </LoderContainer>
      </div>
    </Container>
  );
}

export default Loading;
