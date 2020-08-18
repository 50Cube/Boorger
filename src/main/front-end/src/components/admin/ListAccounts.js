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
            loaded: false
        })
    }

    componentDidMount() {
        axios.get("/accounts", { headers: getHeader() } )
            .then(response => {
                this.setState({
                    users: response.data
                })
            }).catch(error => {
                console.log("ERROR " + error)
            }).finally(() => {
                console.log(this.state.users)
                this.setState({
                    loaded: true
                })
            })
    }

    createData = (login, email, firstname, lastname, active, confirmed, lastAuthIp, lastSuccessfulAuth, creationDate ) => {
        return { login, email, firstname, lastname, active, confirmed, lastAuthIp, lastSuccessfulAuth, creationDate };
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

        if(!this.state.loaded) {
            return ( <Spinner animation="border" /> )
        } else return (
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
        );
    }
}