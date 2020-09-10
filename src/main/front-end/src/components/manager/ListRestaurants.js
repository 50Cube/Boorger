import React, { Component } from "react";
import axios from 'axios';
import Translate from '../../i18n/Translate';
import {getHeader} from "../../services/UserDataService";
import Swal from "sweetalert2";
import { ListGroup, Spinner, Button } from 'react-bootstrap';
import ListMenu from "./ListMenu";
import '../../css/ListRestaurants.css';

export default class ListRestaurants extends Component {

    constructor(props) {
        super(props);
        this.state = {
            restaurants: [{
                name: "",
                active: ""
            }],
            loaded: false,
            showDetails: false,
            showMenu: false,
            restaurantName: ""
        }
    }

    componentDidMount() {
        this.getRestaurants();
    }

    getRestaurants = () => {
        axios.get("/restaurants", { headers: getHeader()})
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
    };

    createData = (name, active) => {
        return { name, active };
    };

    handleBackButtonClick = () => {
        this.setState({ showDetails: false, showMenu: false, loaded: false });
        this.getRestaurants();
    };

    render() {
        const list = [];
        for(let i=0; i<this.state.restaurants.length; i++) {
            list.push(this.createData(this.state.restaurants[i].name, this.state.restaurants[i].active.toString()));
        }

        if(this.state.showDetails) {
            return (
                <div>
                    details
                </div>
            )
        } else if(this.state.showMenu) {
            return ( <ListMenu restaurantName={this.state.restaurantName} handleBackButtonClick={this.handleBackButtonClick} /> )
        } else return (
            <div>
                { this.state.loaded ?
                <ListGroup className="listRestaurantsListGroup">
                    { list.map(element => (
                        <ListGroup.Item className="listRestaurantElement">
                            <div>
                                <p className="listRestaurantsLabel listRestaurantsName">{element.name}</p>
                                { element.active === 'true' ? <Button className="listRestaurantsLabel listRestaurantsButton">{Translate('deactivate')}</Button> :
                                    <Button className="listRestaurantsLabel listRestaurantsButton">{Translate('activate')}</Button>}
                                <Button className="listRestaurantsLabel listRestaurantsButton">{Translate('details')}</Button>
                                <Button className="listRestaurantsLabel listRestaurantsButton" onClick={() => this.setState({
                                    restaurantName: element.name, showMenu: true })}>{Translate('showMenu')}
                                </Button>
                            </div>
                        </ListGroup.Item>
                    ))}
                </ListGroup> :
                    <Spinner animation="border" /> }
            </div>
        );
    }
}