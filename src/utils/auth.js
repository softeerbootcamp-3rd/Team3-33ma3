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

function getId() {
  const id = localStorage.getItem("id");
  if (!id) {
    return null;
  }
  return id;
}

function tokenLoader() {
  const accessToken = getAuthToken();
  const id = getId();

  return { accessToken: accessToken, id: id };
}

function checkAuthLoader() {
  const token = getAuthToken();

  if (!token) {
    return redirect("/auth");
  }
}

export { removeAuthToken, getId, tokenLoader, checkAuthLoader };
