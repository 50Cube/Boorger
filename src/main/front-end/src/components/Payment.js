import React, { Component } from "react";
import Translate from '../i18n/Translate';
import ErrorPageImage from "./ErrorPageImage";
import '../css/ErrorPageImage.css';

export default class Payment extends Component {

    render() {
        return (
            <div className="errorDiv">
                <ErrorPageImage/>
                <div className="message-box">
                    <h1 style={{"letter-spacing":"5px"}}>{Translate('finishPayment')}</h1>
                    <h2>{Translate('finishPayment2')}</h2>
                </div>
            </div>
        )
    }
}
