import styled from "styled-components";
import SubmitIcon from "/src/assets/chatSubmit.svg";
import { useState } from "react";
import { useLoaderData } from "react-router-dom";
import { BASE_URL } from "../../../constants/url";
import { getMemberId } from "../../../utils/auth";
import { getCurrentTimeFormatted } from "../../../utils/dateTimeHelper";

const InputContainer = styled.div`
  display: flex;
  height: 80px;
  align-items: center;
  justify-content: center;
  border-radius: 0px 0px 14px 14px;
  background: #f8f8f8fa;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  gap: 25px;
`;

const InputText = styled.input`
  width: 700px;
  height: 40px;
  font-size: 20px;
  border-radius: 10px;
  border: none;
  padding: 5px;
`;

const SubmitText = styled.button`
  width: 50px;
  height: 50px;
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
      message: inputValue,
    };
    fetch(`${BASE_URL}chat/${props.roomId}/${props.receiverId}`, {
      method: "POST",
      headers: {
        Authorization: accessToken,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(message),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log(data);
        setInputValue("");
        const newChat = {
          senderId: Number(getMemberId()),
          contents: inputValue,
          createTime: getCurrentTimeFormatted(),
          readDone: false,
        };
        props.updateChat((prev) => [...prev, newChat]);
      })
      .catch((error) => console.log(error));
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

export { ChatInput };
