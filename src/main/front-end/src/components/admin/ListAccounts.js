import React, { Component }  from 'react';
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
import {Pagination, Button, InputGroup, Form} from "react-bootstrap";
import Swal from "sweetalert2";
import Profile from './Profile';
import '../../css/AdminMenu.css'
import {BsSearch} from "react-icons/all";


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
                lastSuccessfulAuth: ""
            }],
            loaded: false,
            pageAmount: 1,
            page: 0,
            showProfile: props.showProfile ? props.showProfile : false,
            profileLogin: '',
            tableLoading: false,
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

    createData = (login, email, firstname, lastname, active, confirmed, lastSuccessfulAuth ) => {
        return { login, email, firstname, lastname, active, confirmed, lastSuccessfulAuth };
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

    handleBackButtonClick = () => {
        this.setState({ showProfile: false, loaded: false });
        this.getAccounts();
    };

    handleSearch = (e) => {
        if (/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ`\-!@#$%^&*]+)$/.test(e) || e === '') {
            console.log(e)
            axios.get("/accounts/pageAmount/" + e, { headers: getHeader() })
                .then(response => {
                    this.setState({
                        pageAmount: response.data,
                        tableLoading: true
                    })
                }).catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            }).then(() => axios.get("/accounts/" + this.state.page + "/" + e, { headers: getHeader() } )
                .then(response => {
                    this.setState({
                        users: response.data,
                        loaded: true,
                        tableLoading: false
                    })
                }).catch(error => {
                    Swal.fire({
                        icon: "error",
                        title: error.response.data
                    })
                }))
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
                this.state.users[i].lastSuccessfulAuth))
        }

        const pages = [];
        for(let i=0; i<this.state.pageAmount; i++) {
            pages.push(
                <Pagination.Item key={i} active={i === this.state.page}>{i+1}</Pagination.Item>
            )
        }

        if(!this.state.loaded) {
            return ( <Spinner animation="border" /> )
        } else {
            if(!this.state.showProfile) {
                return (
                    <div>
                        <p className="filterUsers">{Translate('filterUsers')}</p>
                        <InputGroup className="mb-3">
                            <InputGroup.Prepend>
                                <InputGroup.Text>
                                    <BsSearch/>
                                </InputGroup.Text>
                            </InputGroup.Prepend>
                            <Form.Control type="text" onChange={(e) => this.handleSearch(e.target.value)}/>
                        </InputGroup>

                        { this.state.tableLoading ? <Spinner animation="border" /> :
                        <TableContainer component={Paper}>
                            <Table className={classes.table} aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell className="emptyLabel"/>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('username')}</TableCell>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('email')}</TableCell>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('firstname')}</TableCell>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('lastname')}</TableCell>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('active')}</TableCell>
                                        <TableCell className="tableLabels"
                                                   align="center">{Translate('confirmed')}</TableCell>
                                        <TableCell className="tableLongLabels"
                                                   align="center">{Translate('lastAuthDate')}</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {rows.map((row) => (
                                        <TableRow key={row.login}>
                                            <TableCell align="center">
                                                <Button className="viewProfileButton"
                                                        onClick={() =>
                                                            this.setState({showProfile: true, profileLogin: row.login })}
                                                >{Translate('viewProfile')}</Button>
                                            </TableCell>
                                            <TableCell align="center">{row.login}</TableCell>
                                            <TableCell align="center">{row.email}</TableCell>
                                            <TableCell align="center">{row.firstname}</TableCell>
                                            <TableCell align="center">{row.lastname}</TableCell>
                                            <TableCell align="center">{Translate(row.active)}</TableCell>
                                            <TableCell align="center">{Translate(row.confirmed)}</TableCell>
                                            <TableCell align="center">{row.lastSuccessfulAuth}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer> }
                        <div className="paginationDiv">
                            <Pagination className="herb">
                                <Pagination.First className="herb"/>
                                <Pagination className="herb" onClick={this.handlePageChange}>{pages}</Pagination>
                                <Pagination.Last/>
                            </Pagination>
                        </div>
                    </div>
                );
            } else return (
                <Profile profileLogin={this.state.profileLogin} handleBackButtonClick={this.handleBackButtonClick}/>
            )
        }
    }
}