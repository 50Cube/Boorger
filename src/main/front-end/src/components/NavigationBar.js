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

    logoutLabel = () => {
        if(getUser() !== "") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link href="/" onClick={this.logout}>Wyloguj</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    clientNavbar = () => {
        if(getFirstAccessLevel() === process.env.REACT_APP_CLIENT_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>client page</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    managerNavbar = () => {
        if(getFirstAccessLevel() === process.env.REACT_APP_MANAGER_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>manager page</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    adminNavbar = () => {
        if(getFirstAccessLevel() === process.env.REACT_APP_ADMIN_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>admin page</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    username = () => {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>{getUser()}</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
    };

    render() {
        return (
            <Navbar expand="lg">
                <Navbar.Brand href="/">BOORGER</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        {this.guestNavbar()}
                        {this.clientNavbar()}
                        {this.managerNavbar()}
                        {this.adminNavbar()}
                        {this.username()}
                        {this.logoutLabel()}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
