import React, { Component } from "react";
import { Nav, Navbar } from 'react-bootstrap';
import "../css/NavigationBar.css";
import {getFirstAccessLevel, getUser} from "../UserDataService";

export default class NavigationBar extends Component {

    logout = () => {
        localStorage.removeItem("user");
    };

    guestNavbar = () => {
        if(getUser() === "") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link href="/login">Logowanie</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link href="/register">Rejestracja</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    clientNavbar = () => {
        if(getFirstAccessLevel() === "CLIENT") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link href="/" onClick={this.logout}>Wyloguj</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    render() {
        return (
            <Navbar expand="lg">
                <Navbar.Brand href="/">BOORGER</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    {this.guestNavbar()}
                    {this.clientNavbar()}
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
