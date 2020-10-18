import React, { Component } from 'react';
import axios from 'axios';
import {getHeader} from "../services/UserDataService";
import {Spinner, ListGroup, Button} from "react-bootstrap";
import Swal from "sweetalert2";
import Translate from '../i18n/Translate';
import '../css/ReservationDetails.css';

export default class ReservationDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservation: {
                dishDTOs: [{}]
            },
            loaded: false
        }
    }

    getReservation = () => {
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
    };

    componentDidMount() {
        this.getReservation();
    }

    createData = (name, price) => {
      return { name, price };
    };

    handleStatusChange = (status) => {
        this.setState({ loaded: false });
        axios.put("/reservation/" + status + "/" + this.props.reservationId, {}, { headers: getHeader()})
            .then(() => {
                this.getReservation();
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    };

    render() {
        let menu = [];
        for(let i=0; i<this.state.reservation.dishDTOs.length; i++) {
            menu.push(this.createData(this.state.reservation.dishDTOs[i].name, this.state.reservation.dishDTOs[i].price));
        }

        if(this.state.loaded) {
            return (
                <div>
                    <div className="reservationFirstDiv">
                        <p className="reservationLabels">{Translate('reservationNumber')}</p>
                        <p className="reservationContent">{this.state.reservation.businessKey}</p>
                        { this.props.manager ?
                            <div>
                                <p className="reservationLabels">{Translate('user')}</p>
                                <p className="reservationContent">{this.state.reservation.clientDTO.login}</p>
                            </div> : null }
                        <p className="reservationLabels">{Translate('bookingTime')}</p>
                        <p className="reservationContent">{this.state.reservation.creationDate}</p>
                        <p className="reservationLabels">{Translate('selectedDate')}</p>
                        <p className="reservationContent">{this.state.reservation.startDate} - {this.state.reservation.endDate}</p>
                        <p className="reservationLabels">{Translate('restaurant')}</p>
                        <p className="reservationContent">{this.state.reservation.restaurantName}</p>
                        <p className="reservationLabels">{Translate('tableNumber')}</p>
                        <p className="reservationContent">{this.state.reservation.tableNumber}</p>
                        <p className="reservationLabels">{Translate('reservationStatus')}</p>
                        <p className="reservationContent">{Translate(this.state.reservation.status)}</p>
                        { this.state.reservation.status === 'BOOKED' ?
                        <div>
                            <Button className="reservationStatusFinish" onClick={() => this.handleStatusChange("finish")}>
                                {Translate('finishReservation')}</Button>
                            <Button className="reservationStatusCancel" onClick={() => this.handleStatusChange("cancel")}>
                                {Translate('cancelReservation')}</Button>
                        </div> : null }
                    </div>
                    <div className="reservationSecondDiv">
                        <p className="reservationLabels">{Translate('order')}</p>
                        <ListGroup className="reservationMenuList">
                            { menu.map(element => (
                                <ListGroup.Item className="reservationMenuItem">
                                    <p className="reservationMenuName">{element.name}</p>
                                    <p className="reservationMenuPrice">{element.price} {Translate('pln')}</p>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </div>
                    <Button className="buttons" onClick={() => this.props.handleBackButtonClick()}>{Translate('back')}</Button>
                </div>
            );
        } else return ( <Spinner animation="border" /> )
    }
}