import styled from "styled-components";
import SubmitIcon from "/src/assets/chatSubmit.svg";
import { useState } from "react";
import { useLoaderData } from "react-router-dom";

const InputContainer = styled.div`
  display: flex;
  height: 60px;
  width: 100%;
  padding: 10px;
  align-items: center;
  justify-content: center;
  border-radius: 0px 0px 14px 14px;
  background: #f8f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  box-sizing: border-box;
  gap: 15px;
`;

const InputText = styled.input`
  width: 100%;
  height: 35px;
  font-size: ${({ theme }) => theme.fontSize.regular};
  font-weight: 500;
  border-radius: 10px;
  border: none;
  padding: 5px;
`;

const SubmitText = styled.button`
  padding: 10px;
`;

function ChatInput(props) {
  const [inputValue, setInputValue] = useState("");

  const authData = useLoaderData();
  const accessToken = authData.accessToken;

  const handleInputChange = (event) => {
    setInputValue(event.target.value);
  };

  const handleSubmit = () => {
    const message = {
      roomId: props.roomId,
      senderId: props.senderId,
      receiverId: props.receiverId,
      message: inputValue,
    };
    props.webSocket.send(JSON.stringify(message));
  };

  return (
    <InputContainer>
      <InputText
        value={inputValue}
        onChange={handleInputChange}
        placeholder="메시지를 입력하세요"
      />
      <SubmitText onClick={handleSubmit}>
        <img
          src={SubmitIcon}
          alt="Submit"
          style={{ width: "26px", height: "24px" }}
        />
      </SubmitText>
    </InputContainer>
  );
}

export { ChatInput };
