import React, { Component } from 'react';
import {Spinner, Button, FormGroup, FormControl, Tooltip, InputGroup} from "react-bootstrap";
import axios from 'axios';
import Translate from '../../i18n/Translate';
import Swal from "sweetalert2";
import {getHeader, getLanguageShortcut} from "../../services/UserDataService";
import { FcApproval, FcCancel, AiFillEdit, TiArrowBack, IoMdArrowDropdown, RiMailSendLine } from "react-icons/all";
import ValidationMessage from "../../i18n/ValidationMessage";
import ValidationService from "../../services/ValidationService";
import BootstrapSwitchButton from 'bootstrap-switch-button-react';
import OverlayTrigger from "react-bootstrap/OverlayTrigger";
import { Checkbox } from "@material-ui/core";
import '../../css/Profile.css';

export default class Profile extends Component {

    constructor(props) {
        super(props);
        this.state = {
            login: props.profileLogin,
            email: '',
            firstname: "", firstnameCopy: "", firstnameValid: true,
            lastname: "", lastnameCopy: "", lastnameValid: true,
            active: '', activeCopy: "",
            confirmed: '',
            accessLevels: {},
            adminChecked: false, adminCheckedCopy: false,
            managerChecked: false, managerCheckedCopy: false,
            clientChecked: false, clientCheckedCopy: false,
            accessLevelsValid: true,
            lastSuccessfulAuth: '',
            lastAuthIp: '',
            language: '',
            errorMsg: {},
            loaded: false, buttonLoading: false, emailButtonLoading: false,
            editable: false,
        }
    }

    componentDidMount() {
        axios.get('/account/' + this.state.login, { headers: getHeader() })
            .then(response => {
                this.setState({
                    login: response.data.login,
                    email: response.data.email,
                    firstname: response.data.firstname, firstnameCopy: response.data.firstname,
                    lastname: response.data.lastname, lastnameCopy: response.data.lastname,
                    active: response.data.active, activeCopy: response.data.active,
                    confirmed: response.data.confirmed,
                    accessLevels: response.data.accessLevels,
                    adminChecked: response.data.accessLevels.includes(process.env.REACT_APP_ADMIN_ROLE),
                    adminCheckedCopy: response.data.accessLevels.includes(process.env.REACT_APP_ADMIN_ROLE),
                    managerChecked: response.data.accessLevels.includes(process.env.REACT_APP_MANAGER_ROLE),
                    managerCheckedCopy: response.data.accessLevels.includes(process.env.REACT_APP_MANAGER_ROLE),
                    clientChecked: response.data.accessLevels.includes(process.env.REACT_APP_CLIENT_ROLE),
                    clientCheckedCopy: response.data.accessLevels.includes(process.env.REACT_APP_CLIENT_ROLE),
                    lastSuccessfulAuth: response.data.lastSuccessfulAuth,
                    lastAuthIp: response.data.lastAuthIp,
                    language: response.data.language,
                    loaded: true
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    }

    updateFirstname = (firstname) => {
        this.setState({firstname}, ValidationService.validateFirstname)
    };

    updateLastname = (lastname) => {
        this.setState({lastname}, ValidationService.validateLastname)
    };

    cancelEdit = () => {
        this.setState({
            firstname: this.state.firstnameCopy,
            lastname: this.state.lastnameCopy,
            active: this.state.activeCopy,
            firstnameValid: true,
            lastnameValid: true,
            editable: false,
            buttonLoading: false,
            adminChecked: this.state.adminCheckedCopy,
            managerChecked: this.state.managerCheckedCopy,
            clientChecked: this.state.clientCheckedCopy,
            accessLevelsValid: true
        })
    };

    handleEdit = (e) => {
        e.preventDefault();
        let roles = [];
        if(this.state.adminChecked) roles.push(process.env.REACT_APP_ADMIN_ROLE);
        if(this.state.clientChecked) roles.push(process.env.REACT_APP_CLIENT_ROLE);
        if(this.state.managerChecked) roles.push(process.env.REACT_APP_MANAGER_ROLE);
        this.setState({
            buttonLoading: true,
            accessLevels: roles
        });
        axios.put('/editOtherAccount', {
            login: this.state.login,
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            active: this.state.active,
            accessLevels: roles,
            language: getLanguageShortcut()
        }, { headers: getHeader()})
            .then(() => {
                this.setState({
                    firstnameCopy: this.state.firstname,
                    lastnameCopy: this.state.lastname,
                    activeCopy: this.state.active,
                    adminCheckedCopy: this.state.adminChecked,
                    managerCheckedCopy: this.state.managerChecked,
                    clientCheckedCopy: this.state.clientChecked,
                    editable: false,
                    buttonLoading: false
                })
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
    };

    resendEmail = (e) => {
        e.preventDefault();
        this.setState({ emailButtonLoading: true });
        axios.post('/resendEmail', {
            email: this.state.email,
            language: this.state.language
        }, {headers: getHeader()})
          .then(() => {
            this.setState({ emailButtonLoading: false })
        }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
     };

    render() {
        if(!this.state.loaded) {
            return (<Spinner animation="border"/>)
        } else return (
            <div className="profileDiv">
                <div className="profileFirstDiv">
                    <p className="profileLabels">{Translate('username')}</p>
                    <p className="profileText">{this.state.login}</p>
                    <p className="profileLabels">{Translate('email')}</p>
                    <p className="profileText">{this.state.email}</p>
                    <p className="profileLabels">{Translate('lastAuthDate')}</p>
                    <p className="profileText">{this.state.lastSuccessfulAuth ? this.state.lastSuccessfulAuth : '-'}</p>
                    <p className="profileLabels">{Translate('lastAuthIP')}</p>
                    <p className="profileText">{this.state.lastAuthIp ? this.state.lastAuthIp : '-'}</p>
                </div>
                <div className="profileSecondDiv">
                    <p className="profileLabels">{Translate('firstname')}</p>
                    { this.state.editable ?
                    <FormGroup className="profileForms">
                        <FormControl size='lg' defaultValue={this.state.firstname} onChange={event => this.updateFirstname(event.target.value)}/>
                        <ValidationMessage valid={this.state.firstnameValid} message={this.state.errorMsg.firstname} />
                    </FormGroup>
                        : <p className="profileText">{this.state.firstname}</p> }

                    <p className="profileLabels">{Translate('lastname')}</p>
                    { this.state.editable ?
                        <FormGroup className="profileForms">
                            <FormControl size='lg' defaultValue={this.state.lastname} onChange={event => this.updateLastname(event.target.value)}/>
                            <ValidationMessage valid={this.state.lastnameValid} message={this.state.errorMsg.lastname} />
                        </FormGroup>
                    : <p className="profileText">{this.state.lastname}</p> }

                    <p className="profileLabels">{Translate('active')}</p>
                    { this.state.editable ?
                        <div className="profileActiveSwitch"><BootstrapSwitchButton checked={this.state.active} size="lg" onstyle="dark"
                                                        onlabel={Translate('true')} offlabel={Translate('false')}
                                                        onChange={() => this.setState({ active: !this.state.active })}/></div> :
                        <p className="profileText"> { this.state.active ? <FcApproval/> : <FcCancel/> } </p> }

                    <p className="profileLabels">{Translate('confirmed')}</p>
                    { <p className="profileText profileEmailSend"> { this.state.confirmed ? <FcApproval/> : <div>
                        { this.state.emailButtonLoading ? <div><Spinner className="spinner" animation="border" /></div>
                            : <OverlayTrigger trigger="click" placement="bottom" overlay={
                                <Tooltip className="profileEmailSend" onClick={this.resendEmail} id="mail-tooltip">
                                    <div>{Translate('confirmMail')}<RiMailSendLine/></div>
                                </Tooltip>}>
                                <div><FcCancel/><IoMdArrowDropdown/></div>
                            </OverlayTrigger> }
                        </div>
                    } </p> }
                </div>
                <div className="profileThirdDiv">
                    <p className="profileLabels">{Translate('accessLevels')}</p>
                    { this.state.editable ?
                    <div className="profileRoleCheckbox">
                        <div className="profileCheckbox">
                            <Checkbox checked={ this.state.adminChecked }
                                onChange={() => this.setState({ adminChecked: !this.state.adminChecked }, ValidationService.validateAccessLevels) }/>
                            <p>{Translate(process.env.REACT_APP_ADMIN_ROLE)}</p>
                        </div>
                        <div className="profileCheckbox">
                            <Checkbox checked={ this.state.managerChecked }
                                onChange={() => this.setState({ managerChecked: !this.state.managerChecked }, ValidationService.validateAccessLevels) } />
                            <p>{Translate(process.env.REACT_APP_MANAGER_ROLE)}</p>
                        </div>
                        <div className="profileCheckbox">
                            <Checkbox checked={ this.state.clientChecked }
                                onChange={() => this.setState({ clientChecked: !this.state.clientChecked }, ValidationService.validateAccessLevels) }/>
                            <p>{Translate(process.env.REACT_APP_CLIENT_ROLE)}</p>
                        </div>
                        <ValidationMessage valid={this.state.accessLevelsValid} message={this.state.errorMsg.accessLevels} />
                    </div>
                        : <p className="profileText">{Translate(this.state.accessLevels)}</p> }
                </div>
                <div>
                    { this.state.editable ?
                        <Button className="profileConfirmButton" onClick={this.handleEdit}
                                disabled={!this.state.firstnameValid || !this.state.lastnameValid || !this.state.accessLevelsValid}>
                            { this.state.buttonLoading ? <Spinner className="spinner" animation="border" /> : Translate('save') }
                        </Button> :
                        <Button className="profileButton" onClick={() => this.setState({ editable: true})}>
                            <AiFillEdit className="profileButtonIcon"/> {Translate('edit')}
                        </Button> }

                    { this.state.editable ?
                        <Button className="profileCancelButton" onClick={this.cancelEdit}>
                            {Translate('cancel')}
                        </Button> :
                        <Button className="profileButton" onClick={() => {this.props.handleBackButtonClick()}}>
                            <TiArrowBack className="profileButtonIcon"/> {Translate('back')}
                        </Button> }
                </div>
            </div>
        )
    }
}