import React, { Component } from "react";
import axios from 'axios';
import {Jumbotron} from "./Jumbotron";
import DayPicker from 'react-day-picker';
import 'react-day-picker/lib/style.css';
import {getHeader, getLanguageShortcut} from "../services/UserDataService";
import Timekeeper from 'react-timekeeper';
import Translate from '../i18n/Translate';
import { Button, Spinner, ListGroup } from 'react-bootstrap';
import {Dropdown} from "semantic-ui-react";
import '../css/BookRestaurant.css';
import Swal from "sweetalert2";
import RestaurantTablePuzzle from "./RestaurantTablePuzzle";

const WEEKDAYS_SHORT = { pl: ['Pn', 'Wt', 'Śr', 'Czw', 'Pt', 'Sob', 'Ndz'] };
const WEEKDAYS_LONG = { pl: ['Poniedziałek', 'Wtorek', 'Środa', 'Czwartek', 'Piątek', 'Sobota', 'Niedziela']};
const MONTHS = { pl: ['Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec', 'Lipiec', 'Sierpień',
        'Wrzesień', 'Październik', 'Listopad', 'Grudzień']};

export default class BookRestaurant extends Component {

    constructor(props) {
        super(props);
        this.state = {
            name: sessionStorage.getItem("restaurantName"),
            selectedDay: new Date(),
            selectedHour: '12:00',
            duration: 30,
            tables: [{ }],
            buttonLoading: false,
            loaded: false
        }
    }

    handleButtonClick = () => {
        this.setState({ buttonLoading: true });
        axios.get("/restaurant/" + this.state.name, { headers: getHeader() })
            .then(response => {
                this.setState({
                    buttonLoading: false,
                    loaded: true,
                    tables: response.data.tableDTOs
                })
            }).catch(error => {
            Swal.fire({
                icon: "error",
                title: error.response.data
            }).then(() => this.setState({ buttonLoading: false }))
        })
    };

    createData = (number, capacity, active) => {
        return { number, capacity, active };
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
        let hours = parseInt(durationConcat[0]) + parseInt(Math.floor(this.state.duration/60));
        let minutes = parseInt(durationConcat[1]) + parseInt(this.state.duration%60);
        if(minutes.toString().length === 1) minutes = minutes + '0';

        let tableList = [];
        for(let i=0; i<this.state.tables.length; i++) {
            if(this.state.tables[i].active)
                tableList.push(this.createData(this.state.tables[i].number, this.state.tables[i].capacity, this.state.tables[i].active));
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
                    <p className="bookSearchLabel">
                        {Translate('selectedDate')}: {this.state.selectedDay.getDate()}.{this.state.selectedDay.getMonth() + 1}.{this.state.selectedDay.getFullYear()
                    }  {this.state.selectedHour} - {hours}:{minutes}
                    </p>
                    : null }
                    <ListGroup>
                        { tableList.map(element => (
                            <ListGroup.Item>
                                <RestaurantTablePuzzle
                                    number={element.number}
                                    capacity={element.capacity}
                                />
                            </ListGroup.Item>
                        )) }
                    </ListGroup>
                </div>
            </div>
        );
    }
}