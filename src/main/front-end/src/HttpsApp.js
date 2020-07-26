import React, { Component } from 'react';
import HttpsRedirect from 'react-https-redirect';
import App from "./App";

export default class HttpsApp extends Component {
    render() {
        return (
            <HttpsRedirect>
                <App />
            </HttpsRedirect>
        );
    }
}