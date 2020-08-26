import React, { Component } from "react";
import Translate from '../i18n/Translate';
import ErrorPageImage from "./ErrorPageImage";
import '../css/ErrorPageImage.css';

export default class AccessDenied extends Component {

    render() {
        return (
            <div className="errorDiv">
                <ErrorPageImage/>
                <div className="message-box">
                    <h1>ERROR 403</h1>
                    <h2>{Translate('accessDenied')}</h2>
                </div>
            </div>
        )
    }
}
