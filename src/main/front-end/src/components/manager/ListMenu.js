import React, { Component } from "react";
import axios from 'axios';
import { ListGroup, Button } from "react-bootstrap";
import Translate from '../../i18n/Translate';

export default class ListMenu extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
    }

    render() {
        return (
            <div>
                {this.props.restaurantName}
                <Button className="buttons" onClick={() => this.props.handleBackButtonClick()}>{Translate('back')}</Button>
            </div>
        );
    }
}