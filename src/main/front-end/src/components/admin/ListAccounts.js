import React, { Component, Fragment }  from 'react';
import axios from 'axios';
import { getHeader } from "../../services/UserDataService";
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Spinner from "react-bootstrap/Spinner";
import Paper from '@material-ui/core/Paper';
import Translate from "../../i18n/Translate";
import '../../css/AdminMenu.css'
import {Pagination} from "react-bootstrap";
import Swal from "sweetalert2";



export default class ListAccounts extends Component {

    constructor(props) {
        super(props);
        this.state = ({
            users: [{
                login: "",
                email: "",
                firstname: "",
                lastname: "",
                active: "",
                confirmed: "",
                lastAuthIp: "",
                lastSuccessfulAuth: "",
                creationDate: ""
            }],
            loaded: false,
            pageAmount: 1,
            page: 0
        })
    }

    getAccounts = () => {
        axios.get("/accounts/" + this.state.page, { headers: getHeader() } )
            .then(response => {
                this.setState({
                    users: response.data,
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
        axios.get("/accounts/pageAmount", { headers: getHeader() })
            .then(response => {
                this.setState({
                    pageAmount: response.data
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        });

        this.getAccounts();
    }

    createData = (login, email, firstname, lastname, active, confirmed, lastAuthIp, lastSuccessfulAuth, creationDate ) => {
        return { login, email, firstname, lastname, active, confirmed, lastAuthIp, lastSuccessfulAuth, creationDate };
    };


    handlePageChange = (e) => {
        if(e.target.text) {
            this.setState({
                    loaded: false,
                    page: e.target.text - 1
                },
                this.getAccounts
            );
        }
    };

    render() {
        const classes = makeStyles({
            table: {
                minWidth: 600,
            },
        });

        const rows = [];
        for(let i=0; i<this.state.users.length; i++) {
            rows.push(this.createData(this.state.users[i].login, this.state.users[i].email, this.state.users[i].firstname,
                this.state.users[i].lastname, this.state.users[i].active.toString(), this.state.users[i].confirmed.toString(),
                this.state.users[i].lastAuthIp, this.state.users[i].lastSuccessfulAuth, this.state.users[i].creationDate))
        }

        const pages = [];
        for(let i=0; i<this.state.pageAmount; i++) {
            pages.push(
                <Pagination.Item key={i} active={i === this.state.page}>{i+1}</Pagination.Item>
            )
        }

        if(!this.state.loaded) {
            return ( <Spinner animation="border" /> )
        } else return (
            <div>
                <TableContainer component={Paper}>
                    <Table className={classes.table} aria-label="simple table">
                        <TableHead>
                            <TableRow>
                                <TableCell className="tableLabels" align="center">{Translate('username')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('email')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('firstname')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('lastname')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('active')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('confirmed')}</TableCell>
                                <TableCell className="tableLongLabels" align="center">{Translate('lastAuthIP')}</TableCell>
                                <TableCell className="tableLongLabels" align="center">{Translate('lastAuthDate')}</TableCell>
                                <TableCell className="tableLabels" align="center">{Translate('creationDate')}</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <TableRow key={row.login}>
                                    <TableCell align="center">{row.login}</TableCell>
                                    <TableCell align="center">{row.email}</TableCell>
                                    <TableCell align="center">{row.firstname}</TableCell>
                                    <TableCell align="center">{row.lastname}</TableCell>
                                    <TableCell align="center">{Translate(row.active)}</TableCell>
                                    <TableCell align="center">{Translate(row.confirmed)}</TableCell>
                                    <TableCell align="center">{row.lastAuthIp}</TableCell>
                                    <TableCell align="center">{row.lastSuccessfulAuth}</TableCell>
                                    <TableCell align="center">{row.creationDate}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
                <div className="paginationDiv">
                    <Pagination>
                        <Pagination.First />
                        <Pagination onClick={this.handlePageChange} >{pages}</Pagination>
                        <Pagination.Last />
                    </Pagination>
                </div>
            </div>
        );
    }
}