import React, { Component } from "react";
import Translate from "../i18n/Translate";
import image from '../assets/navbarImage.jpg';
import { BsFillForwardFill, BsSearch } from "react-icons/all";
import { Jumbotron } from "../components/Jumbotron";
import { InputGroup, Form, Button } from "react-bootstrap";
import '../css/Home.css';
import '../css/App.css';

export default class Home extends Component {

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
                        <InputGroup className="mb-3">
                            <InputGroup.Prepend>
                                <InputGroup.Text>
                                    <BsSearch/>
                                </InputGroup.Text>
                            </InputGroup.Prepend>
                            <Form.Control type="text"/>
                            <Button className="buttons" type="button" >{Translate('search')}</Button>
                        </InputGroup>
                    </div>
                </div>
            </div>
        )
    }
}
