import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';

export default class Login extends Component {

    render() {
        return (
            <div>
                <form>
                    <FormGroup>
                        <FormLabel>Login</FormLabel>
                        <FormControl autoFocus />
                    </FormGroup>
                    <FormGroup>
                        <FormLabel>Password</FormLabel>
                        <FormControl type="password" />
                    </FormGroup>
                    <Button block type="submit">Login</Button>
                </form>
            </div>
        )
    }
}
