import React, { Component } from 'react';

export default class ExceptionHandler extends Component {

    componentDidCatch(error, errorInfo) {
        window.location.replace("/error")
    }

    render() {
        return this.props.children;
    }
}