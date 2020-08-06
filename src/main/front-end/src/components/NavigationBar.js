import React, { Component } from "react";
import { Nav, Navbar, NavDropdown } from 'react-bootstrap';
import "../css/NavigationBar.css";
import {getAccessLevels, getLanguage, getUser, hashString} from "../services/UserDataService";
import Cookies from "universal-cookie/lib";
import { BsFillPeopleFill, BsFillPersonFill } from "react-icons/bs";
import Translate from '../i18n/Translate';
import RoleContext from "../services/RoleContext";
import NavLink from "react-bootstrap/NavLink";
import {Link} from "react-router-dom";

export default class NavigationBar extends Component {

    constructor(props) {
        super(props);
        this.cookies = new Cookies();
    }

    logout = () => {
        this.cookies.remove("jwt");
    };

    guestNavbar = () => {
        if(getUser() === "") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link as={Link} to="/login">{Translate('login')}</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link as={Link} to="/register">Rejestracja</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    clientNavbar = (role) => {
        if(role === process.env.REACT_APP_CLIENT_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>client page</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    managerNavbar = (role) => {
        if(role === process.env.REACT_APP_MANAGER_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link>manager page</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    adminNavbar = (role) => {
        if(role === process.env.REACT_APP_ADMIN_ROLE) {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <NavLink as={Link} to="/listAccounts" exact>Users</NavLink>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    accessLevelNavbar  = (role) => {
        if(getUser() !== "" && getAccessLevels().length > 1) {
            return (
                <Nav className="ml-auto">
                    <NavDropdown alignRight title={
                        <span><BsFillPeopleFill className="navIcons" /> {role}</span>}>
                        {getAccessLevels().includes(process.env.REACT_APP_ADMIN_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_ADMIN_ROLE)}>Admin</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_MANAGER_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_MANAGER_ROLE)}>Manager</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_CLIENT_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_CLIENT_ROLE)}>Client</NavDropdown.Item> : null}
                    </NavDropdown>
                </Nav>
            )
        }
    };

    changeRole = (role) => {
      sessionStorage.setItem("role", hashString(role));
      window.location.replace("/");
    };

    usernameNavbar = () => {
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

    languageNavbar = () => {
        return (
            <Nav className="ml-auto">
                <NavDropdown title={getLanguage()}>
                    <NavDropdown.Item disabled={true}>Wybierz jezyk</NavDropdown.Item>
                    <NavDropdown.Item onClick={() => this.changeLanguage("pl")}>pl</NavDropdown.Item>
                    <NavDropdown.Item onClick={() => this.changeLanguage("en")}>en</NavDropdown.Item>
                </NavDropdown>
            </Nav>
        )
    };

    changeLanguage = (language) => {
        localStorage.setItem("lang", language);
        window.location.reload();
    };

    logoutNavbar = () => {
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

    render() {
        return (
            <RoleContext.Consumer>
                {({ role }) => (
                <Navbar expand="lg">
                    <Navbar.Brand as={Link} to="/">BOORGER</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ml-auto">
                            {this.clientNavbar(role)}
                            {this.managerNavbar(role)}
                            {this.adminNavbar(role)}
                            {this.accessLevelNavbar(role)}
                            {this.usernameNavbar()}
                            {this.languageNavbar()}
                            {this.guestNavbar()}
                            {this.logoutNavbar()}
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
                )}
            </RoleContext.Consumer>
        )
    }
}
