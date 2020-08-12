import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../services/UserDataService";
import Swal from "sweetalert2";

export default class Confirm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            token: window.location.href.split('=')
        }
    }

    componentDidMount() {
        this.props.history.push("/");
        axios.post("/confirm/" + this.state.token[1], null, { headers: getHeader() })
            .then(response => {
                Swal.fire({
                    icon: "success",
                    title: response.data
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
            <div/>
        )
    }
}
