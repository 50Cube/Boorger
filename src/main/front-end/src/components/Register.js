import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';
import axios from 'axios';

export default class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                "login": "",
                "password": "",
                "confirmPassword": "",
                "firstname": "",
                "lastname": "",
                "email": "",
                "language": "pl"
            }
        }
    }

    handleFieldChanged = (event, field) => {
        const tmp = {...this.state.user};
        tmp[field] = event.target.value;
        this.setState({ user: tmp });
    };

    register = () => {
        axios.post("/register", this.state.user)
            .then(response => {
                this.props.history.push("/");
            }).catch(error => {

        })
    };

    render() {
        return (
            <div>
                <form>
                    <FormGroup>
                        <FormLabel>Nazwa uzytkownika</FormLabel>
                        <FormControl autoFocus value={this.state.user["login"]} onChange={event => this.handleFieldChanged(event, "login")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>Password</FormLabel>
                        <FormControl type="password" value={this.state.user["password"]} onChange={event => this.handleFieldChanged(event, "password")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>Password2</FormLabel>
                        <FormControl type="password" value={this.state.user["confirmPassword"]} onChange={event => this.handleFieldChanged(event, "confirmPassword")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>Firstname</FormLabel>
                        <FormControl value={this.state.user["firstname"]} onChange={event => this.handleFieldChanged(event, "firstname")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>LastName</FormLabel>
                        <FormControl value={this.state.user["lastname"]} onChange={event => this.handleFieldChanged(event, "lastname")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>Email</FormLabel>
                        <FormControl value={this.state.user["email"]} onChange={event => this.handleFieldChanged(event, "email")} />
                    </FormGroup>

                    <Button type="submit" onClick={this.register}>Register</Button>
                </form>
            </div>
        )
    }
}
