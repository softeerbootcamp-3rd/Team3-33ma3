import { useState } from "react";
import { searchAddressToCoordinate } from "../../../utils/locationUtils";
import InputText from "../../../components/input/InputText";
import generateKeyBasedOnCurrentTime from "../../../utils/generateKeyBasedOnCurrentTime";

function CenterAuthForm() {
  const [coords, setCoords] = useState({});
  const [centerAddress, setCenterAddress] = useState("");
  const [checkAddress, setCheckAddress] = useState(false);
  const [autoCompleteKey, setAutoCompleteKey] = useState(null);
  const [autoCompleteAddress, setAutoCompleteAddress] = useState("");

  function handleInputCenterName(e) {
    searchAddressToCoordinate(e.target.value)
      .then((res) => {
        if (res !== null) {
          setCenterAddress(res.address.roadAddress);
          setCheckAddress(true);
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

  function handleAutoComplete(e) {
    setAutoCompleteAddress(e.target.innerHTML);
    setCheckAddress(false);
    setAutoCompleteKey(generateKeyBasedOnCurrentTime());
  }

  return (
    <>
      <input type="hidden" name="latitude" value={coords.latitude} />
      <input type="hidden" name="longitude" value={coords.longitude} />
      <InputText
        id="centerName"
        type="text"
        name="centerName"
        placeholder="센터이름"
        size="small"
        required
      />
      <InputText
        id="address"
        type="text"
        name="address"
        onChange={handleInputCenterName}
        placeholder="위치"
        size="small"
        key={autoCompleteKey}
        defaultValue={autoCompleteAddress}
        required
      />
      {checkAddress && <div onClick={handleAutoComplete}>{centerAddress}</div>}
    </>
  );
}

export { CenterAuthForm };
