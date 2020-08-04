import React, { Component } from "react";
import { Nav, Navbar, NavDropdown } from 'react-bootstrap';
import "../css/NavigationBar.css";
import {getAccessLevels, getUser} from "../services/UserDataService";
import Cookies from "universal-cookie/lib";
import { BsFillPeopleFill, BsFillPersonFill } from "react-icons/bs";

export default class NavigationBar extends Component {

    constructor(props) {
        super(props);
        this.cookies = new Cookies();
        this.state = {
            currentRole: props.role
        }
    }

    logout = () => {
        this.cookies.remove("jwt");
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
        if(this.state.currentRole === process.env.REACT_APP_CLIENT_ROLE) {
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
        if(this.state.currentRole === process.env.REACT_APP_MANAGER_ROLE) {
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
        if(this.state.currentRole === process.env.REACT_APP_ADMIN_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link href="/listAccounts">Users</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    username = () => {
        if(getUser() !== "") {
            return (
                <Nav className="ml-auto">
                    <NavDropdown alignRight title={
                        <span><BsFillPersonFill className="navIcons" /> {getUser()}</span>}>
                        <NavDropdown.Item>profil</NavDropdown.Item>
                        <NavDropdown.Item>haslo</NavDropdown.Item>
                    </NavDropdown>
                </Nav>
            )
        }
    };

    accessLevel = () => {
        if(getUser() !== "" && getAccessLevels().length > 1) {
            return (
                <Nav className="ml-auto">
                    <NavDropdown alignRight title={
                        <span><BsFillPeopleFill className="navIcons" /> {this.state.currentRole}</span>}>
                        {getAccessLevels().includes(process.env.REACT_APP_ADMIN_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeAccessLevel(process.env.REACT_APP_ADMIN_ROLE)}>Admin</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_MANAGER_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeAccessLevel(process.env.REACT_APP_MANAGER_ROLE)}>Manager</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_CLIENT_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeAccessLevel(process.env.REACT_APP_CLIENT_ROLE)}>Client</NavDropdown.Item> : null}
                    </NavDropdown>
                </Nav>
            )
        }
    };

    changeAccessLevel = (level) => {
        this.props.callbackFromParent(level);
    };

    static getDerivedStateFromProps(props, state) {
        if(props.role !== state.currentRole)
            return { currentRole: props.role }
    }

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
                        {this.accessLevel()}
                        {this.username()}
                        {this.logoutLabel()}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}
