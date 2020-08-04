import React, { Component }  from 'react';
import axios from 'axios';
import { getAuthHeader } from "../services/UserDataService";
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';


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
                lastAuthIP: "",
                lastAuthDate: ""
            }],
            loaded: false
        })
    }

    componentDidMount() {
        axios.get("/accounts", { headers: getAuthHeader() } )
            .then(response => {
                this.setState({
                    users: response.data
                })
            }).catch(error => {
                console.log("ERROR " + error)
            }).finally(() => {
                this.setState({
                    loaded: true
                })
            })
    }

    createData = (login, email, firstname, lastname, active, confirmed, lastAuthIP, lastAuthDate) => {
        return { login, email, firstname, lastname, active, confirmed, lastAuthIP, lastAuthDate };
    };

    render() {
        const classes = makeStyles({
            table: {
                minWidth: 600,
            },
        });

        const rows = [];
        for(let i=0; i<this.state.users.length; i++) {
            rows.push(this.createData(this.state.users[i].login, this.state.users[i].email, this.state.users[i].firstname, this.state.users[i].lastname,
                this.state.users[i].active.toString(), this.state.users[i].confirmed.toString(), this.state.users[i].lastAuthIP, this.state.users[i].lastAuthDate))
        }

        if(!this.state.loaded) {
            return (
                <div>
                    <h1>loading</h1>
                </div>
            )
        } else return (
            <TableContainer component={Paper}>
                <Table className={classes.table} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell>login</TableCell>
                            <TableCell align="right">email</TableCell>
                            <TableCell align="right">name</TableCell>
                            <TableCell align="right">surname</TableCell>
                            <TableCell align="right">active</TableCell>
                            <TableCell align="right">confirmed</TableCell>
                            <TableCell align="right">last auth ip</TableCell>
                            <TableCell align="right">last auth date</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {rows.map((row) => (
                            <TableRow key={row.login}>
                                <TableCell component="th" scope="row">
                                    {row.login}
                                </TableCell>
                                <TableCell align="right">{row.email}</TableCell>
                                <TableCell align="right">{row.firstname}</TableCell>
                                <TableCell align="right">{row.lastname}</TableCell>
                                <TableCell align="right">{row.active}</TableCell>
                                <TableCell align="right">{row.confirmed}</TableCell>
                                <TableCell align="right">{row.lastAuthIP}</TableCell>
                                <TableCell align="right">{row.lastAuthDate}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        );
    }
}