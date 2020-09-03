import React, { Component } from "react";
import { FiMap } from "react-icons/all";

export default class SingleRestaurantPuzzle extends Component {

    render() {
        const image = "data:image/png;base64," + this.props.photo;
        return (
            <div>
                <div className="puzzleImageDiv">
                    <img className="puzzleImage" src={image}  alt="loading"/>
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