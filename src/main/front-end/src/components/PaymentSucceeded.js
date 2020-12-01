import React, { Component } from "react";
import axios from 'axios';
import Translate from '../i18n/Translate';
import ErrorPageImage from "./ErrorPageImage";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
import {getHeader} from "../services/UserDataService";
import '../css/ErrorPageImage.css';

export default class PaymentSucceeded extends Component {

    constructor(props) {
        super(props);
        this.state = {
            message: Translate('paymentLoading')
        }
    }

    componentDidMount() {
        let args = this.props.location.search.substring(1).split('&');
        axios.post("/finishPayment", {
            paymentId: args[0].substring("paymentId=".length),
            token: args[1].substring("token=".length),
            payerId: args[2].substring("payerId=".length)
        }, { headers: getHeader() }).then(response => {
            this.setState({
                message: response.data
            })
        }).catch(error => {
            this.setState({
                message: error.response.data
            })
        })
    }

    render() {
        return (
            <div className="errorDiv">
                <ErrorPageImage/>
                <div className="message-box">
                    <h2 className="payment">{Translate('paymentSucceeded')}</h2>
                    <h1 className="payment">{this.state.message}</h1>
                    <Button as={Link} to="/" className="buttons">{Translate('backToHome')}</Button>
                </div>
            </div>
        )
    }
}
