import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../../services/UserDataService";
import Swal from "sweetalert2";
import {Spinner} from "react-bootstrap";
import Translate from '../../i18n/Translate';
import '../../css/RestaurantDetails.css';

export default class RestaurantDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            description: "", descriptionValid: false,
            installment: 0,
            active: "",
            address: {},
            hours: {},
            tables: [{}],
            creationDate: "",
            signature: "",
            loaded: false,
            editing: false,
            errorMsg: {}
        }
    }

    componentDidMount() {
        axios.get("/restaurant/" + this.props.restaurantName, { headers: getHeader() })
            .then(response => {
                this.setState({
                    description: response.data.description,
                    installment: response.data.installment,
                    active: response.data.active,
                    address: response.data.addressDTO,
                    hours: response.data.hoursDTO,
                    tables: response.data.tableDTOs,
                    creationDate: response.data.creationDate,
                    signature: response.data.signature,
                    loaded: true
                })
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
    }

    render() {
        if(this.state.loaded) {
            return (
                <div>
                    <div className="restaurantDetailsFirstDiv">
                        <p className="restaurantDetailsNameLabel">{this.props.restaurantName}</p>
                        <p>{this.state.description}</p>
                        <p className="restaurantDetailsAddressLabel">{this.state.address.city}, {this.state.address.street} {this.state.address.streetNo}</p>
                        <p>{Translate('installment')}: {this.state.installment} {Translate('pln')}</p>
                        { this.state.active ? <p>{Translate('restaurantActive')}</p> : <p>{Translate('')}</p> }
                        <p>{Translate('creationDateRestaurant')} {this.state.creationDate}</p>
                    </div>
                    <div className="restaurantDetailsSecondDiv">
                        second
                    </div>
                </div>
            ) } else return ( <Spinner animation="border" /> )
    }
}