import React, { Component } from "react";
import axios from 'axios';
import Translate from '../../i18n/Translate';
import {getHeader} from "../../services/UserDataService";
import Swal from "sweetalert2";

export default class ListRestaurants extends Component {

    constructor(props) {
        super(props);
        this.state = {
            restaurants: [{
                name: ""
            }],
            loaded: false
        }
    }

    componentDidMount() {
        axios.get("/restaurants", { headers: getHeader()})
            .then(response => {
                this.setState({

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
            <div>
                <h1>lista restauracji</h1>
            </div>
        );
    }
}