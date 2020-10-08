import React, { Component } from 'react';
import axios from 'axios';
import {getHeader, getUser} from "../../services/UserDataService";
import Swal from "sweetalert2";
import {Spinner} from "react-bootstrap";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@material-ui/core';
import Translate from '../../i18n/Translate';

export default class ListMyReservations extends Component {

    constructor(props) {
        super(props);
        this.state = {
            reservations: [{}],
            loaded: false
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

    render() {
        let rows = [];
        for(let i=0; i<this.state.reservations.length; i++) {
            rows.push(this.createData(this.state.reservations[i].businessKey, this.state.reservations[i].startDate,
                this.state.reservations[i].status, this.state.reservations[i].restaurantName));
        }

        return (
            <div>
                { this.state.loaded ?
                <div>
                    <TableContainer>
                        <Table aria-label="simple table">
                            <TableHead>
                                <TableRow>
                                    <TableCell>{Translate('reservationNumber')}</TableCell>
                                    <TableCell>{Translate('restaurant')}</TableCell>
                                    <TableCell>{Translate('startDate')}</TableCell>
                                    <TableCell>{Translate('reservationStatus')}</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                { rows.map(row => (
                                    <TableRow>
                                        <TableCell>{row.businessKey}</TableCell>
                                        <TableCell>{row.restaurantName}</TableCell>
                                        <TableCell>{row.startDate}</TableCell>
                                        <TableCell>{Translate(row.status)}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </div> : <Spinner animation="border" /> }
            </div>
        );
    }
}