import { redirect } from "react-router-dom";

function getAuthToken() {
  const token = localStorage.getItem("accessToken");

  if (!token) {
    return null;
  }

  return token;
}

export function tokenLoader() {
  const token = getAuthToken();
  return token;
}

export function checkAuthLoader() {
  const token = getAuthToken();

  if (!token) {
    return redirect("/auth");
  }
}
