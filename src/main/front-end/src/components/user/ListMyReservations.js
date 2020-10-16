import React, { Component } from 'react';
import axios from 'axios';
import {getHeader, getUser} from "../../services/UserDataService";
import Swal from "sweetalert2";
import {Spinner, Button, InputGroup, Form} from "react-bootstrap";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import Translate from '../../i18n/Translate';
import {BsSearch} from "react-icons/all";
import MyReservationDetails from "./MyReservationDetails";
import '../../css/ListReservations.css';

export default class ListMyReservations extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservationId: "",
            reservations: [{}],
            loaded: false,
            showDetails: false
        }
    }

    componentDidMount() {
        axios.get("/reservations/" + getUser(), { headers: getHeader() })
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

    createData = (businessKey, startDate, status, restaurantName) => {
      return { businessKey, startDate, status, restaurantName };
    };

    handleSearch = (e) => {
        this.setState({ loaded: false });
        if (/^([a-zA-Z0-9!@$^&*-]+)$/.test(e) || e === '') {
            axios.get("/reservations/filtered/" + getUser() + "/" + e, { headers: getHeader()})
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

    render() {
        let rows = [];
        for(let i=0; i<this.state.reservations.length; i++) {
            rows.push(this.createData(this.state.reservations[i].businessKey, this.state.reservations[i].startDate,
                this.state.reservations[i].status, this.state.reservations[i].restaurantName));
        }

        if(this.state.showDetails) {
            return ( <MyReservationDetails reservationId={this.state.reservationId} /> )
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
                { this.state.loaded ?
                <div>
                    <TableContainer className="myReservationTable">
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell className="myReservationsTableLabels">{Translate('reservationNumber')}</TableCell>
                                    <TableCell className="myReservationsTableLabels">{Translate('restaurant')}</TableCell>
                                    <TableCell className="myReservationsTableLabels">{Translate('startDate')}</TableCell>
                                    <TableCell className="myReservationsTableLabels">{Translate('reservationStatus')}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                { rows.map(row => (
                                    <TableRow>
                                        <TableCell>{row.businessKey}</TableCell>
                                        <TableCell>{row.restaurantName}</TableCell>
                                        <TableCell>{row.startDate}</TableCell>
                                        <TableCell>{Translate(row.status)}</TableCell>
                                        <TableCell>
                                            <Button className="myReservationsDetailsButton" onClick={() => this.setState({
                                                reservationId: row.businessKey, showDetails: true })}>{Translate('details')}</Button>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </div> : <Spinner animation="border" /> }
            </div>
        ); }
    }
}