import { useState, useEffect } from "react";

function useTimer() {
  const [leftTime, setLeftTime] = useState(0);
  const hours = Math.floor(leftTime / 60 / 60);
  const minutes = Math.floor(leftTime / 60) % 60;
  const seconds = leftTime % 60;

  // 초기 남은 시간 계산
  useEffect(() => {
    const curTime = new Date();
    const midnightTime = new Date(curTime);
    midnightTime.setHours(24);
    midnightTime.setMinutes(0);
    midnightTime.setSeconds(0);
    midnightTime.setMilliseconds(0);
    setLeftTime(Math.floor((midnightTime - curTime) / 1000));
  }, []);

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
    return time < 10 ? "0" + time : time;
  }

  return `${formTime(hours)}:${formTime(minutes)}:${formTime(seconds)}`;
}

export default useTimer;
