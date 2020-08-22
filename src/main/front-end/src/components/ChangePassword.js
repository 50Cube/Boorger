import React, { Component } from "react";
import axios from 'axios';
import {getHeader, getUser} from "../services/UserDataService";
import Swal from "sweetalert2";
import {Button, FormControl, FormGroup, FormLabel} from "react-bootstrap";
import {RiLockPasswordLine} from "react-icons/all";
import Translate from "../i18n/Translate";
import ValidationMessage from "../i18n/ValidationMessage";
import '../css/ChangePassword.css';
import Spinner from "react-bootstrap/Spinner";
import Reaptcha from "reaptcha";

export default class ChangePassword extends Component {

    constructor(props) {
        super(props);
        this.state = {
            password: '', passwordValid: false,
            confirmPassword: '', confirmPasswordValid: false,
            previousPassword: '', previousPasswordValid: !!window.location.href.split('=')[1],
            captcha: '', captchaValid: false,
            formValid: false,
            errorMsg: {},
            token: window.location.href.split('='),
            loading: false
        }
    }

    validateForm = () => {
        const { passwordValid, confirmPasswordValid, previousPasswordValid, captchaValid } = this.state;
        this.setState({
            formValid: passwordValid && confirmPasswordValid && previousPasswordValid && captchaValid
        })
    };

    updatePreviousPassword = (previousPassword) => {
        this.setState({previousPassword}, this.validatePreviousPassword)
    };

    validatePreviousPassword = () => {
        const { previousPassword } = this.state;
        let previousPasswordValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(previousPassword.length < 1) {
            previousPasswordValid = false;
            errorMsg.password = 'field-required'
        }
        else if(previousPassword.length < 8) {
            previousPasswordValid = false;
            errorMsg.password = 'password-length'
        }
        else if(!/\d/.test(previousPassword)) {
            previousPasswordValid = false;
            errorMsg.password = 'password-number'
        }
        else if(!/[!@#$%^&*]/.test(previousPassword)) {
            previousPasswordValid = false;
            errorMsg.password = 'password-char'
        }
        else if(previousPassword.length > 32) {
            previousPasswordValid = false;
            errorMsg.password = 'field-toolong'
        }
        this.setState({previousPasswordValid, errorMsg}, this.validateForm)
    };

    updatePassword = (password) => {
        this.setState({password}, this.validatePassword)
    };

    validatePassword = () => {
        const { password } = this.state;
        let passwordValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(password.length < 1) {
            passwordValid = false;
            errorMsg.password = 'field-required'
        }
        else if(password.length < 8) {
            passwordValid = false;
            errorMsg.password = 'password-length'
        }
        else if(!/\d/.test(password)) {
            passwordValid = false;
            errorMsg.password = 'password-number'
        }
        else if(!/[!@#$%^&*]/.test(password)) {
            passwordValid = false;
            errorMsg.password = 'password-char'
        }
        else if(password.length > 32) {
            passwordValid = false;
            errorMsg.password = 'field-toolong'
        }
        this.setState({passwordValid, errorMsg}, this.validateForm)
    };

    updateConfirmPassword = (confirmPassword) => {
        this.setState({confirmPassword}, this.validateConfirmPassword)
    };

    validateConfirmPassword = () => {
        const { confirmPassword } = this.state;
        let confirmPasswordValid = true;
        let errorMsg = {...this.state.errorMsg};

        if(confirmPassword !== this.state.password) {
            confirmPasswordValid = false;
            errorMsg.confirmPassword = 'password-confirm';
        }
        this.setState({confirmPasswordValid, errorMsg}, this.validateForm)
    };

    onCaptchaVerify = (response) => {
        this.setState({
            captchaValid: true,
            captcha: response
        }, this.validateForm)
    };

        // axios.post("/confirm/" + this.state.token[1], null, { headers: getHeader() })
        //     .then(response => {
        //         Swal.fire({
        //             icon: "success",
        //             title: response.data
        //         })
        //     }).catch(error => {
        //     Swal.fire({
        //         icon: "error",
        //         title: error.response.data
        //     })
        // })

    change = () => {
        let url = "/changePassword/";
        if(getUser())
            url.concat(getUser() + "/" + null + this.state.captcha);
        else url.concat(null + "/" + this.state.token[1] + this.state.captcha);

        console.log(url)

        axios.post(url,
            {
                previousPassword: this.state.previousPassword,
                password: this.state.password
        }, { headers: getHeader() })
            .then(response => {
                Swal.fire({
                    icon: "success",
                    title: response.data
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    };

    render() {
        if(this.state.token[1]) {
            return (
                <div className="changeImage">
                    <div className="changeMain">
                        <h1>{Translate('changePassword')}</h1>
                        <form>
                            <FormGroup className="labels">
                                <RiLockPasswordLine className="icons"/>
                                <FormLabel>{Translate('password')} *</FormLabel>
                                <FormControl type="password" value={this.state.password} onChange={event => this.updatePassword(event.target.value)} />
                                <ValidationMessage valid={this.state.passwordValid} message={this.state.errorMsg.password} />
                            </FormGroup>

                            <FormGroup className="labels">
                                <RiLockPasswordLine className="icons"/>
                                <FormLabel>{Translate('password2')} *</FormLabel>
                                <FormControl type="password" value={this.state.confirmPassword} onChange={event => this.updateConfirmPassword(event.target.value)} />
                                <ValidationMessage valid={this.state.confirmPasswordValid} message={this.state.errorMsg.confirmPassword} />
                            </FormGroup>
                            <div className="bottom">
                                <Reaptcha className="captcha" ref={e => (this.captcha = e)} sitekey="6LdHkr8ZAAAAANbDVayO9qNn7iHVA5GvPlSnXxYE" onVerify={this.onCaptchaVerify}/>
                            </div>
                            <div className="bottom">
                                <Button className="button" type="submit" onClick={this.change} disabled={!this.state.formValid}>{Translate('confirm')}</Button>
                            </div>
                        </form>
                        { this.state.loading ? <Spinner className="spinner" animation="border" /> : null }
                    </div>
                </div>
            )
        } else
            return (
                <div className="changeImage">
                    <div className="changeMain">
                        <h1>change password</h1>
                    </div>
                </div>
        )
    }
}
