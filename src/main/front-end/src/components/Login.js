import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                "login": "",
                "password": ""
            }
        }
    }

    handleFieldChanged = (event, field) => {
        const tmp = {...this.state.user};
        tmp[field] = event.target.value;
        this.setState({ user: tmp });
    };


    render() {
        return (
            <div>
                <form>
                    <FormGroup>
                        <FormLabel>Login</FormLabel>
                        <FormControl autoFocus value={this.state.user["login"]} onChange={event => this.handleFieldChanged(event, "login")} />
                    </FormGroup>
                    <FormGroup>
                        <FormLabel>Password</FormLabel>
                        <FormControl type="password" value={this.state.user["password"]} onChange={event => this.handleFieldChanged(event, "password")} />
                    </FormGroup>
                    <Button type="submit">Login</Button>
                </form>
            </div>
        )
    }
}
