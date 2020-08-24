import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';
import axios from 'axios';
import Swal from 'sweetalert2';
import Cookies from "universal-cookie/lib";
import { getFirstAccessLevel, getHeader, hashString } from "../../services/UserDataService";
import Translate from "../../i18n/Translate";
import {Link} from "react-router-dom";
import Spinner from "react-bootstrap/Spinner";
import '../../css/Login.css';

export default class Login extends Component {

    emptyUser = {
        "login": "",
        "password": "",
    };

    constructor(props) {
        super(props);
        this.state = {
            user: this.emptyUser,
            loading: false
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
        this.setState({
            loading: true
        });
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
                }).then(() => this.setState({
                    user: this.emptyUser,
                    loading: false
                }))
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
                        <Link className="forgot" to="/reset">{Translate('forgotPassword')}</Link>
                        <Button className="buttons" type="submit" onClick={this.login}>
                            { this.state.loading ? <Spinner className="spinner" animation="border" /> : Translate('confirm') } </Button>
                    </form>
                </div>
            </div>
        )
    }
}
