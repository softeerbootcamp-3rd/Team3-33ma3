function getCurrentTimeFormatted() {
  const now = new Date();
  const hours = now.getHours();
  const minutes = now.getMinutes();
  const isAM = hours < 12;
  const formattedHours = isAM ? hours : hours - 12;
  const formattedMinutes = minutes < 10 ? `0${minutes}` : minutes;
  return `${isAM ? "오전" : "오후"} ${formattedHours}:${formattedMinutes}`;
}

export { getCurrentTimeFormatted };
