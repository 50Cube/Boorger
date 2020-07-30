import React, { Component } from "react";
import Swal from "sweetalert2";

export default class Home extends Component {

    componentDidMount() {
        if("swal1" in localStorage) {
            let messages = [localStorage.getItem('swal1'), localStorage.getItem('swal2')];
            Swal.fire({
                icon: 'success',
                html: messages.join('<br>'),
                position: "top-right",
                timer: 2500,
                showConfirmButton: false
            }).then(() => {
                localStorage.removeItem("swal1");
                localStorage.removeItem("swal2");
            })
        }
    }

    render() {
        return (
            <div>
                <h1>HOME PAGE</h1>
            </div>
        )
    }
}
