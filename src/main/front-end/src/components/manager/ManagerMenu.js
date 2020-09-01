import React, { Component, Fragment } from "react";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import { Menu } from 'semantic-ui-react';
import Translate from "../../i18n/Translate";
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
                            </Breadcrumb>
                        </div>
                        <div className="menu">
                            <Menu pointing secondary vertical>

                            </Menu>
                        </div>
                        { activeItem === '' ?
                            <div className="managerContent">
                                <h1>{Translate('managerPanelLabel')}</h1>
                            </div> : null }
                    </div>
                </div>
            </Fragment>
        )
    }
}