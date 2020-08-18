import React, {Component, Fragment} from "react";
import { Menu } from 'semantic-ui-react';
import Translate from '../../i18n/Translate';
import ListAccounts from "./ListAccounts";

export default class AdminMenu extends Component {

    constructor(props) {
        super(props);
        this.state = ({
            activeItem: ''
        })
    }

    handleItemClick = (e, { name }) => this.setState({ activeItem: name });

    render() {
        const { activeItem } = this.state;
        return (
            <Fragment>
                <div className="adminMenu">
                    <div className="menu">
                        <Menu pointing secondary vertical>
                            <Menu.Item name="accountList" active={activeItem === 'accountList'} onClick={this.handleItemClick}>{Translate('listAccounts')}</Menu.Item>
                            <Menu.Item name="addAccount"  active={activeItem === 'addAccount'} onClick={this.handleItemClick}>{Translate('addAccount')}</Menu.Item>
                            <Menu.Item name="placeholder"  active={activeItem === 'placeholder'} onClick={this.handleItemClick}>PLACEHOLDER</Menu.Item>
                        </Menu>
                    </div>
                    { activeItem === '' ?
                    <div className="adminContent">
                        <h1>{Translate('adminPanelLabel1')}</h1>
                        <h2>{Translate('adminPanelLabel2')}</h2>
                    </div> : null }
                    { activeItem === 'accountList' ?
                    <div className="adminContent">
                        <ListAccounts render={false} />
                    </div> : null }
                </div>
            </Fragment>
        );
    }
}