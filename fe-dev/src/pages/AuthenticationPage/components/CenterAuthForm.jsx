import { useState } from "react";
import { searchAddressToCoordinate } from "../../../utils/locationUtils";
import InputText from "../../../components/input/InputText";
import generateKeyBasedOnCurrentTime from "../../../utils/generateKeyBasedOnCurrentTime";
import styled from "styled-components";
import SignUpModal from "./SignUpModal";

const InputAddress = styled.div`
  width: 100%;
  font-size: 15px;
  font-weight: 500;
  padding: 10px;
  border: none;
  border-radius: 10px;
  box-sizing: border-box;
  text-align: left;
  background: #eeeeee;
  color: ${(props) => props.theme.colors.text_weak};
`;

function CenterAuthForm() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [address, setAddress] = useState("위치");
  const [coords, setCoords] = useState({});
  console.log(address, coords);
  function handleSaveAddress(address) {
    setAddress(address);
    searchAddressToCoordinate(address)
      .then((res) => {
        if (res !== null) {
          setCoords((prev) => ({
            ...prev,
            latitude: res.point.y,
            longitude: res.point.x,
          }));
        }
      })
      .catch((error) => {
        console.error(error); // 오류 발생 시 처리
      });
  }

  return (
    <>
      {isModalOpen && (
        <SignUpModal
          onSave={handleSaveAddress}
          handleClose={() => setIsModalOpen(false)}
        />
      )}
      <input type="hidden" name="latitude" value={coords.latitude} />
      <input type="hidden" name="longitude" value={coords.longitude} />
      <InputAddress onClick={() => setIsModalOpen(true)}>
        {address}
      </InputAddress>
    </>
  );
}

export { CenterAuthForm };
