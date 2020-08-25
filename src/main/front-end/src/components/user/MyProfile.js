import React, { Component } from "react";
import axios from 'axios';
import { FaUser, AiFillEdit } from "react-icons/all";
import '../../css/MyProfile.css';
import Translate from "../../i18n/Translate";
import {FormGroup, FormControl, OverlayTrigger, Tooltip, Button} from 'react-bootstrap';
import {getHeader, getLanguageShortcut, getUser} from "../../services/UserDataService";
import Swal from "sweetalert2";
import Spinner from "react-bootstrap/Spinner";
import ValidationMessage from "../../i18n/ValidationMessage";
import ValidationService from "../../services/ValidationService";

export default class MyProfile extends Component {

    constructor(props) {
        super(props);
        this.state = {
            login: "",
            email: "",
            creationDate: "",
            firstname: "", firstnameCopy: "", firstnameValid: true,
            lastname: "", lastnameCopy: "", lastnameValid: true,
            accessLevels: "",
            errorMsg: {},
            loaded: false, buttonLoading: false,
            editable: false
        }
    }

    componentDidMount() {
        axios.get('/account/' + getUser(), { headers: getHeader() })
            .then(response => {
                this.setState({
                    login: response.data.login,
                    email: response.data.email,
                    creationDate: response.data.creationDate,
                    firstname: response.data.firstname,
                    firstnameCopy: response.data.firstname,
                    lastname: response.data.lastname,
                    lastnameCopy: response.data.lastname,
                    accessLevels: response.data.accessLevels,
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

    handleEdit = (e) => {
        e.preventDefault();
        this.setState({
            buttonLoading: true
        });
        axios.put('/editPersonal', {
            login: getUser(),
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            language: getLanguageShortcut()
        }, { headers: getHeader() })
            .then(() => {
                this.setState({
                    firstnameCopy: this.state.firstname,
                    lastnameCopy: this.state.lastname,
                    editable: false,
                    buttonLoading: false
                });
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            }).then(() => this.setState({ buttonLoading: false }))
        })
    };

    cancelEdit = () => {
      this.setState({
          firstname: this.state.firstnameCopy,
          lastname: this.state.lastnameCopy,
          firstnameValid: true,
          lastnameValid: true,
          editable: false,
          buttonLoading: false
      })
    };

    render() {
        if(!this.state.loaded) {
            return (<Spinner animation="border"/>)
        } else return (
            <div>
                <div className="profileImage">
                    <FaUser/>
                </div>
                <div className="profileData">
                    <p className="label profileText">{Translate('username')}</p>
                    <p className="profileText">{this.state.login}</p>
                    <p className="label profileText">{Translate('email')}</p>
                    <p className="profileText">{this.state.email}</p>
                    { this.state.accessLevels.length > 1 ?
                        <div style={{"margin-bottom": "20px"}}><p className="label profileText">{Translate('accessLevels')}</p>
                            <p className="profileText">{Translate(this.state.accessLevels.toString())}</p></div> : null }
                    <p className="label profileText">{Translate('creationDate')}</p>
                    <p className="profileText">{this.state.creationDate}</p>
                    <p className="label profileText">{Translate('firstname')}</p>

                    { this.state.editable ?
                        <FormGroup className="validationMessages">
                            <FormControl size='lg' defaultValue={this.state.firstname} onChange={event => this.updateFirstname(event.target.value)}/>
                            <ValidationMessage valid={this.state.firstnameValid} message={this.state.errorMsg.firstname} />
                        </FormGroup>
                        : <p className="profileText">{this.state.firstname}</p>}
                    <p className="label profileText">{Translate('lastname')}</p>

                    { this.state.editable ?
                        <FormGroup className="validationMessages">
                            <FormControl size='lg' defaultValue={this.state.lastname} onChange={event => this.updateLastname(event.target.value)}/>
                            <ValidationMessage valid={this.state.lastnameValid} message={this.state.errorMsg.lastname} />
                        </FormGroup>
                    : <p className="profileText">{this.state.lastname}</p> }

                    { this.state.editable ?
                        <div>
                            <Button className="editButtonConfirm" onClick={this.handleEdit}
                                    disabled={!this.state.firstnameValid || !this.state.lastnameValid}>
                                { this.state.buttonLoading ? <Spinner className="spinner" animation="border" /> : Translate('save') } </Button>
                            <Button className="editButtonCancel" onClick={this.cancelEdit}>{Translate('cancel')}</Button>
                        </div> : null }

                </div>
                <div className="editIcon">
                    <OverlayTrigger placement="bottom" overlay={<Tooltip id="button-tooltip">{Translate('editTooltip')}</Tooltip>}>
                        <AiFillEdit onClick={() => this.setState({ editable: true })} />
                    </OverlayTrigger>
                </div>
            </div>
        )
    }
}