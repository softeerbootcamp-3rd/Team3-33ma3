// 날짜 기반 임의 키 생성 함수
function generateKeyBasedOnCurrentTime() {
  const currentTime = new Date().getTime();
  const key = `key_${currentTime}`;
  return key;
}

export default generateKeyBasedOnCurrentTime;
