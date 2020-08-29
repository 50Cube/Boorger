import React, { Component } from "react";
import {Button, FormGroup, FormControl, FormLabel} from 'react-bootstrap';
import axios from 'axios';
import Translate from "../../i18n/Translate";
import Swal from "sweetalert2";
import {getHeader, getLanguage} from "../../services/UserDataService";
import ValidationMessage from "../../i18n/ValidationMessage";
import { AiOutlineMail, RiLockPasswordLine, FiUserPlus, FiUser, FiUsers } from "react-icons/all";
import Reaptcha from "reaptcha";
import '../../css/Register.css';
import Spinner from "react-bootstrap/Spinner";
import ValidationService from '../../services/ValidationService';

export default class Register extends Component {

    constructor(props) {
        super(props);
        this.state = {
            login: '', loginValid: false,
            password: '', passwordValid: false,
            confirmPassword: '', confirmPasswordValid: false,
            firstname: '', firstnameValid: false,
            lastname: '', lastnameValid: false,
            email: '', emailValid: false,
            language: localStorage.getItem("lang") ? localStorage.getItem("lang") : navigator.language,
            captcha: '', captchaValid: false,
            formValid: false,
            errorMsg: {},
            loading: false
        }
    }

    validateForm = () => {
      const { loginValid, passwordValid, confirmPasswordValid, firstnameValid, lastnameValid, emailValid } = this.state;
      this.setState({
          formValid: loginValid && passwordValid && confirmPasswordValid && firstnameValid && lastnameValid && emailValid
      })
    };

    updateLogin = (login) => {
        this.setState({login}, ValidationService.validateLogin)
    };

    updatePassword = (password) => {
      this.setState({password}, ValidationService.validatePassword)
    };

    updateConfirmPassword = (confirmPassword) => {
        this.setState({confirmPassword}, ValidationService.validateConfirmPassword)
    };

    updateFirstname = (firstname) => {
      this.setState({firstname}, ValidationService.validateFirstname)
    };

    updateLastname = (lastname) => {
        this.setState({lastname}, ValidationService.validateLastname)
    };

    updateEmail = (email) => {
      this.setState({email}, ValidationService.validateEmail)
    };

    register = (e) => {
        e.preventDefault();
        this.setState({
            loading: true
        });
        if(this.state.formValid) {
            axios.post("/register/" + this.state.captcha, {
                login: this.state.login,
                password: this.state.password,
                firstname: this.state.firstname,
                lastname: this.state.lastname,
                email: this.state.email,
                language: this.state.language
            }, {headers: getHeader()})
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
                    this.setState({loading: false});
                    this.captcha.reset();
                })
            })
        } else {
            Swal.fire({
                icon: "warning",
                title:  getLanguage() === ('en-US') ? 'Please fill out all required fields in correct way' : 'Uzupełnij wszystkie wymagane pola w poprawny sposób'
            })
        }
    };

    onCaptchaVerify = (response) => {
        this.setState({
            captchaValid: true,
            captcha: response
        })
    };

    render() {
        return (
            <div className="image">
                <div className="main">
                    <form>
                        <FormGroup className="labels">
                            <FiUserPlus className="icons"/>
                            <FormLabel>{Translate('username')} *</FormLabel>
                            <FormControl autoFocus value={this.state.login} onChange={event => this.updateLogin(event.target.value)} />
                            <ValidationMessage valid={this.state.loginValid} message={this.state.errorMsg.login} />
                        </FormGroup>

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

                        <FormGroup className="labels">
                            <FiUser className="icons"/>
                            <FormLabel>{Translate('firstname')} *</FormLabel>
                            <FormControl value={this.state.firstname} onChange={event => this.updateFirstname(event.target.value)} />
                            <ValidationMessage valid={this.state.firstnameValid} message={this.state.errorMsg.firstname} />
                        </FormGroup>

                        <FormGroup className="labels">
                            <FiUsers className="icons"/>
                            <FormLabel>{Translate('lastname')} *</FormLabel>
                            <FormControl value={this.state.lastname} onChange={event => this.updateLastname(event.target.value)} />
                            <ValidationMessage valid={this.state.lastnameValid} message={this.state.errorMsg.lastname} />
                        </FormGroup>

                        <FormGroup className="labels">
                            <AiOutlineMail className="icons"/>
                            <FormLabel>{Translate('email')} *</FormLabel>
                            <FormControl value={this.state.email} onChange={event => this.updateEmail(event.target.value)} />
                            <ValidationMessage valid={this.state.emailValid} message={this.state.errorMsg.email} />
                        </FormGroup>

                        <div className="bottom">
                            <Reaptcha className="captcha" ref={e => (this.captcha = e)} hl={getLanguage()}
                                      sitekey="6LdHkr8ZAAAAANbDVayO9qNn7iHVA5GvPlSnXxYE" onVerify={this.onCaptchaVerify}/>
                        </div>
                        <div className="bottom">
                            <Button className="button" type="submit" onClick={this.register} disabled={!this.state.captchaValid}>
                                { this.state.loading ? <Spinner className="spinner" animation="border" /> : Translate('confirm') } </Button>
                        </div>
                    </form>
                </div>
            </div>
        )
    }
}
