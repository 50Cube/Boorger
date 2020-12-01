import React, { Component } from 'react';
import ErrorPageImage from "./ErrorPageImage";
import Translate from "../i18n/Translate";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import '../css/ErrorPageImage.css';

export default class UnexpectedError extends Component {

    render() {
        return (
            <div className="errorDiv">
                <ErrorPageImage/>
                <div className="message-box">
                    <h1 className="errorMessage">{Translate('unexpectedError')}</h1>
                    <Button as={Link} to="/" className="buttons">{Translate('backToHome')}</Button>
                </div>
            </div>
        );
    }
}