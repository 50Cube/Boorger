import React, { Component } from "react";
import axios from 'axios';
import { FaUser, AiFillEdit } from "react-icons/all";
import '../../css/MyProfile.css';
import Translate from "../../i18n/Translate";
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import {getHeader, getUser} from "../../services/UserDataService";
import Swal from "sweetalert2";
import Spinner from "react-bootstrap/Spinner";

export default class MyProfile extends Component {

    constructor(props) {
        super(props);
        this.state = {
            user: {
                login: "",
                email: "",
                creationDate: "",
                firstname: "",
                lastname: "",
                accessLevels: ""
            },
            loaded: false,
            editable: false
        }
    }

    componentDidMount() {
        axios.get('/account/' + getUser(), { headers: getHeader() })
            .then(response => {
                console.log(response.data)
                this.setState({
                    user: response.data,
                    loaded: true
                })
            }).catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
        })
    }


    render() {
        if(!this.state.loaded) {
            return (<Spinner animation="border"/>)
        } else return (
            <div>
                <div className="profileImage">
                    <FaUser/>
                </div>
                <div className="profileData">
                    <p className="label">{Translate('username')}</p>
                    <p>{this.state.user.login}</p>
                    <p className="label">{Translate('email')}</p>
                    <p>{this.state.user.email}</p>
                    { this.state.user.accessLevels.length > 1 ?
                        <div style={{"margin-bottom": "20px"}}><p className="label">{Translate('accessLevels')}</p>
                            <p>{Translate(this.state.user.accessLevels.toString())}</p></div> : null }
                    <p className="label">{Translate('creationDate')}</p>
                    <p>{this.state.user.creationDate}</p>
                    <p className="label">{Translate('firstname')}</p>
                    <p>{this.state.user.firstname}</p>
                    <p className="label">{Translate('lastname')}</p>
                    <p>{this.state.user.lastname}</p>
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