import React, { Component } from "react";
import axios from 'axios';
import Translate from '../i18n/Translate';
import ErrorPageImage from "./ErrorPageImage";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
import {getHeader} from "../services/UserDataService";
import Swal from "sweetalert2";
import '../css/ErrorPageImage.css';

export default class PaymentCancelled extends Component {

    componentDidMount() {
        axios.post("/cancelPayment/" + this.props.location.search.substring("?token=".length), {},{ headers: getHeader()})
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
    }

    render() {
        return (
            <div className="errorDiv">
                <ErrorPageImage/>
                <div className="message-box">
                    <h1 className="payment">{Translate('paymentCancelled')}</h1>
                    <Button as={Link} to="/" className="buttons">{Translate('backToHome')}</Button>
                </div>
            </div>
        )
    }
}
