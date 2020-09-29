import React, { Component } from "react";
import Translate from '../i18n/Translate';
import '../css/BookRestaurant.css';

export default class RestaurantTablePuzzle extends Component {

    render() {
        return (
            <div>
                { this.props.capacity === 2 ? <p className="tableCapacityLabel">{Translate('2tableList')}</p> : null }
                { this.props.capacity === 3 ? <p className="tableCapacityLabel">{Translate('3tableList')}</p> : null }
                { this.props.capacity === 4 ? <p className="tableCapacityLabel">{Translate('4tableList')}</p> : null }
                <p className="tableNumberLabel">{Translate('number')}: {this.props.number}</p>
            </div>
        );
    }
}