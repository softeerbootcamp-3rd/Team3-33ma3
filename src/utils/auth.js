import { redirect } from "react-router-dom";

function getAuthToken() {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return null;
  }

  return token;
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

export function tokenLoader() {
  const accessToken = getAuthToken();
  const memberId = getMemberId();
  const memberType = getMemberType();

  return {
    accessToken: accessToken,
    memberId: memberId,
    memberType: memberType,
  };
}

export function checkAuthLoader() {
  const token = getAuthToken();

  if (!token) {
    return redirect("/auth");
  }
}
