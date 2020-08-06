import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import {getCurrentAccessLevel, getUser} from "./services/UserDataService";

export const PrivateRoute = ({component: Component, accessLevels, ...rest}) => {
  return (
      <Route {...rest} render={props => (
          getUser() !== "" && accessLevels.includes(getCurrentAccessLevel()) ?
              <Component {...props} />
              : <Redirect to="/accessDenied" />
      )} />
  );
};