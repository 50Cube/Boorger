import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import { Container } from "react-bootstrap";
import Home from "./components/Home";
import NotFound from "./components/NotFound";
import NavigationBar from "./components/NavigationBar";
import Login from "./components/Login";
import Register from "./components/Register";

export default class App extends Component {

    render() {
        return (
            <React.Fragment>
                <NavigationBar />
                <Router>
                    <Container>
                        <Switch>
                            <Route exact path="/" component={Home} />
                            <Route path="/login" component={Login} />
                            <Route path="/register" component={Register} />
                            <Route component={NotFound} />
                        </Switch>
                    </Container>
                </Router>
            </React.Fragment>
        );
    }
}
