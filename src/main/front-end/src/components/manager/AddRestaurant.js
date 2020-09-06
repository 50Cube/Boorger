import React, { Component } from 'react';
import axios from 'axios';
import Translate from '../../i18n/Translate';
import {FormGroup, FormLabel, FormControl, Button, Spinner, ListGroup, ListGroupItem, OverlayTrigger, Tooltip } from "react-bootstrap";
import ValidationMessage from "../../i18n/ValidationMessage";
import ValidationService from "../../services/ValidationService";
import TextareaCounter from 'react-textarea-counter';
import { Dropdown } from "semantic-ui-react";
import ImageUploader from 'react-images-upload';
import { Stepper, Step, StepLabel } from '@material-ui/core';
import { getHeader } from "../../services/UserDataService";
import { FcPlus } from "react-icons/all";
import Swal from "sweetalert2";
import '../../css/AddRestaurant.css';


export default class AddRestaurant extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "", nameValid: false,
            description: "", descriptionValid: false,
            installment: 0,
            active: false,
            image: "",
            formValid: false,
            errorMsg: {},
            buttonLoading: false,
            step: 0,
            addresses: [{
                city: "",
                street: "",
                streetNo: ""
            }],
            newAddress: false,
            city: "", cityValid: false,
            street: "", streetValid: false,
            streetNo: "", streetNoValid: false,
            addressSaveButtonLoading: false
        }
    }

    loadAddresses = () => {
        axios.get("/addresses", { headers: getHeader() })
            .then(response => {
                this.setState({
                    addresses: response.data
                })
            }).catch(error => {
            console.log(error.response.data)
        })
    };

    componentDidMount() {
        this.loadAddresses();
    }

    validateForm = () => {
        const { nameValid, descriptionValid } = this.state;
        this.setState({
            formValid: nameValid && descriptionValid && this.state.city.length > 0
        })
    };

    updateName = (name) => {
        this.setState({name}, ValidationService.validateRestaurantName)
    };

    updateDescription = (description) => {
      this.setState({description}, ValidationService.validateDescription)
    };

    updateCity = (city) => {
        this.setState({city}, ValidationService.validateCity)
    };

    updateStreet = (street) => {
        this.setState({street}, ValidationService.validateStreet)
    };

    updateStreetNo = (streetNo) => {
        this.setState({streetNo}, ValidationService.validateStreetNo)
    };

    createData = (city, street, streetNo) => {
        return { city, street, streetNo };
    };

    handleImageUpload = (e) => {
        if(e.length > 0) {
            let reader = new FileReader();
            reader.readAsDataURL(e[0]);
            reader.onloadend = () => {
                this.setState({ image: reader.result })
            }
        }
    };

    handleNewAddress = (e) => {
        e.preventDefault();
        this.setState({ addressSaveButtonLoading: true });
        axios.post("/address", {
            city: this.state.city,
            street: this.state.street,
            streetNo: this.state.streetNo
        }, { headers: getHeader() })
            .then(() => {
                this.setState({
                    newAddress: false,
                    addressSaveButtonLoading: false,
                    cityValid: false,
                    streetValid: false,
                    streetNoValid: false,
                    errorMsg: {}});
                this.loadAddresses();
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                }).then(() => this.setState({ addressSaveButtonLoading: false }))
            })
    };

    handleSubmit = (e) => {
        e.preventDefault();
      console.log(this.state)
    };

    render() {
        const installmentOptions = [
            { text: '0%', value: 0 },
            { text: '10%', value: 10 },
            { text: '20%', value: 20 },
            { text: '30%', value: 30 },
            { text: '40%', value: 40 },
            { text: '50%', value: 50 },
            { text: '60%', value: 60 },
            { text: '70%', value: 70 },
            { text: '80%', value: 80 },
            { text: '90%', value: 90 },
            { text: '100%', value: 100 }
        ];

        const addressTable = [];
        for(let i=0; i<this.state.addresses.length; i++) {
            addressTable.push(this.createData(this.state.addresses[i].city, this.state.addresses[i].street, this.state.addresses[i].streetNo));
        }

        return (
            <div>
                <Stepper className="addRestaurantSteps" alternativeLabel activeStep={this.state.step}>
                    <Step> <StepLabel>{Translate('step1')}</StepLabel> </Step>
                    <Step> <StepLabel>{Translate('step2')}</StepLabel> </Step>
                </Stepper>
                { this.state.step === 0 ?
                    <div>
                        <div className="addRestaurantFirstDiv">
                            <form>
                                <FormGroup className="addRestaurantFormLabels">
                                    <FormLabel>{Translate('name')} *</FormLabel>
                                    <FormControl autoFocus value={this.state.name} onChange={event => this.updateName(event.target.value)} />
                                    <ValidationMessage valid={this.state.nameValid} message={this.state.errorMsg.name} />
                                </FormGroup>

                                <FormGroup className="addRestaurantFormLabels">
                                    <FormLabel>{Translate('description')} *</FormLabel>
                                    <TextareaCounter countLimit={255} value={this.state.description} onChange={event => this.updateDescription(event.target.value)} />
                                    <ValidationMessage valid={this.state.descriptionValid} message={this.state.errorMsg.description}/>
                                </FormGroup>
                            </form>
                            <div>
                                <p className="addRestaurantFormLabels addRestaurantLabels">{Translate('installment')} *</p>
                                <Dropdown value={this.state.installment} selection options={installmentOptions}
                                          onChange={(e, {value}) => this.setState({ installment: value})}/>
                            </div>
                        </div>
                        <div className="addRestaurantSecondDiv">
                            <p className="addRestaurantFormLabels addRestaurantLabels">Logo</p>
                            <ImageUploader singleImage label={Translate('imageDetails')} buttonText={Translate('chooseImage')} withPreview
                                           fileTypeError={Translate('wrongExtension')} fileSizeError={Translate('wrongSize')}
                                            onChange={(e) => this.handleImageUpload(e)}/>
                        </div>
                    </div>
                 : <div>
                        <div className="addRestaurantFirstDiv">
                            { this.state.newAddress ?
                            <div>
                                <form>
                                    <FormGroup className="addRestaurantFormLabels newAddressForm">
                                        <FormLabel>{Translate('city')} *</FormLabel>
                                        <FormControl autoFocus value={this.state.city} onChange={event => this.updateCity(event.target.value)} />
                                        <ValidationMessage valid={this.state.cityValid} message={this.state.errorMsg.city}/>
                                    </FormGroup>

                                    <FormGroup className="addRestaurantFormLabels newAddressForm">
                                        <FormLabel>{Translate('street')} *</FormLabel>
                                        <FormControl value={this.state.street} onChange={event => this.updateStreet(event.target.value)} />
                                        <ValidationMessage valid={this.state.streetValid} message={this.state.errorMsg.street}/>
                                    </FormGroup>

                                    <FormGroup className="addRestaurantFormLabels newAddressForm">
                                        <FormLabel>{Translate('streetNo')} *</FormLabel>
                                        <FormControl maxLength={4} value={this.state.streetNo} onChange={event => this.updateStreetNo(event.target.value)} />
                                        <ValidationMessage valid={this.state.streetNoValid} message={this.state.errorMsg.streetNo}/>
                                    </FormGroup>
                                </form>
                                <div className="newAddressButtons">
                                    <Button className="newAddressConfirmButton" disabled={!this.state.cityValid || !this.state.streetValid
                                    || !this.state.streetNoValid} onClick={this.handleNewAddress}>
                                        { this.state.addressSaveButtonLoading ? <Spinner className="spinner" animation="border" /> : Translate('save')}</Button>
                                    <Button className="newAddressCancelButton" onClick={() => this.setState({
                                        newAddress: false, cityValid: false, streetValid: false, streetNoValid: false, errorMsg: {} })}>{Translate('cancel')}</Button>
                                </div>
                            </div> :
                            <div>
                                <p className="addRestaurantFormLabels">{Translate('chooseAddress')}
                                    <OverlayTrigger placement="top" overlay={<Tooltip id="button-tooltip">{Translate('addNewAddress')}</Tooltip>}>
                                        <FcPlus className="addRestaurantIcon" onClick={() => this.setState({
                                            newAddress: true, city: "", street: "", streetNo: ""})}/>
                                    </OverlayTrigger>
                                </p>
                                <ListGroup className="addRestaurantAddressList">
                                    { addressTable.map(address => (
                                        <ListGroupItem action onClick={() => this.setState({
                                            city: address.city,
                                            street: address.street,
                                            streetNo: address.streetNo
                                        }, this.validateForm)}>
                                            {address.city}, {address.street} {address.streetNo}
                                        </ListGroupItem>
                                    ))}
                                </ListGroup>
                            </div> }
                        </div>
                        <div className="addRestaurantSecondDiv">
                            <h1>hours</h1>
                        </div>
                    </div> }
                <div>
                    { this.state.step === 0 ? <Button className="buttons" onClick={() => this.setState({ step: 1 })}>{Translate('next')}</Button>
                        : <div>
                            <Button className="buttons addRestaurantButton" onClick={() => this.setState({ step: 0 })}>{Translate('previous')}</Button>
                            <Button className="buttons addRestaurantButton" disabled={!this.state.formValid} onClick={this.handleSubmit}>
                                { this.state.buttonLoading ? <Spinner className="spinner" animation="border" /> : Translate('confirm') }</Button>
                        </div> }
                </div>
            </div>
        )
    }
}