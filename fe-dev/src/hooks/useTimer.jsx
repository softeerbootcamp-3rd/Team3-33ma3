import { useState, useEffect } from "react";

const SECONDS_PER_MINUTE = 60;
const SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
const MILLISECONDS_PER_SECOND = 1000;

function useTimer() {
  const [leftTime, setLeftTime] = useState(0);
  const hours = Math.floor(leftTime / SECONDS_PER_HOUR);
  const minutes =
    Math.floor(leftTime / SECONDS_PER_MINUTE) % SECONDS_PER_MINUTE;
  const seconds = leftTime % SECONDS_PER_MINUTE;

  // 초기 남은 시간 계산
  useEffect(() => {
    const curTime = new Date();
    const midnightTime = new Date(curTime);
    midnightTime.setHours(24);
    midnightTime.setMinutes(0);
    midnightTime.setSeconds(0);
    midnightTime.setMilliseconds(0);
    setLeftTime(Math.floor((midnightTime - curTime) / MILLISECONDS_PER_SECOND));
  }, []);

  useEffect(() => {
    const timer = setInterval(() => {
      setLeftTime(leftTime - 1);
    }, MILLISECONDS_PER_SECOND);

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
