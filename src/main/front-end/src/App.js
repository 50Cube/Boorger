import React, {Component, Fragment} from "react";
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import { Container } from "react-bootstrap";
import { PrivateRoute } from './PrivateRoute';
import { RestrictedRoute } from "./RestrictedRoute";
import Home from "./components/Home";
import NotFound from "./components/NotFound";
import NavigationBar from "./components/NavigationBar";
import Login from "./components/Login";
import Register from "./components/Register";
import AccessDenied from "./components/AccessDenied";
import ListAccounts from "./components/ListAccounts";
import {getFirstAccessLevel} from "./services/UserDataService";
import { I18nProvider, LOCALES } from "./i18n";
import RoleContext from "./services/RoleContext";

export default class App extends Component {

    setRole = (role) => {
      this.setState({ role })
    };

    state = {
        role: getFirstAccessLevel(),
        setRole: this.setRole
    };

    render() {
        return (
            <RoleContext.Provider value={this.state}>
                <I18nProvider locale={LOCALES.POLISH}>
                    <Fragment>
                        <Router>
                            <NavigationBar />
                            <Container>
                                <Switch>
                                    <Route exact path="/" component={Home} />
                                    <RestrictedRoute path="/login" component={Login} />
                                    <RestrictedRoute path="/register" component={Register} />

                                    <PrivateRoute path="/listAccounts" component={ListAccounts} accessLevels={[process.env.REACT_APP_ADMIN_ROLE]} />

                                    <Route path="/accessDenied" component={AccessDenied} />
                                    <Route component={NotFound} />
                                </Switch>
                            </Container>
                        </Router>
                    </Fragment>
                </I18nProvider>
            </RoleContext.Provider>
        );
    }
};
