import Cookies from "universal-cookie/lib";

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