import React, { useEffect, useState } from "react";

function Timer({ remainTime }) {
  const [leftTime, setLeftTime] = useState(remainTime);
  const hours = Math.floor(leftTime / 60 / 60);
  const minutes = Math.floor(leftTime / 60) % 60;
  const seconds = leftTime % 60;

  useEffect(() => {
    const timer = setInterval(() => {
      setLeftTime(leftTime - 1);
    }, 1000);

    if (leftTime <= 0) {
      clearInterval(timer);
    }

    return () => {
      clearInterval(timer);
    };
  }, [leftTime]);

  function formTime(time) {
    if (time < 10) {
      return "0" + time;
    }
    return time;
  }

  return (
    <>
      {formTime(hours)}:{formTime(minutes)}:{formTime(seconds)}
    </>
  );
}

export default Timer;
