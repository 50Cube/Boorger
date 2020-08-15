import React, { Component } from "react";
import Swal from "sweetalert2";
import Translate from "../i18n/Translate";
import image from '../assets/navbarImage.jpg';
import { BsFillForwardFill } from "react-icons/all";
import { Jumbotron } from "../components/Jumbotron";
import '../css/Home.css';
import '../css/App.css';

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
                <Jumbotron />
                <div className="centerDiv">
                    <div className="steps">
                        <div className="step">
                            <img alt="loading" className="stepPicture" src={image}/>
                            <h1 className="title">{Translate('choose-restaurant')}</h1>
                        </div>
                        <BsFillForwardFill className="arrow"/>
                        <div className="step">
                            <img alt="loading" className="stepPicture" src={image}/>
                            <h1 className="title">{Translate('book-table')}</h1>
                        </div>
                        <BsFillForwardFill className="arrow"/>
                        <div className="step">
                            <img alt="loading" className="stepPicture" src={image}/>
                            <h1 className="title">{Translate('order-food')}</h1>
                        </div>
                    </div>
                    <div className="search">
                        <p>{Translate('search-restaurant')}</p>
                        <div class="input-group mb-3">
                            <input type="text" class="form-control" className="form-control"/>
                            <button class="btn btn-outline-secondary" type="button">{Translate('search')}</button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}
