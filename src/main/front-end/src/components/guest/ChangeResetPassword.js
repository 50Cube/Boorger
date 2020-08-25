import React, { Component } from "react";
import axios from 'axios';
import {getHeader, getLanguage, getLanguageShortcut} from "../../services/UserDataService";
import Swal from "sweetalert2";
import {Button, FormControl, FormGroup, FormLabel} from "react-bootstrap";
import {RiLockPasswordLine} from "react-icons/all";
import Translate from "../../i18n/Translate";
import ValidationMessage from "../../i18n/ValidationMessage";
import Spinner from "react-bootstrap/Spinner";
import Reaptcha from "reaptcha";
import ValidationService from "../../services/ValidationService";
import '../../css/ChangeResetPassword.css';

export default class ChangeResetPassword extends Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '', passwordValid: false,
            confirmPassword: '', confirmPasswordValid: false,
            captcha: '', captchaValid: false,
            formValid: false,
            errorMsg: {},
            token: window.location.href.split('='),
            loading: false
        }
    }

    validateForm = () => {
        const { passwordValid, confirmPasswordValid, captchaValid } = this.state;
        this.setState({
            formValid: passwordValid && confirmPasswordValid && captchaValid
        })
    };

    updatePassword = (password) => {
        this.setState({password}, ValidationService.validatePassword)
    };

    updateConfirmPassword = (confirmPassword) => {
        this.setState({confirmPassword}, ValidationService.validateConfirmPassword)
    };

    onCaptchaVerify = (response) => {
        this.setState({
            captchaValid: true,
            captcha: response
        }, this.validateForm)
    };

    change = (e) => {
        e.preventDefault();
        this.setState({
            loading: true
        });
        axios.post("/changeResetPassword/" + this.state.token[1] + "/" + this.state.captcha,
            {
                password: this.state.password,
                language: getLanguageShortcut()
        }, { headers: getHeader() })
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
                this.captcha.reset();
            })
        })
    };

    render() {
        return (
            <div className="changeImage">
                <div className="changeMain">
                    <h1>{Translate('changePassword')}</h1>
                    <form>
                        <FormGroup className="labels">
                            <RiLockPasswordLine className="icons"/>
                            <FormLabel>{Translate('newPassword')} *</FormLabel>
                            <FormControl type="password" value={this.state.password} onChange={event => this.updatePassword(event.target.value)} />
                            <ValidationMessage valid={this.state.passwordValid} message={this.state.errorMsg.password} />
                        </FormGroup>

                        <FormGroup className="labels">
                            <RiLockPasswordLine className="icons"/>
                            <FormLabel>{Translate('repeatNewPassword')} *</FormLabel>
                            <FormControl type="password" value={this.state.confirmPassword} onChange={event => this.updateConfirmPassword(event.target.value)} />
                            <ValidationMessage valid={this.state.confirmPasswordValid} message={this.state.errorMsg.confirmPassword} />
                        </FormGroup>
                        <div className="bottom">
                            <Reaptcha className="captcha" ref={e => (this.captcha = e)} hl={getLanguage()}
                                      sitekey="6LdHkr8ZAAAAANbDVayO9qNn7iHVA5GvPlSnXxYE" onVerify={this.onCaptchaVerify}/>
                        </div>
                        <div className="bottom">
                            <Button className="button" type="submit" onClick={this.change} disabled={!this.state.formValid}>
                                { this.state.loading ? <Spinner className="spinner" animation="border" /> : Translate('confirm') } </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}
