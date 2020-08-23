import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../services/UserDataService";
import Swal from "sweetalert2";
import Translate from '../i18n/Translate';
import {FormGroup, FormControl, Button} from "react-bootstrap";
import ValidationMessage from "../i18n/ValidationMessage";
import Spinner from "react-bootstrap/Spinner";
import '../css/ResetPassword.css';

export default class ResetPassword extends Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '', emailValid: false,
            errorMsg: {},
            loading: false
        }
    }

    updateEmail = (email) => {
        this.setState({email}, this.validateEmail)
    };

    validateEmail = () => {
        const { email } = this.state;
        let emailValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(email.length < 1) {
            emailValid = false;
            errorMsg.email = 'field-required';
        } else if(!/^([a-zA-Z0-9-_]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,})$/.test(email)) {
            emailValid = false;
            errorMsg.email = 'email-pattern';
        }
        else if(email.length > 32) {
            emailValid = false;
            errorMsg.email = 'field-toolong'
        }
        this.setState({emailValid, errorMsg})
    };

    resetPassword = (e) => {
        e.preventDefault();
        this.setState({
            loading: true
        });
        axios.post("/reset/" + this.state.email, null, { headers: getHeader() })
            .then(response => {
                Swal.fire({
                    icon: "success",
                    title: response.data
                }).then(() => this.props.history.push("/"));
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            }).then(() => {
                this.setState({ loading: false });
            })
        })
    };

    render() {
        return (
            <div className="resetImage">
                <div className="resetMain">
                    <h1>{Translate('provideMail')}</h1>
                    <form>
                        <FormGroup>
                            <FormControl autoFocus value={this.state.email} onChange={event => this.updateEmail(event.target.value)}/>
                            <ValidationMessage valid={this.state.emailValid} message={this.state.errorMsg.email} />
                        </FormGroup>
                        <Button className="buttons" type="submit" disabled={!this.state.emailValid} onClick={this.resetPassword}>
                            { this.state.loading ? <Spinner className="spinner" animation="border" /> : Translate('confirm') }</Button>
                    </form>
                </div>
            </div>
        )
    }
}
