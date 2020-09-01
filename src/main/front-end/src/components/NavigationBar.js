import React, { Component } from "react";
import { Nav, Navbar, NavDropdown } from 'react-bootstrap';
import "../css/NavigationBar.css";
import {getAccessLevels, getHeader, getLanguage, getUser, hashString} from "../services/UserDataService";
import Cookies from "universal-cookie/lib";
import { BsFillPeopleFill, BsFillPersonFill, BsPencilSquare, BsBriefcaseFill, BsReverseLayoutTextWindowReverse } from "react-icons/bs";
import { FaRegFlag, FiLogIn, FiLogOut } from "react-icons/all";
import Translate from '../i18n/Translate';
import RoleContext from "../services/RoleContext";
import NavLink from "react-bootstrap/NavLink";
import {Link} from "react-router-dom";
import axios from 'axios';

export default class NavigationBar extends Component {

    constructor(props) {
        super(props);
        this.cookies = new Cookies();
    }

    logout = () => {
        this.cookies.remove("jwt");
        sessionStorage.removeItem("role");
    };

    guestNavbar = () => {
        if(getUser() === "") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link as={Link} to="/login"><FiLogIn className="navIcons"/> {Translate('login')}</Nav.Link>
                    </Nav.Item>
                    <Nav.Item>
                        <Nav.Link as={Link} to="/register"><BsPencilSquare className="navIcons"/> {Translate('register')}</Nav.Link>
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
                        <Nav.Link as={Link} to="/managerMenu"><BsReverseLayoutTextWindowReverse className="navIcons" />{Translate('managerPanel')}</Nav.Link>
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
                        <NavLink as={Link} to="/adminMenu"><BsBriefcaseFill className="navIcons" /> {Translate('adminPanel')}</NavLink>
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
                        <span><BsFillPeopleFill className="navIcons" /> {Translate(role)}</span>}>
                        {getAccessLevels().includes(process.env.REACT_APP_ADMIN_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_ADMIN_ROLE)}>{Translate('admin')}</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_MANAGER_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_MANAGER_ROLE)}>{Translate('manager')}</NavDropdown.Item> : null}
                        {getAccessLevels().includes(process.env.REACT_APP_CLIENT_ROLE) ?
                            <NavDropdown.Item onClick={() => this.changeRole(process.env.REACT_APP_CLIENT_ROLE)}>{Translate('client')}</NavDropdown.Item> : null}
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
                    <Nav.Item>
                        <Nav.Link as={Link} to="/userMenu"><BsFillPersonFill className="navIcons" /> {getUser()}</Nav.Link>
                    </Nav.Item>
                </Nav>
            )
        }
    };

    languageNavbar = () => {
        return (
            <Nav className="ml-auto">
                <NavDropdown title={
                    <span><FaRegFlag className="navIcons" /> {Translate(getLanguage())}</span>}>
                    <NavDropdown.Item disabled={true}>{Translate('chooseLanguage')}</NavDropdown.Item>
                    <NavDropdown.Item onClick={() => this.changeLanguage("pl")}>Polski</NavDropdown.Item>
                    <NavDropdown.Item onClick={() => this.changeLanguage("en")}>English</NavDropdown.Item>
                </NavDropdown>
            </Nav>
        )
    };

    changeLanguage = (language) => {
        localStorage.setItem("lang", language);
        if(getUser()) {
            axios.put("/language/" + getUser(), null ,{ headers: getHeader() })
                .catch(() => {
                    console.log("ERROR: Account not found")
                })
        }
        window.location.reload();
    };

    logoutNavbar = () => {
        if(getUser() !== "") {
            return (
                <Nav className="ml-auto">
                    <Nav.Item>
                        <Nav.Link href="/" onClick={this.logout}><FiLogOut className="navIcons"/> {Translate('logout')}</Nav.Link>
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
