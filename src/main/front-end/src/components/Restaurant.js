import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../services/UserDataService";
import Swal from "sweetalert2";
import {Jumbotron} from "./Jumbotron";
import Translate from '../i18n/Translate';
import { IoMdSad } from "react-icons/all";
import {Button, ListGroup} from "react-bootstrap";
import '../css/Restaurant.css';

export default class Restaurant extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: sessionStorage.getItem("restaurantName"),
            description: "",
            installment: "",
            active: "",
            city: "",
            street: "",
            streetNo: "",
            tables: [{
                number: "",
                capacity: "",
                active: ""
            }],
            hours: {},
            menu: {}
        }
    }

    componentDidMount() {
        axios.get("/restaurant/" + this.state.name, { headers: getHeader() } )
            .then(response => {
                this.setState({
                    description: response.data.description,
                    installment: response.data.installment,
                    active: response.data.active,
                    city: response.data.addressDTO.city,
                    street: response.data.addressDTO.street,
                    streetNo: response.data.addressDTO.streetNo,
                    tables: response.data.tableDTOs,
                    hours: response.data.hoursDTO,
                    menu: response.data.dishDTOs
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    }

    createData = (name, description, price) => {
        return { name, description, price };
    };

    render() {
        const menuList = [];
        for(let i=0; i<this.state.menu.length; i++) {
            menuList.push(this.createData(this.state.menu[i].name, this.state.menu[i].description, this.state.menu[i].price))
        }

        return (
            <div>
                <Jumbotron />
                <div className="restaurantInfo">
                    { this.state.active ? null : <h2 className="restaurantInactive">
                        <IoMdSad className="sadIcon"/>{Translate('inactiveMsg')}<IoMdSad className="sadIcon"/></h2> }
                    <div className="infoFirstDiv">
                        <h1 className="restaurantNameLabel">{this.state.name}</h1>
                        <p className="restaurantDescriptionLabel">{this.state.description}</p>
                        <p className="restaurantAddressLabel">{this.state.city}, {this.state.street} {this.state.streetNo}</p>
                    </div>
                    <div className="infoSecondDiv">
                        <div>
                            <p className="restaurantHoursLabel">{Translate('openingHours')}</p>
                            <div className="restaurantDays">
                                <p>{Translate('monday')}</p>
                                <p>{Translate('tuesday')}</p>
                                <p>{Translate('wednesday')}</p>
                                <p>{Translate('thursday')}</p>
                                <p>{Translate('friday')}</p>
                                <p>{Translate('saturday')}</p>
                                <p>{Translate('sunday')}</p>
                            </div>
                            <div className="restaurantHours">
                                <p>{this.state.hours.mondayStart} - {this.state.hours.mondayEnd}</p>
                                <p>{this.state.hours.tuesdayStart} - {this.state.hours.tuesdayEnd}</p>
                                <p>{this.state.hours.wednesdayStart} - {this.state.hours.wednesdayEnd}</p>
                                <p>{this.state.hours.thursdayStart} - {this.state.hours.thursdayEnd}</p>
                                <p>{this.state.hours.fridayStart} - {this.state.hours.fridayEnd}</p>
                                <p>{this.state.hours.saturdayStart} - {this.state.hours.saturdayEnd}</p>
                                <p>{this.state.hours.sundayStart} - {this.state.hours.sundayEnd}</p>
                            </div>
                        </div>
                    </div>
                    <div className="restaurantReserve">
                        <Button className="reserveButton">{Translate('book-table')}</Button>
                    </div>
                    <h2 className="restaurantMenuLabel">Menu</h2>
                    <ListGroup>
                        { menuList.map(element => (
                            <ListGroup.Item className="restaurantMenuItem">
                                <div className="restaurantMenuFirstDiv">
                                    <p className="restaurantMenuNameLabel">{element.name}</p>
                                    <p>{element.description}</p>
                                </div>
                                <div className="restaurantMenuSecondDiv">
                                    <p className="restaurantMenuPriceLabel">{element.price} {Translate('pln')}</p>
                                </div>
                            </ListGroup.Item>
                        )) }
                    </ListGroup>
                </div>
            </div>
        );
    }
}