import React, { Component } from 'react';
import axios from 'axios';
import {getHeader} from "../../services/UserDataService";
import {Spinner} from "react-bootstrap";
import Swal from "sweetalert2";

export default class MyReservationDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservation: {},
            loaded: false
        }
    }

    componentDidMount() {
        axios.get("/reservation/" + this.props.reservationId, { headers: getHeader() })
            .then(response => {
                this.setState({
                    reservation: response.data,
                    loaded: true
                })
            }).catch(error => {
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
                    {this.state.reservation.clientDTO.login}
                </div>
            );
        } else return ( <Spinner animation="border" /> )
    }
}