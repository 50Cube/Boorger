import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';
import axios from 'axios';
import Translate from "../i18n/Translate";

export default class Register extends Component {

    emptyUser = {
        "login": "",
        "password": "",
        "confirmPassword": "",
        "firstname": "",
        "lastname": "",
        "email": "",
        "language": localStorage.getItem("lang") ? localStorage.getItem("lang") : navigator.language
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.emptyUser
        }
    }

    handleFieldChanged = (event, field) => {
        const tmp = {...this.state.user};
        tmp[field] = event.target.value;
        this.setState({ user: tmp });
    };

    register = (e) => {
        e.preventDefault();
        axios.post("/register", this.state.user)
            .then(() => {
                this.props.history.push("/")
            }).catch(error => {
                console.log(error)
        })
    };

    render() {
        return (
            <div>
                <form>
                    <FormGroup>
                        <FormLabel>{Translate('username')}</FormLabel>
                        <FormControl autoFocus value={this.state.user["login"]} onChange={event => this.handleFieldChanged(event, "login")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>{Translate('password')}</FormLabel>
                        <FormControl type="password" value={this.state.user["password"]} onChange={event => this.handleFieldChanged(event, "password")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>{Translate('password2')}</FormLabel>
                        <FormControl type="password" value={this.state.user["confirmPassword"]} onChange={event => this.handleFieldChanged(event, "confirmPassword")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>{Translate('firstname')}</FormLabel>
                        <FormControl value={this.state.user["firstname"]} onChange={event => this.handleFieldChanged(event, "firstname")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>{Translate('lastname')}</FormLabel>
                        <FormControl value={this.state.user["lastname"]} onChange={event => this.handleFieldChanged(event, "lastname")} />
                    </FormGroup>

                    <FormGroup>
                        <FormLabel>{Translate('email')}</FormLabel>
                        <FormControl value={this.state.user["email"]} onChange={event => this.handleFieldChanged(event, "email")} />
                    </FormGroup>

                    <Button type="submit" onClick={this.register}>{Translate('confirm')}</Button>
                </form>
            </div>
        )
    }
}
