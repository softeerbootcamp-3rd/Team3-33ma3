import { redirect } from "react-router-dom";

function getAuthToken() {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return null;
  }

  return token;
}

function removeAuthToken() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
}

function getMemberId() {
  const memberId = localStorage.getItem("memberId");
  if (!memberId) {
    return null;
  }
  return memberId;
}

function getMemberType() {
  const memberType = localStorage.getItem("memberType");
  if (!memberType) {
    return null;
  }
  return memberType;
}

function tokenLoader() {
  const accessToken = getAuthToken();
  const memberId = getMemberId();
  const memberType = getMemberType();

  return {
    accessToken: accessToken,
    memberId: memberId,
    memberType: memberType,
  };
}

function checkAuthLoader() {
  const token = getAuthToken();

  if (!token) {
    return redirect("/auth");
  }
}

export { removeAuthToken, getMemberId, tokenLoader, checkAuthLoader };
