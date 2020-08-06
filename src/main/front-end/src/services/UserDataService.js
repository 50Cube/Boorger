import Cookies from "universal-cookie/lib";
import {LOCALES} from "../i18n/Locales";

const cookies = new Cookies();
let token = require("jsonwebtoken");

export const getAuthHeader = () => {
    let jwt = cookies.get("jwt");
    if(jwt != null) {
        return { Authorization: 'Bearer ' + jwt};
    } else {
        return {};
    }
};

export const getUser = () => {
    let jwt = cookies.get("jwt");
    if(jwt != null) {
        return token.decode(jwt)["sub"];
    } else {
        return "";
    }
};

export const getAccessLevels = () => {
    let jwt = cookies.get("jwt");
    if(jwt != null) {
        return token.decode(jwt)["roles"];
    } else {
        return "";
    }
};

export const getFirstAccessLevel = () => {
    let roles = getAccessLevels();
    if(roles) {
        if(roles.includes(process.env.REACT_APP_ADMIN_ROLE)) return process.env.REACT_APP_ADMIN_ROLE;
        else if(roles.includes(process.env.REACT_APP_MANAGER_ROLE)) return process.env.REACT_APP_MANAGER_ROLE;
        else if(roles.includes(process.env.REACT_APP_CLIENT_ROLE)) return process.env.REACT_APP_CLIENT_ROLE;
    }
};

export const getCurrentAccessLevel = () => {
  let role = sessionStorage.getItem("role");
  if(hashString(process.env.REACT_APP_ADMIN_ROLE) == role) return process.env.REACT_APP_ADMIN_ROLE;
  else if(hashString(process.env.REACT_APP_MANAGER_ROLE) == role) return process.env.REACT_APP_MANAGER_ROLE;
  else if(hashString(process.env.REACT_APP_CLIENT_ROLE) == role) return process.env.REACT_APP_CLIENT_ROLE;
};

export const getLanguage = () => {
  if(localStorage.getItem("lang") === 'en')
      return LOCALES.ENGLISH;
  else return LOCALES.POLISH;
};

export const hashString = (string) => {
    const stringHash = require("string-hash");
    return stringHash(string);
};