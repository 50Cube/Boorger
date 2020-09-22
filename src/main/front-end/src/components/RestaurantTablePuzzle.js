import React, { Component } from "react";

export default class RestaurantTablePuzzle extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                number: {this.props.number} capacity: {this.props.capacity}
            </div>
        );
    }
}