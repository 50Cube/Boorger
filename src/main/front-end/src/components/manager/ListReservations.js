import React, { Component } from 'react';
import axios from "axios";
import {getHeader, getUser} from "../../services/UserDataService";
import Swal from "sweetalert2";
import Translate from "../../i18n/Translate";
import {Button, Form, InputGroup, Spinner} from "react-bootstrap";
import {BsSearch} from "react-icons/all";
import {Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@material-ui/core";
import '../../css/ListReservations.css';
import ReservationDetails from "../ReservationDetails";

export default class ListReservations extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            reservations: [{
                clientDTO: {}
            }],
            reservationId: "",
            loaded: false,
            showDetails: false
        }
    }

    getReservations = () => {
        axios.get("/reservations", { headers: getHeader() })
            .then(response => {
                this.setState({
                    reservations: response.data,
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
        this.getReservations();
    }

    createData = (businessKey, startDate, status, restaurantName, login) => {
        return { businessKey, startDate, status, restaurantName, login };
    };

    handleSearch = (e) => {
        this.setState({ loaded: false });
        if (/^([a-zA-Z0-9!@$^&*-]+)$/.test(e) || e === '') {
            axios.get("/reservations/filtered/" + e, { headers: getHeader()})
                .then(response => {
                    this.setState({
                        reservations: response.data,
                        loaded: true
                    })
                }).catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
        }
    };

    handleBackButtonClick = () => {
        this.setState({ showDetails: false, loaded: false });
        this.getReservations();
    };

    render() {
        let rows = [];
        for(let i=0; i<this.state.reservations.length; i++) {
            rows.push(this.createData(this.state.reservations[i].businessKey, this.state.reservations[i].startDate,
                this.state.reservations[i].status, this.state.reservations[i].restaurantName, this.state.reservations[i].clientDTO.login));
        }

        if(this.state.showDetails) {
            return ( <ReservationDetails reservationId={this.state.reservationId} handleBackButtonClick={this.handleBackButtonClick} manager={true} /> )
        } else {
            return (
                <div>
                    <p className="filterReservations">{Translate('filterReservations')}</p>
                    <InputGroup className="mb-3">
                        <InputGroup.Prepend>
                            <InputGroup.Text>
                                <BsSearch/>
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <Form.Control type="text" onChange={(e) => this.handleSearch(e.target.value)}/>
                    </InputGroup>
                    {this.state.loaded ?
                        <div>
                            <TableContainer className="reservationTable">
                                <Table aria-label="simple table">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell
                                                className="myReservationsTableLabels">{Translate('reservationNumber')}</TableCell>
                                            <TableCell
                                                className="myReservationsTableLabels">{Translate('user')}</TableCell>
                                            <TableCell
                                                className="myReservationsTableLabels">{Translate('restaurant')}</TableCell>
                                            <TableCell
                                                className="myReservationsTableLabels">{Translate('startDate')}</TableCell>
                                            <TableCell
                                                className="myReservationsTableLabels">{Translate('reservationStatus')}</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {rows.map(row => (
                                            <TableRow>
                                                <TableCell>{row.businessKey}</TableCell>
                                                <TableCell>{row.login}</TableCell>
                                                <TableCell>{row.restaurantName}</TableCell>
                                                <TableCell>{row.startDate}</TableCell>
                                                <TableCell>{Translate(row.status)}</TableCell>
                                                <TableCell>
                                                    <Button className="myReservationsDetailsButton" onClick={() => {
                                                        this.setState({
                                                            reservationId: row.businessKey, showDetails: true
                                                        })
                                                    }}>{Translate('details')}</Button>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        </div> : <Spinner animation="border"/>}
                </div>
            );
        }
    }
}