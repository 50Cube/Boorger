import React from 'react';
import {getFirstAccessLevel} from "./UserDataService";

const RoleContext = React.createContext({
    role: getFirstAccessLevel(),
    setRole: () => {}
});

export default RoleContext;