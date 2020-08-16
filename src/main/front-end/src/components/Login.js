import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';
import axios from 'axios';
import Swal from 'sweetalert2';
import Cookies from "universal-cookie/lib";
import {getFirstAccessLevel, getHeader, hashString} from "../services/UserDataService";
import Translate from "../i18n/Translate";
import '../css/Login.css';

export default class Login extends Component {

    emptyUser = {
        "login": "",
        "password": "",
        "language": navigator.language
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.emptyUser
        };
        this.cookies = new Cookies();
    }

    handleFieldChanged = (event, field) => {
        const tmp = {...this.state.user};
        tmp[field] = event.target.value;
        this.setState({ user: tmp });
    };

    login = (e) => {
        e.preventDefault();
        axios.post("/login", this.state.user, { headers: getHeader() })
            .then(response => {
                if(response.data.token) {
                    this.cookies.set("jwt", response.data.token, { path: "/" });
                    localStorage.setItem("swal1", response.data.messages[0]);
                    localStorage.setItem("swal2", response.data.messages[1]);
                    localStorage.setItem("lang", response.data.language);
                    sessionStorage.setItem("role", hashString(getFirstAccessLevel()));
                    this.props.history.push("/");
                    window.location.reload();
                }
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                }).then(() => this.setState({ user: this.emptyUser }))
        })
    };


    render() {
        return (
            <div className="loginImage">
                <div className="loginMain">
                    <form>
                        <FormGroup className="labels">
                            <FormLabel>{Translate('username')}</FormLabel>
                            <FormControl autoFocus value={this.state.user["login"]} onChange={event => this.handleFieldChanged(event, "login")} />
                        </FormGroup>
                        <FormGroup className="labels">
                            <FormLabel>{Translate('password')}</FormLabel>
                            <FormControl type="password" value={this.state.user["password"]} onChange={event => this.handleFieldChanged(event, "password")} />
                        </FormGroup>
                        <Button type="submit" onClick={this.login}>{Translate('confirm')}</Button>
                    </form>
                </div>
            </div>
        )
    }
}
