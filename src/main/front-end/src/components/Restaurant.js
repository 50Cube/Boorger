import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../services/UserDataService";
import Swal from "sweetalert2";

export default class Restaurant extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: sessionStorage.getItem("restaurantName"),
            description: "",
            installment: "",
            active: "",
            city: "",
            street: "",
            streetNo: "",
            tables: [{
                number: "",
                capacity: "",
                active: ""
            }],
            hours: {}
        }
    }

    componentDidMount() {
        axios.get("/restaurant/" + this.state.name, { headers: getHeader() } )
            .then(response => {
                this.setState({
                    description: response.data.description,
                    installment: response.data.installment,
                    active: response.data.active,
                    city: response.data.addressDTO.city,
                    street: response.data.addressDTO.street,
                    streetNo: response.data.addressDTO.streetNo,
                    tables: response.data.tableDTOs,
                    hours: response.data.hoursDTO
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    }

    render() {
        return (
            <div>
                <h1>restaurant details</h1>
                <h2> name = {this.state.name}</h2>
            </div>
        );
    }
}