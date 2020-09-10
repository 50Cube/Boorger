import React, { Component } from "react";
import axios from 'axios';
import {ListGroup, Button, Spinner, FormGroup, FormLabel, FormControl} from "react-bootstrap";
import Translate from '../../i18n/Translate';
import {getHeader} from "../../services/UserDataService";
import Swal from "sweetalert2";
import { FcPlus } from "react-icons/all";
import '../../css/ListMenu.css';
import ValidationMessage from "../../i18n/ValidationMessage";
import ValidationService from "../../services/ValidationService";

export default class ListMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            dishes: [{
                name: "",
                description: "",
                price: 0,
                active: ""
            }],
            loaded: false,
            newDish: false,
            name: "", nameValid: false,
            description: "", descriptionValid: false,
            price: 0,
            active: "",
            errorMsg: {}
        }
    }

    componentDidMount() {
        axios.get("/restaurant/" + this.props.restaurantName, { headers: getHeader()})
            .then(response => {
                this.setState({
                    dishes: response.data.dishDTOs,
                    loaded: true
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        })
    }

    createData = (name, description, price, active) => {
        return { name, description, price, active};
    };

    validateForm = () => {
        const { nameValid, descriptionValid } = this.state;
        this.setState({
            formValid: nameValid && descriptionValid
        })
    };

    updateName = (name) => {
        this.setState({name}, ValidationService.validateDishName)
    };

    updateDescription = (description) => {
        this.setState({description}, ValidationService.validateDishDescription)
    };

    render() {
        const list = [];
        for(let i=0; i<this.state.dishes.length; i++) {
            list.push(this.createData(this.state.dishes[i].name, this.state.dishes[i].description, this.state.dishes[i].price, this.state.dishes[i].active));
        }

        return (
            <div>
                { this.state.newDish ?
                <div>
                    <div className="newDishFirstDiv">
                        <form>
                            <FormGroup className="newDishLabels">
                                <FormLabel>{Translate('name')} *</FormLabel>
                                <FormControl autoFocus value={this.state.name} onChange={event => this.updateName(event.target.value)} />
                                <ValidationMessage valid={this.state.nameValid} message={this.state.errorMsg.name}/>
                            </FormGroup>

                            <FormGroup className="newDishLabels">
                                <FormLabel>{Translate('description')} *</FormLabel>
                                <FormControl value={this.state.description} onChange={event => this.updateDescription(event.target.value)} />
                                <ValidationMessage valid={this.state.descriptionValid} message={this.state.errorMsg.description} />
                            </FormGroup>
                        </form>
                    </div>
                    <div className="newDishSecondDiv">
                        second
                    </div>
                </div> :
                    <div>
                        <div className="ListMenuNew" onClick={() => this.setState({ newDish: true })}>
                            <FcPlus className="menuIcon"/> {Translate('newDish')}
                        </div>
                        { this.state.loaded ?
                            <div>
                                <ListGroup>
                                    { list.map(element => (
                                        <ListGroup.Item className="listMenuElement">
                                            <div className="listMenuFirstDiv">
                                                <p className="listMenuName">{element.name}</p>
                                                <p>{element.description}</p>
                                            </div>
                                            <div className="listMenuSecondDiv">
                                                <p className="listMenuPrice">{element.price} {Translate('pln')}</p>
                                            </div>
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            </div> :  <Spinner animation="border" /> }
                    </div> }
                <div>
                    { this.state.newDish ? <Button className="buttons listMenuButton">{Translate('confirm')}</Button> : null }
                    <Button className="buttons listMenuButton" onClick={() => this.props.handleBackButtonClick()}>{Translate('back')}</Button>
                </div>
            </div>
        );
    }
}