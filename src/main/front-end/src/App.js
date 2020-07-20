import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import { Container } from "react-bootstrap";
import Home from "./components/Home";


export default class App extends Component {

    render() {
        return (
            <React.Fragment>
                <Router basename="app">
                    <Container>
                        <Switch>
                            <Route exact path="/" component={Home}/>
                        </Switch>
                    </Container>
                </Router>
            </React.Fragment>
        );
    }
}
