import React, {Component, Fragment} from "react";
import { Menu } from 'semantic-ui-react';
import Translate from '../../i18n/Translate';
import ListAccounts from "./ListAccounts";
import AddAccount from "./AddAccount";
import Breadcrumb from "react-bootstrap/Breadcrumb";
import '../../css/AdminBreadcrumbs.css';

export default class AdminMenu extends Component {

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
                <div className="adminImage">
                    <div className="adminMenu">
                        <div className="adminBreadcrumbs">
                            <Breadcrumb>
                                <Breadcrumb.Item active>{Translate('adminPanel')}</Breadcrumb.Item>
                                { activeItem === 'accountList' ? <Breadcrumb.Item active>{Translate('listAccounts')}</Breadcrumb.Item> : null }
                                { activeItem === 'addAccount' ? <Breadcrumb.Item active>{Translate('addAccount')}</Breadcrumb.Item> : null }
                            </Breadcrumb>
                        </div>
                        <div className="menu">
                            <Menu pointing secondary vertical>
                                <Menu.Item name="accountList" active={activeItem === 'accountList'} onClick={this.handleItemClick}>{Translate('listAccounts')}</Menu.Item>
                                <Menu.Item name="addAccount"  active={activeItem === 'addAccount'} onClick={this.handleItemClick}>{Translate('addAccount')}</Menu.Item>
                            </Menu>
                        </div>
                        { activeItem === '' ?
                        <div className="adminContent">
                            <h1>{Translate('adminPanelLabel1')}</h1>
                            <h2>{Translate('adminPanelLabel2')}</h2>
                        </div> : null }
                        { activeItem === 'accountList' ?
                        <div className="adminContent">
                            <ListAccounts />
                        </div> : null }
                        { activeItem === 'addAccount' ?
                        <div className='adminContent'>
                            <AddAccount />
                        </div> : null}
                    </div>
                </div>
            </Fragment>
        );
    }
}