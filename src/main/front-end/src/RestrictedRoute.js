import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { getUser } from "./services/UserDataService";

export const RestrictedRoute = ({component: Component, ...rest}) => {
    return (
        <Route {...rest} render={props => (
            getUser() === "" ?
                <Component {...props} />
                : <Redirect to="/" />
        )} />
    );
};