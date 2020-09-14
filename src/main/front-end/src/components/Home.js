import React, { Component } from "react";
import axios from 'axios';
import Translate from "../i18n/Translate";
import image from '../assets/navbarImage.jpg';
import { BsFillForwardFill, BsSearch } from "react-icons/all";
import { Jumbotron } from "../components/Jumbotron";
import {InputGroup, Form, ListGroup, Spinner} from "react-bootstrap";
import {getHeader} from "../services/UserDataService";
import SingleRestaurantPuzzle from "./SingleRestaurantPuzzle";
import {Link} from "react-router-dom";
import Swal from "sweetalert2";
import '../css/Home.css';
import '../css/App.css';

export default class Home extends Component {

    constructor(props) {
        super(props);
        this.state = {
            restaurants: [{
                name: "",
                description: "",
                active: "",
                photo: "",
                addressDTO: {
                    city: "",
                    street: "",
                    streetNo: ""
                }
            }],
            loaded: false
        }
    }

    componentDidMount() {
        axios.get('/restaurants', { headers: getHeader()})
            .then(response => {
                this.setState({
                    restaurants: response.data,
                    loaded: true
                })
            })
            .catch(error => {
                console.log(error)
            })
    }

    createData = (name, description, active, photo, city, street, streetNo) => {
        return { name, description, active, photo, city, street, streetNo };
    };

    handleSearch = (e) => {
        this.setState({ loaded: false });
        if (/^([a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*,. -]+)$/.test(e) || e === '') {
            axios.get("/restaurants/" + e, { headers: getHeader()})
                .then(response => {
                    this.setState({
                        restaurants: response.data,
                        loaded: true
                    })
                }).catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
        }
    };

    render() {
        const firstColumn = [];
        const secondColumn = [];
        for(let i=0; i<this.state.restaurants.length; i++) {
            let data = this.createData(this.state.restaurants[i].name, this.state.restaurants[i].description, this.state.restaurants[i].active,
                this.state.restaurants[i].photo, this.state.restaurants[i].addressDTO.city, this.state.restaurants[i].addressDTO.street,
                this.state.restaurants[i].addressDTO.streetNo);
                i%2===0 ? firstColumn.push(data) : secondColumn.push(data)
        }

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
                        <InputGroup className="mb-3">
                            <InputGroup.Prepend>
                                <InputGroup.Text>
                                    <BsSearch/>
                                </InputGroup.Text>
                            </InputGroup.Prepend>
                            <Form.Control type="text" onChange={(e) => this.handleSearch(e.target.value)}/>
                        </InputGroup>
                    </div>

                    { this.state.loaded ?
                    <div className="homeRestaurants">
                        <div className="restaurantsFirstColumn">
                            <ListGroup>
                            { firstColumn.map((element) => (
                                <ListGroup.Item className="homeListItem" as={Link} to="/restaurant" onClick={
                                    () => sessionStorage.setItem("restaurantName", element.name) }>
                                    <SingleRestaurantPuzzle
                                        name={element.name}
                                        description={element.description}
                                        city={element.city}
                                        street={element.street}
                                        streetNo={element.streetNo}
                                        photo={element.photo}
                                        active={element.active}
                                    />
                                </ListGroup.Item>
                            ))}
                            </ListGroup>
                        </div>
                        <div className="restaurantsSecondColumn">
                            <ListGroup>
                                { secondColumn.map((element) => (
                                    <ListGroup.Item className="homeListItem" as={Link} to={"/restaurant"} onClick={
                                        () => sessionStorage.setItem("restaurantName", element.name) }>
                                        <SingleRestaurantPuzzle
                                            name={element.name}
                                            description={element.description}
                                            city={element.city}
                                            street={element.street}
                                            streetNo={element.streetNo}
                                            photo={element.photo}
                                            active={element.active}
                                        />
                                    </ListGroup.Item>
                                ))}
                            </ListGroup>
                        </div>
                    </div> : <Spinner animation="border" /> }
                </div>
            </div>
        )
    }
}
