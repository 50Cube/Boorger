import React, { Component } from "react";
import axios from 'axios';
import {getHeader} from "../../services/UserDataService";
import Swal from "sweetalert2";
import {Spinner, Button, ListGroup, FormGroup} from "react-bootstrap";
import Translate from '../../i18n/Translate';
import BootstrapSwitchButton from "bootstrap-switch-button-react";
import defaultImage from '../../assets/noImage.png';
import TextareaCounter from "react-textarea-counter";
import ValidationService from "../../services/ValidationService";
import ValidationMessage from "../../i18n/ValidationMessage";
import {Dropdown} from "semantic-ui-react";
import '../../css/RestaurantDetails.css';
import TimeInput from "react-time-input";

export default class RestaurantDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            description: "", descriptionValid: true, descriptionCopy: "",
            installment: 0, installmentCopy: 0,
            active: "",
            photo: "",
            address: {},
            mondayStart: "", mondayEnd: "", mondayStartCopy: "", mondayEndCopy: "",
            tuesdayStart: "", tuesdayEnd: "", tuesdayStartCopy: "", tuesdayEndCopy: "",
            wednesdayStart: "", wednesdayEnd: "", wednesdayStartCopy: "", wednesdayEndCopy: "",
            thursdayStart: "", thursdayEnd: "", thursdayStartCopy: "", thursdayEndCopy: "",
            fridayStart: "", fridayEnd: "", fridayStartCopy: "", fridayEndCopy: "",
            saturdayStart: "", saturdayEnd: "", saturdayStartCopy: "", saturdayEndCopy: "",
            sundayStart: "", sundayEnd: "", sundayStartCopy: "", sundayEndCopy: "",
            tables: [{}],
            creationDate: "",
            signature: "",
            loaded: false,
            editing: false,
            buttonLoading: false,
            errorMsg: {}
        }
    }

    componentDidMount() {
        axios.get("/restaurant/" + this.props.restaurantName, { headers: getHeader() })
            .then(response => {
                this.setState({
                    description: response.data.description,
                    descriptionCopy: response.data.description,
                    installment: response.data.installment,
                    installmentCopy: response.data.installment,
                    active: response.data.active,
                    photo: response.data.photo,
                    address: response.data.addressDTO,
                    mondayStart: response.data.hoursDTO.mondayStart, mondayEnd: response.data.hoursDTO.mondayEnd,
                    mondayStartCopy: response.data.hoursDTO.mondayStart, mondayEndCopy: response.data.hoursDTO.mondayEnd,
                    tuesdayStart: response.data.hoursDTO.tuesdayStart, tuesdayEnd: response.data.hoursDTO.tuesdayEnd,
                    tuesdayStartCopy: response.data.hoursDTO.tuesdayStart, tuesdayEndCopy: response.data.hoursDTO.tuesdayEnd,
                    wednesdayStart: response.data.hoursDTO.wednesdayStart, wednesdayEnd: response.data.hoursDTO.wednesdayEnd,
                    wednesdayStartCopy: response.data.hoursDTO.wednesdayStart, wednesdayEndCopy: response.data.hoursDTO.wednesdayEnd,
                    thursdayStart: response.data.hoursDTO.thursdayStart, thursdayEnd: response.data.hoursDTO.thursdayEnd,
                    thursdayStartCopy: response.data.hoursDTO.thursdayStart, thursdayEndCopy: response.data.hoursDTO.thursdayEnd,
                    fridayStart: response.data.hoursDTO.fridayStart, fridayEnd: response.data.hoursDTO.fridayEnd,
                    fridayStartCopy: response.data.hoursDTO.fridayStart, fridayEndCopy: response.data.hoursDTO.fridayEnd,
                    saturdayStart: response.data.hoursDTO.saturdayStart, saturdayEnd: response.data.hoursDTO.saturdayEnd,
                    saturdayStartCopy: response.data.hoursDTO.saturdayStart, saturdayEndCopy: response.data.hoursDTO.saturdayEnd,
                    sundayStart: response.data.hoursDTO.sundayStart, sundayEnd: response.data.hoursDTO.sundayEnd,
                    sundayStartCopy: response.data.hoursDTO.sundayStart, sundayEndCopy: response.data.hoursDTO.sundayEnd,
                    tables: response.data.tableDTOs,
                    creationDate: response.data.creationDate,
                    signature: response.data.signature,
                    loaded: true
                })
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                })
            })
    }

    createData = (number, capacity, active) => {
      return { number, capacity, active };
    };

    handleTableActivityChange = (e) => {
        const tables = this.state.tables;
        for(let i=0; i<this.state.tables.length; i++)
            if(i === e-1) {
                tables[i].active = !this.state.tables[i].active
            }
        this.setState({
            tables: tables
        })
    };

    cancelEdit = () => {
        this.setState({
            editing: false,
            errorMsg: {},
            descriptionValid: false,
            description: this.state.descriptionCopy,
            installment: this.state.installmentCopy,
            mondayStart: this.state.mondayStartCopy, mondayEnd: this.state.mondayEndCopy,
            tuesdayStart: this.state.tuesdayStartCopy, tuesdayEnd: this.state.tuesdayEndCopy,
            wednesdayStart: this.state.wednesdayStartCopy, wednesdayEnd: this.state.wednesdayEndCopy,
            thursdayStart: this.state.thursdayStartCopy, thursdayEnd: this.state.thursdayEndCopy,
            fridayStart: this.state.fridayStartCopy, fridayEnd: this.state.fridayEndCopy,
            saturdayStart: this.state.saturdayStartCopy, saturdayEnd: this.state.saturdayEndCopy,
            sundayStart: this.state.sundayStartCopy, sundayEnd: this.state.sundayEndCopy
        })
    };

    updateDescription = (description) => {
        this.setState({description}, ValidationService.validateDescription)
    };

    handleEdit = () => {
        this.setState({ buttonLoading: true });
        axios.put("restaurant/edit", {
            name: this.props.restaurantName,
            description: this.state.description,
            installment: this.state.installment,
            hoursDTO: {
                mondayStart: this.state.mondayStart, mondayEnd: this.state.mondayEnd,
                tuesdayStart: this.state.tuesdayStart, tuesdayEnd: this.state.tuesdayEnd,
                wednesdayStart: this.state.wednesdayStart, wednesdayEnd: this.state.wednesdayEnd,
                thursdayStart: this.state.thursdayStart, thursdayEnd: this.state.thursdayEnd,
                fridayStart: this.state.fridayStart, fridayEnd: this.state.fridayEnd,
                saturdayStart: this.state.saturdayStart, saturdayEnd: this.state.saturdayEnd,
                sundayStart: this.state.sundayStart, sundayEnd: this.state.sundayEnd
            },
            tableDTOs: this.state.tables,
            signature: this.state.signature
        }, { headers: getHeader()})
            .then(() => {
                this.setState({ buttonLoading: false, editing: false })
            })
            .catch(error => {
                Swal.fire({
                    icon: "error",
                    title: error.response.data
                }).then(() => this.setState({ buttonLoading: false }))
            })
    };

    render() {
        const restaurantTables = [];
        for(let i=0; i<this.state.tables.length; i++) {
            restaurantTables.push(this.createData(this.state.tables[i].number, this.state.tables[i].capacity, this.state.tables[i].active));
        }
        const image = "data:image/png;base64," + this.state.photo;
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

        if(this.state.loaded) {
            return (
                <div>
                    <div className="restaurantDetailsFirstDiv">
                        { this.state.photo ? <img className="restaurantDetailsImage" src={image}  alt="loading"/> :
                            <img className="restaurantDetailsImage" src={defaultImage} alt="loading" /> }
                        <p className="restaurantDetailsNameLabel">{this.props.restaurantName}</p>
                        <p className="restaurantDetailsAddressLabel">{this.state.address.city}, {this.state.address.street} {this.state.address.streetNo}</p>
                        { this.state.editing ?
                        <div>
                            <TextareaCounter countLimit={255} initialValue={this.state.description} onChange={event => this.updateDescription(event.target.value)} />
                            <ValidationMessage valid={this.state.descriptionValid} message={this.state.errorMsg.description}/>
                        </div> : <p>{this.state.description}</p> }
                            { this.state.editing ?
                                <div>
                                    <p>{Translate('installment')}
                                    <Dropdown value={this.state.installment} selection options={installmentOptions}
                                              onChange={(e, {value}) => this.setState({ installment: value})}/>
                                    </p>
                                </div> :
                                <div>
                                    <p>{Translate('installment')}: {this.state.installment} {Translate('pln')}</p>
                                </div> }
                        { this.state.active ? <p className="restaurantDetailsActive">{Translate('restaurantActive')}</p>
                            : <p className="restaurantDetailsInactive">{Translate('restaurantInactive')}</p> }
                        <p>{Translate('creationDateRestaurant')}: {this.state.creationDate}</p>
                    </div>
                    <div className="restaurantDetailsSecondDiv">
                        <p className="restaurantDetailsTablesLabel">{Translate('restaurantTables')}</p>
                        <ListGroup className="restaurantDetailsListGroup">
                            { restaurantTables.map(element => (
                                <ListGroup.Item className="restaurantDetailsListItem">
                                    <p className="restaurantDetailsListItemLabel">{Translate('number')}: {element.number}</p>
                                    <p className="restaurantDetailsListItemLabel restaurantDetailsListMiddleItemLabel">{Translate('capacity')}: {element.capacity}</p>
                                    <p className="restaurantDetailsListItemLabel">
                                        {Translate('active')}:
                                        { this.state.editing ?
                                            <BootstrapSwitchButton checked={element.active} size="sm" onstyle="dark" onlabel={Translate('true')}
                                                                   offlabel={Translate('false')} onChange={() => this.handleTableActivityChange(element.number)} />
                                            : Translate(element.active.toString()) }
                                    </p>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                        <p className="restaurantDetailsHoursLabel">{Translate('openingHours')}</p>
                        <div className="restaurantDetailsDays">
                            <p>{Translate('monday')}</p>
                            <p>{Translate('tuesday')}</p>
                            <p>{Translate('wednesday')}</p>
                            <p>{Translate('thursday')}</p>
                            <p>{Translate('friday')}</p>
                            <p>{Translate('saturday')}</p>
                            <p>{Translate('sunday')}</p>
                        </div>
                        { this.state.editing ?
                        <div className="restaurantDetailsHours">
                            <p className="editableHours"><TimeInput initTime={this.state.mondayStart} onTimeChange={(value) => this.setState({
                                mondayStart: value})}/> - <TimeInput initTime={this.state.mondayEnd}
                                                                     onTimeChange={(value) => this.setState({mondayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.tuesdayStart} onTimeChange={(value) => this.setState({
                                tuesdayStart: value})}/> - <TimeInput initTime={this.state.tuesdayEnd}
                                                                     onTimeChange={(value) => this.setState({tuesdayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.wednesdayStart} onTimeChange={(value) => this.setState({
                                wednesdayStart: value})}/> - <TimeInput initTime={this.state.wednesdayEnd}
                                                                     onTimeChange={(value) => this.setState({wednesdayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.thursdayStart} onTimeChange={(value) => this.setState({
                                thursdayStart: value})}/> - <TimeInput initTime={this.state.thursdayEnd}
                                                                     onTimeChange={(value) => this.setState({thursdayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.fridayStart} onTimeChange={(value) => this.setState({
                                fridayStart: value})}/> - <TimeInput initTime={this.state.fridayEnd}
                                                                     onTimeChange={(value) => this.setState({fridayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.saturdayStart} onTimeChange={(value) => this.setState({
                                saturdayStart: value})}/> - <TimeInput initTime={this.state.saturdayEnd}
                                                                     onTimeChange={(value) => this.setState({saturdayEnd: value})}/></p>
                            <p className="editableHours"><TimeInput initTime={this.state.sundayStart} onTimeChange={(value) => this.setState({
                                sundayStart: value})}/> - <TimeInput initTime={this.state.sundayEnd}
                                                                     onTimeChange={(value) => this.setState({sundayEnd: value})}/></p>
                        </div> :
                        <div className="restaurantDetailsHours">
                            <p>{this.state.mondayStart} - {this.state.mondayEnd}</p>
                            <p>{this.state.tuesdayStart} - {this.state.tuesdayEnd}</p>
                            <p>{this.state.wednesdayStart} - {this.state.wednesdayEnd}</p>
                            <p>{this.state.thursdayStart} - {this.state.thursdayEnd}</p>
                            <p>{this.state.fridayStart} - {this.state.fridayEnd}</p>
                            <p>{this.state.saturdayStart} - {this.state.saturdayEnd}</p>
                            <p>{this.state.sundayStart} - {this.state.sundayEnd}</p>
                        </div> }
                    </div>
                    { this.state.editing ?
                        <div>
                            <Button className="buttons restaurantDetailsConfirmButton" disabled={!this.state.descriptionValid}
                            onClick={this.handleEdit}>
                                { this.state.buttonLoading ? <Spinner animation="border" /> : Translate('save')}</Button>
                            <Button className="buttons restaurantDetailsCancelButton"
                            onClick={this.cancelEdit}>{Translate('cancel')}</Button>
                        </div> :
                        <div>
                            <Button className="buttons restaurantDetailsButton" onClick={() => this.setState({ editing: true })}>{Translate('edit')}</Button>
                            <Button className="buttons restaurantDetailsButton" onClick={() => this.props.handleBackButtonClick()}>{Translate('back')}</Button>
                        </div> }
                </div>
            ) } else return ( <Spinner animation="border" /> )
    }
}