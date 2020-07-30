import React, { Component } from "react";
import { Button, FormGroup, FormControl, FormLabel } from 'react-bootstrap';
import axios from 'axios';
import Swal from 'sweetalert2';

export default class Login extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                "login": "",
                "password": "",
                "language": "pl"
            }
        }
    }

    handleFieldChanged = (event, field) => {
        const tmp = {...this.state.user};
        tmp[field] = event.target.value;
        this.setState({ user: tmp });
    };

    reload = () => {
      window.location.reload();
    };

    login = (e) => {
        e.preventDefault();
        axios.post("/login", this.state.user)
            .then(response => {
                if(response.data.token) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                    localStorage.setItem("swal1", response.data.messages[0]);
                    localStorage.setItem("swal2", response.data.messages[1]);
                    this.props.history.push("/");
                    window.location.reload();
                }
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                }).then(() => this.setState({ user: {
                        "login": "",
                        "password": "",
                        "language": "pl"
                    } }))
        })
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
                    <Button type="submit" onClick={this.login}>Login</Button>
                </form>
            </div>
        )
    }
}
