import React, { Component } from 'react';
import axios from 'axios';
import { FormGroup, FormControl, FormLabel, Button } from "react-bootstrap";
import Translate from "../../i18n/Translate";
import ValidationService from "../../services/ValidationService";
import ValidationMessage from "../../i18n/ValidationMessage";
import { Checkbox } from "@material-ui/core";
import '../../css/AddAccount.css';
import BootstrapSwitchButton from "bootstrap-switch-button-react";
import {getHeader, getLanguageShortcut} from "../../services/UserDataService";
import Swal from "sweetalert2";

export default class AddAccount extends Component {

    constructor(props) {
        super(props);
        this.state = {
            login: '', loginValid: false,
            password: '', passwordValid: false,
            confirmPassword: '', confirmPasswordValid: false,
            firstname: '', firstnameValid: false,
            lastname: '', lastnameValid: false,
            email: '', emailValid: false,
            active: false,
            accessLevels: {}, accessLevelsValid: false,
            clientChecked: false,
            managerChecked: false,
            adminChecked: false,
            formValid: false,
            errorMsg: {},
        }
    }

    validateForm = () => {
        const { loginValid, passwordValid, confirmPasswordValid, firstnameValid, lastnameValid, emailValid, accessLevelsValid } = this.state;
        this.setState({
            formValid: loginValid && passwordValid && confirmPasswordValid && firstnameValid && lastnameValid && emailValid && accessLevelsValid
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

    addAccount = (e) => {
        e.preventDefault();
        let roles = [];
        if(this.state.adminChecked) roles.push(process.env.REACT_APP_ADMIN_ROLE);
        if(this.state.clientChecked) roles.push(process.env.REACT_APP_CLIENT_ROLE);
        if(this.state.managerChecked) roles.push(process.env.REACT_APP_MANAGER_ROLE);
        axios.post('/addAccount', {
            login: this.state.login,
            password: this.state.password,
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            email: this.state.email,
            active: this.state.active,
            accessLevels: roles,
            language: getLanguageShortcut()
        }, { headers: getHeader()})
            .then(response => {
                Swal.fire({
                    icon: "success",
                    title: response.data
                }).then(() => window.location.reload());
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
    };

    render() {
        return (
            <div>
                <div className="addAccountFirstDiv">
                    <form>
                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('username')} *</FormLabel>
                            <FormControl autoFocus value={this.state.login} onChange={event => this.updateLogin(event.target.value)} />
                            <ValidationMessage valid={this.state.loginValid} message={this.state.errorMsg.login} />
                        </FormGroup>

                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('password')} *</FormLabel>
                            <FormControl type="password" value={this.state.password} onChange={event => this.updatePassword(event.target.value)} />
                            <ValidationMessage valid={this.state.passwordValid} message={this.state.errorMsg.password} />
                        </FormGroup>

                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('password2')} *</FormLabel>
                            <FormControl type="password" value={this.state.confirmPassword} onChange={event => this.updateConfirmPassword(event.target.value)} />
                            <ValidationMessage valid={this.state.confirmPasswordValid} message={this.state.errorMsg.confirmPassword} />
                        </FormGroup>

                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('firstname')} *</FormLabel>
                            <FormControl value={this.state.firstname} onChange={event => this.updateFirstname(event.target.value)} />
                            <ValidationMessage valid={this.state.firstnameValid} message={this.state.errorMsg.firstname} />
                        </FormGroup>

                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('lastname')} *</FormLabel>
                            <FormControl value={this.state.lastname} onChange={event => this.updateLastname(event.target.value)} />
                            <ValidationMessage valid={this.state.lastnameValid} message={this.state.errorMsg.lastname} />
                        </FormGroup>

                        <FormGroup className="addAccountLabels">
                            <FormLabel>{Translate('email')} *</FormLabel>
                            <FormControl value={this.state.email} onChange={event => this.updateEmail(event.target.value)} />
                            <ValidationMessage valid={this.state.emailValid} message={this.state.errorMsg.email} />
                        </FormGroup>
                    </form>
                </div>
                <div className="addAccountSecondDiv">
                    <div className="addAccountActive">
                        <p>{Translate('active')}</p>
                        <BootstrapSwitchButton checked={this.state.active} size="lg" onstyle="dark"
                                               onlabel={Translate('true')} offlabel={Translate('false')}
                                               onChange={() => this.setState({ active: !this.state.active })}/>
                    </div>
                    <p className="addAccountCheckbox" style={{"font-style": "italic"}}>{Translate('accessLevels')} *</p>
                    <div className="addAccountCheckbox">
                        <Checkbox checked={ this.state.clientChecked }
                                  onChange={() => this.setState({ clientChecked: !this.state.clientChecked }, ValidationService.validateAccessLevels) } />
                        <p>{Translate(process.env.REACT_APP_CLIENT_ROLE)}</p>
                    </div>
                    <div className="addAccountCheckbox">
                        <Checkbox checked={ this.state.managerChecked }
                                  onChange={() => this.setState({ managerChecked: !this.state.managerChecked }, ValidationService.validateAccessLevels) } />
                        <p>{Translate(process.env.REACT_APP_MANAGER_ROLE)}</p>
                    </div>
                    <div className="addAccountCheckbox">
                        <Checkbox checked={ this.state.adminChecked }
                                  onChange={() => this.setState({ adminChecked: !this.state.adminChecked }, ValidationService.validateAccessLevels) } />
                        <p>{Translate(process.env.REACT_APP_ADMIN_ROLE)}</p>
                    </div>
                    <ValidationMessage valid={this.state.accessLevelsValid} message={this.state.errorMsg.accessLevels} />
                </div>
                <div className="addAccountButtons">
                    <Button onClick={this.addAccount} disabled={!this.state.formValid}>{Translate('confirm')}</Button>
                </div>
            </div>
        )
    }
}