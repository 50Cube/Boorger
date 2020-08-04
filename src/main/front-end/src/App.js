import React, { Component } from "react";
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


export default class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            role: getFirstAccessLevel()
        }
    }

    roleCallback = (newRole) => {
        this.setState({
            role: newRole
        });
    };

    render() {
        return (
            <React.Fragment>
                <NavigationBar callbackFromParent={this.roleCallback} role={this.state.role} />
                <Router>
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
            </React.Fragment>
        );
    }
}
