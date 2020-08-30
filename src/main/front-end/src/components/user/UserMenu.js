import React, {Component, Fragment} from "react";
import { Menu } from 'semantic-ui-react';
import Translate from '../../i18n/Translate';
import MyProfile from "./MyProfile";
import ChangePassword from "./ChangePassword";
import '../../css/UserMenu.css';
import '../../css/Menu.css';

export default class UserMenu extends Component {

    constructor(props) {
        super(props);
        this.state = ({
            activeItem: 'myProfile'
        })
    }

    handleItemClick = (e, { name }) => this.setState({ activeItem: name });

    render() {
        const { activeItem } = this.state;
        return (
            <Fragment>
                <div className="userImage">
                    <div className="userMenu">
                        <div className="menu">
                            <Menu pointing secondary vertical>
                                <Menu.Item name="myProfile" active={activeItem === 'myProfile'} onClick={this.handleItemClick}>{Translate('myProfile')}</Menu.Item>
                                <Menu.Item name="changePassword"  active={activeItem === 'changePassword'} onClick={this.handleItemClick}>{Translate('changePassword')}</Menu.Item>
                            </Menu>
                        </div>
                        { activeItem === 'myProfile' ?
                            <div className="userContent">
                                <MyProfile />
                            </div> : null }
                        { activeItem === 'changePassword' ?
                            <div className="userContent">
                                <ChangePassword />
                            </div> : null }
                    </div>
                </div>
            </Fragment>
        );
    }
}