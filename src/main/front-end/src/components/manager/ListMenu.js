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
            }],
            loaded: false,
            newDish: false, newDishButtonLoading: false,
            name: "", nameValid: false,
            description: "", descriptionValid: false,
            price: 0, priceValid: false,
            formValid: false,
            errorMsg: {}
        }
    }

    componentDidMount() {
        this.loadDishes();
    }

    loadDishes = () => {
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
    };

    createData = (name, description, price) => {
        return { name, description, price};
    };

    validateForm = () => {
        const { nameValid, descriptionValid, priceValid } = this.state;
        this.setState({
            formValid: nameValid && descriptionValid && priceValid
        })
    };

    updateName = (name) => {
        this.setState({name}, ValidationService.validateDishName)
    };

    updateDescription = (description) => {
        this.setState({description}, ValidationService.validateDishDescription)
    };

    updatePrice = (price) => {
      this.setState({price}, ValidationService.validateDishPrice)
    };

    handleNewDish = (e) => {
        e.preventDefault();
        this.setState({ newDishButtonLoading: true });
        axios.post("/dish/" + this.props.restaurantName, {
            name: this.state.name,
            description: this.state.description,
            price: this.state.price
        }, { headers: getHeader() })
            .then(() => {
                this.setState({
                    newDishButtonLoading: false,
                    newDish: false,
                    nameValid: false,
                    descriptionValid: false,
                    priceValid: false
                });
                this.loadDishes();
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            }).then(() => this.setState({ newDishButtonLoading: false }))
        })
    };

    render() {
        const list = [];
        for(let i=0; i<this.state.dishes.length; i++) {
            list.push(this.createData(this.state.dishes[i].name, this.state.dishes[i].description, this.state.dishes[i].price, this.state.dishes[i].active));
        }

        return (
            <div>
                { this.state.newDish ?
                <div className="newDishDiv">
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

                        <FormGroup className="newDishLabels">
                            <FormLabel>{Translate('price')} *</FormLabel>
                            <FormControl className="newDishPriceForm" maxLength={8} value={this.state.price} onChange={event => this.updatePrice(event.target.value)} />
                            <ValidationMessage valid={this.state.priceValid} message={this.state.errorMsg.price} />
                        </FormGroup>
                    </form>
                </div> :
                    <div>
                        <div className="ListMenuNew" onClick={() => this.setState({
                            newDish: true, name: "", description: "", price: 0, })}>
                            <FcPlus className="menuIcon"/> {Translate('newDish')}
                        </div>
                        { this.state.loaded ?
                            <div>
                                <ListGroup className="listMenuListGroup">
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
                    { this.state.newDish ?
                        <div>
                            <Button className="buttons listMenuButton" disabled={!this.state.formValid} onClick={this.handleNewDish}>
                                { this.state.newDishButtonLoading ? <Spinner animation="border" /> : Translate('confirm') }
                            </Button>
                            <Button className="buttons listMenuButton" onClick={() => this.setState({
                                newDish: false,  nameValid: false, descriptionValid: false, priceValid: false, errorMsg: {} })}>
                                {Translate('back')}
                            </Button>
                        </div>
                        : <Button className="buttons listMenuButton" onClick={() => this.props.handleBackButtonClick()}>{Translate('back')}</Button> }
                </div>
            </div>
        );
    }
}