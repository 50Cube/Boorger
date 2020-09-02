import React, { Component } from "react";
import { FiMap } from "react-icons/all";

export default class SingleRestaurantPuzzle extends Component {

    render() {
        return (
            <div>
                <div className="puzzleImage">
                    <p>zdjecie</p>
                </div>
                <div className="puzzleContent">
                    <p className="puzzleName">{this.props.name}</p>
                    <p className="puzzleDescription">{this.props.description}</p>
                    <p className="puzzleAddress"><FiMap className="puzzleIcon"/>
                        {this.props.city}, {this.props.street} {this.props.streetNo}
                    </p>
                </div>
            </div>
        )
    }
}