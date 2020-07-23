import React, { Component } from "react";
import { Nav, Navbar } from 'react-bootstrap';
import "../css/NavigationBar.css";

export default class NavigationBar extends Component {

    render() {
        return (
            <Navbar expand="lg">
                <Navbar.Brand href="/">BOORGER</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        <Nav.Item>
                            <Nav.Link href="/login">Logowanie</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/register">Rejestracja</Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
