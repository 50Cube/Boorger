export const getAuthHeader = () => {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user != null) {
        return { Authorization: 'Bearer ' + user.token};
    } else {
        return {};
    }
};

export const getUser = () => {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user != null) {
        return user.login;
    } else {
        return "";
    }
};

export const getAccessLevels = () => {
    let user = JSON.parse(localStorage.getItem('user'));
    if(user != null && user.accessLevels.length > 0) {
        return user.accessLevels;
    } else {
        return "";
    }
};

export const getFirstAccessLevel = () => {
    if(getAccessLevels() !== "")
        return getAccessLevels()[getAccessLevels().length - 1];
};