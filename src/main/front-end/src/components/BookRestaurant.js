import React, { Component } from "react";
import axios from 'axios';
import {Jumbotron} from "./Jumbotron";
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';
import {getHeader, getLanguage, getLanguageShortcut, getUser} from "../services/UserDataService";
import Timekeeper from 'react-timekeeper';
import Translate from '../i18n/Translate';
import {Button, Spinner, ListGroup} from 'react-bootstrap';
import {Dropdown} from "semantic-ui-react";
import Swal from "sweetalert2";
import RestaurantTablePuzzle from "./RestaurantTablePuzzle";
import { GridList, GridListTile } from "@material-ui/core";
import Sticky from 'react-sticky-el';
import { BsTrash } from 'react-icons/all';
import '../css/BookRestaurant.css';

const WEEKDAYS_SHORT = { pl: ['Pn', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob', 'Ndz'] };
const WEEKDAYS_LONG = { pl: ['Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota', 'Niedziela']};
const MONTHS = { pl: ['Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec', 'Lipiec', 'Sierpień',
        'Wrzesień', 'Październik', 'Listopad', 'Grudzień']};

export default class BookRestaurant extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: sessionStorage.getItem("restaurantName"),
            installment: 0,
            selectedDay: new Date(),
            selectedHour: '12:00',
            duration: 30,
            freeTables: [{}],
            menu: [{}],
            buttonLoading: false,
            loaded: false,
            selectedTable: "",
            selectedMenu: [],
            total: 0
        }
    }

    handleButtonClick = () => {
        this.setState({ buttonLoading: true });

        let durationConcat = this.state.selectedHour.split(':');
        let hours = parseInt(durationConcat[0]) + parseInt(Math.floor((parseInt(durationConcat[1]) + this.state.duration)/60));
        hours = hours%24;
        if(hours.toString().length === 1) hours = '0' + hours;
        let minutes = (parseInt(durationConcat[1]) + parseInt(this.state.duration))%60;
        if(minutes.toString().length === 1) minutes = minutes + '0';

        axios.post("/tables", {
            restaurantName: this.state.name,
            startDate: this.state.selectedDay.toISOString().substring(0, 10) + ' ' + this.state.selectedHour,
            endDate: this.state.selectedDay.toISOString().substring(0, 10) + ' ' + hours + ':' + minutes
            }, { headers: getHeader() })
            .then(response => {
            this.setState({
                freeTables: response.data,
                startDate: this.state.selectedDay.toISOString().substring(0, 10) + ' ' + this.state.selectedHour,
                endDate: this.state.selectedDay.toISOString().substring(0, 10) + ' ' + hours + ':' + minutes
            })
        }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            })
        });

        axios.get("/restaurant/" + this.state.name, { headers: getHeader() })
            .then(response => {
                this.setState({
                    buttonLoading: false,
                    loaded: true,
                    menu: response.data.dishDTOs,
                    installment: response.data.installment
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            }).then(() => this.setState({ buttonLoading: false }))
        })
    };

    createTableData = (number, capacity, active) => {
        return { number, capacity, active };
    };

    createMenuData = (name, description, price) => {
        return { name, description, price };
    };

    handleDishDelete = (element) => {
        let dishes = [...this.state.selectedMenu];
        let index = dishes.indexOf(element);
        dishes.splice(index, 1);
        this.setState({
            selectedMenu: dishes,
            total: this.state.total - element.price
        });
    };

    handleReservation = () => {
        Swal.fire({
            icon: 'question',
            title: getLanguage() === ('en-US') ? 'Are you sure?' : 'Czy jesteś pewien?',
            showCancelButton: true,
            cancelButtonColor: '#000000',
            confirmButtonText: getLanguage() === ('en-US') ? 'yes' : 'Tak',
            cancelButtonText: getLanguage() === ('en-US') ? 'No' : 'Nie',
        }).then(result => {
            if(result.isConfirmed) {
                axios.post("/reservation", {
                    guestNumber: this.state.selectedTable.capacity,
                    startDate: this.state.startDate,
                    endDate: this.state.endDate,
                    totalPrice: this.state.total,
                    restaurantName: this.state.name,
                    tableNumber: this.state.selectedTable.number,
                    status: "BOOKED",
                    clientDTO: {
                        login: getUser()
                    }
                }, { headers: getHeader() })
                    .then(response => {
                        Swal.fire({
                            icon: "success",
                            title: response.data
                        })
                    }).catch(error => {
                        Swal.fire({
                            icon: "error",
                            title: error.response.data
                        })
                })
            }
        })
    };

    render() {
        const durationOptions = [
            { text: '30', value: 30 },
            { text: '45', value: 45 },
            { text: '60', value: 60 },
            { text: '75', value: 75 },
            { text: '90', value: 90 },
            { text: '105', value: 105 },
            { text: '120', value: 120 },
            { text: '135', value: 135 },
            { text: '150', value: 150 },
            { text: '165', value: 165 },
            { text: '180', value: 180 }
        ];

        let durationConcat = this.state.selectedHour.split(':');
        let hours = parseInt(durationConcat[0]) + parseInt(Math.floor((parseInt(durationConcat[1]) + this.state.duration)/60));
        hours = hours%24;
        if(hours.toString().length === 1) hours = '0' + hours;
        let minutes = (parseInt(durationConcat[1]) + parseInt(this.state.duration))%60;
        if(minutes.toString().length === 1) minutes = minutes + '0';

        let tableList = [];
        for(let i=0; i<this.state.freeTables.length; i++) {
            if(this.state.freeTables[i].active)
                tableList.push(this.createTableData(this.state.freeTables[i].number, this.state.freeTables[i].capacity, this.state.freeTables[i].active));
        }

        let menuList = [];
        for(let i=0; i<this.state.menu.length; i++) {
            menuList.push(this.createMenuData(this.state.menu[i].name, this.state.menu[i].description, this.state.menu[i].price));
        }

        return (
            <div>
                <Jumbotron />
                <div className="bookDateDiv">
                    <div className="bookDateFirstDiv">
                        <p className="bookDateLabel">{Translate('chooseDate')}</p>
                        <DayPicker locale="pl" months={MONTHS[getLanguageShortcut()]} weekdaysLong={WEEKDAYS_LONG[getLanguageShortcut()]}
                                   weekdaysShort={WEEKDAYS_SHORT[getLanguageShortcut()]} firstDayOfWeek={0} selectedDays={this.state.selectedDay}
                                   onDayClick={(day) => this.setState({ selectedDay: day })}/>
                    </div>
                    <div className="bookDateSecondDiv">
                        <Timekeeper hour24Mode forceCoarseMinutes coarseMinutes={5}
                                    onChange={(time) => this.setState({ selectedHour: time.formatted24 })}
                        doneButton={() => (
                            <div className="bookDuration">
                                <p>{Translate('duration')}</p>
                                <Dropdown className="bookDropdown" defaultValue={this.state.duration} selection options={durationOptions}
                                          onChange={(e, {value}) => this.setState({ duration: value})}/>
                            </div>
                        )}/>
                    </div>
                    <Button className="buttons bookButton" onClick={this.handleButtonClick}>
                        { this.state.buttonLoading ? <Spinner className="spinner" animation="border" /> : Translate('checkTables')}
                    </Button>
                </div>

                <div className="bookTablesDiv">
                    { this.state.loaded ?
                        <div>
                            <div className="bookSelectFirstDiv">
                                <p className="bookSearchLabel">
                                    {Translate('selectedDate')}: {this.state.selectedDay.getDate()}.{this.state.selectedDay.getMonth() + 1}.{this.state.selectedDay.getFullYear()
                                }  {this.state.selectedHour} - {hours}:{minutes}
                                </p>
                                <p className="bookSelectTable">{Translate('selectTable')}</p>
                                <GridList className="bookGridList" cols={5}>
                                    { tableList.map(element => (
                                        <GridListTile className="bookGridListTile" onClick={() => this.setState({ selectedTable: element })}>
                                            <RestaurantTablePuzzle
                                                number={element.number}
                                                capacity={element.capacity}
                                            />
                                        </GridListTile>
                                    )) }
                                </GridList>
                                <p className="bookMenuLabel">Menu</p>
                                <ListGroup>
                                    { menuList.map(element => (
                                        <ListGroup.Item className="restaurantMenuItem bookListGroup" onClick={() => this.setState(prevState => ({
                                        selectedMenu: [...prevState.selectedMenu, element],
                                        total: this.state.total + element.price})) }>
                                            <div className="restaurantMenuFirstDiv">
                                                <p className="restaurantMenuNameLabel">{element.name}</p>
                                                <p>{element.description}</p>
                                            </div>
                                            <div className="restaurantMenuSecondDiv">
                                                <p className="restaurantMenuPriceLabel">{element.price} {Translate('pln')}</p>
                                            </div>
                                        </ListGroup.Item>
                                    ))}
                                </ListGroup>
                            </div>

                            <div className="bookSelectSecondDiv">
                                <Sticky>
                                    <div className="bookCartDiv">
                                        <p className="cartLabel">{Translate('cart')}</p>
                                        <hr/>
                                        <p className="totalPriceLabel">{this.state.name}</p>
                                        <p>{Translate('selectedTable')}: {this.state.selectedTable.number}</p>
                                        <hr/>
                                        <ListGroup className="bookCartListGroup">
                                            { this.state.selectedMenu.map(element => (
                                                <div className="cartDish">
                                                    <div  className="cartName">
                                                        <p>{element.name}</p>
                                                    </div>
                                                    <div className="cartPrice">
                                                        <p>{element.price} {Translate('pln')}</p>
                                                    </div>
                                                    <div className="cartTrash">
                                                        <BsTrash onClick={() => this.handleDishDelete(element)} />
                                                    </div>
                                                </div>
                                            ))}
                                        </ListGroup>
                                        <hr/>
                                        <p className="totalPriceLabel">{Translate('total')}: {this.state.total} {Translate('pln')}</p>
                                        <p>{Translate('installment')}: {this.state.total * this.state.installment/100} {Translate('pln')}</p>
                                        <Button className="buttons bookButton" onClick={this.handleReservation}
                                                disabled={!this.state.selectedTable || this.state.selectedMenu.length === 0}>
                                            {Translate('book')}
                                        </Button>
                                    </div>
                                </Sticky>
                            </div>
                        </div>
                    : null }
                </div>
            </div>
        );
    }
}