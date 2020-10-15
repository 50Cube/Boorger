import React, { Component, Fragment } from "react";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Menu } from 'semantic-ui-react';
import Translate from "../../i18n/Translate";
import AddRestaurant from "./AddRestaurant";
import ListRestaurants from "./ListRestaurants";
import ListReservations from "./ListReservations";
import '../../css/ManagerBreadcrumbs.css';
import '../../css/ManagerMenu.css';

export default class ManagerMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            activeItem: ''
        }
    }

    handleItemClick = (e, { name }) => this.setState({ activeItem: name });

    render() {
        const { activeItem } = this.state;
        return (
            <Fragment>
                <div className="managerImage">
                    <div className="managerMenu">
                        <div className="managerBreadcrumbs">
                            <Breadcrumb>
                                <Breadcrumb.Item active>{Translate('managerPanel')}</Breadcrumb.Item>
                                { activeItem === 'listRestaurants' ? <Breadcrumb.Item active>{Translate('listRestaurants')}</Breadcrumb.Item> : null }
                                { activeItem === 'addRestaurant' ? <Breadcrumb.Item active>{Translate('addRestaurant')}</Breadcrumb.Item> : null }
                                { activeItem === 'listReservations' ? <Breadcrumb.Item active>{Translate('listReservations')}</Breadcrumb.Item> : null}
                            </Breadcrumb>
                        </div>
                        <div className="menu">
                            <Menu pointing secondary vertical>
                                <Menu.Item name="listRestaurants" active={activeItem === 'listRestaurants'} onClick={this.handleItemClick}>
                                    {Translate('listRestaurants')}
                                </Menu.Item>
                                <Menu.Item name="addRestaurant" active={activeItem === 'addRestaurant'} onClick={this.handleItemClick}>
                                    {Translate('addRestaurant')}
                                </Menu.Item>
                                <Menu.Item name="listReservations" active={activeItem === 'listReservations'} onClick={this.handleItemClick}>
                                    {Translate('listReservations')}
                                </Menu.Item>
                            </Menu>
                        </div>
                        { activeItem === '' ?
                            <div className="managerContent">
                                <h1>{Translate('managerPanelLabel1')}</h1>
                                <h2>{Translate('managerPanelLabel2')}</h2>
                            </div> : null }
                        { activeItem === 'listRestaurants' ?
                            <div className="managerContent">
                                <ListRestaurants />
                            </div> : null }
                        { activeItem === 'addRestaurant' ?
                            <div className="managerContent">
                                <AddRestaurant />
                            </div> : null }
                        { activeItem === 'listReservations' ?
                            <div className="managerContent">
                                <ListReservations />
                            </div> : null}
                    </div>
                </div>
            </Fragment>
        )
    }
}