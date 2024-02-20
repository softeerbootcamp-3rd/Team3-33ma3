import styled from "styled-components";
import SubmitIcon from "/src/assets/chatSubmit.svg";
import { useState } from "react";

function ChatInput() {
  const [inputValue, setInputValue] = useState("");

  const handleInputChange = (event) => {
    setInputValue(event.target.value);
  };

  const handleSubmit = () => {
    setInputValue("");
  };

  return (
    <InputContainer>
      <InputText
        value={inputValue}
        onChange={handleInputChange}
        placeholder="메시지를 입력하세요"
      />
      <SubmitText onClick={handleSubmit}>
        <img src={SubmitIcon} alt="Submit" />
      </SubmitText>
    </InputContainer>
  );
}

const InputContainer = styled.div`
  display: flex;
  height: 80px;
  align-items: center;
  justify-content: center;
  border: 1px solid black;
  border-radius: 14px;
  background: white;
  gap: 30px;
`;

const InputText = styled.input`
  width: 700px;
  height: 40px;
  font-size: 20px;
`;

const SubmitText = styled.button`
  width: 50px;
  height: 50px;
  border: 1px solid black;
`;

export { ChatInput };
