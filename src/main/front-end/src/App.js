import React, {Component, Fragment} from "react";
import { BrowserRouter as Router, Switch, Route} from "react-router-dom";
import { PrivateRoute } from './PrivateRoute';
import { RestrictedRoute } from "./RestrictedRoute";
import Home from "./components/Home";
import NotFound from "./components/NotFound";
import NavigationBar from "./components/NavigationBar";
import Login from "./components/guest/Login";
import Register from "./components/guest/Register";
import Confirm from "./components/guest/Confirm";
import AccessDenied from "./components/AccessDenied";
import {getCurrentAccessLevel, getLanguage} from "./services/UserDataService";
import { I18nProvider } from "./i18n";
import RoleContext from "./services/RoleContext";
import Cookies from "universal-cookie/cjs";
import AdminMenu from "./components/admin/AdminMenu";
import ResetPassword from "./components/guest/ResetPassword";
import ChangeResetPassword from "./components/guest/ChangeResetPassword";
import UserMenu from "./components/user/UserMenu";
import ManagerMenu from "./components/manager/ManagerMenu";
import Restaurant from "./components/Restaurant";
import './css/App.css';


export default class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            role: getCurrentAccessLevel()
        };
        this.cookies = new Cookies();
        if(!sessionStorage.getItem("role")) {
            this.cookies.remove("jwt");
        }
    }


    render() {
        return (
            <RoleContext.Provider value={this.state}>
                <I18nProvider locale={getLanguage()}>
                    <Fragment>
                        <Router>
                            <NavigationBar />
                            <div className="mainDiv">
                                <Switch>
                                    <Route exact path="/" component={Home} />
                                    <RestrictedRoute path="/login" component={Login} />
                                    <RestrictedRoute path="/register" component={Register} />
                                    <RestrictedRoute path="/confirm" component={Confirm} />
                                    <RestrictedRoute path="/reset" component={ResetPassword} />

                                    <Route path="/changeResetPassword" component={ChangeResetPassword} />
                                    <Route path="/restaurant" component={Restaurant} />

                                    <PrivateRoute path="/adminMenu" component={AdminMenu} accessLevels={[process.env.REACT_APP_ADMIN_ROLE]} />
                                    <PrivateRoute path="/managerMenu" component={ManagerMenu} accessLevels={[process.env.REACT_APP_MANAGER_ROLE]} />
                                    <PrivateRoute path="/userMenu" component={UserMenu} accessLevels={[process.env.REACT_APP_ADMIN_ROLE,
                                                                    process.env.REACT_APP_MANAGER_ROLE, process.env.REACT_APP_CLIENT_ROLE]} />

                                    <Route path="/accessDenied" component={AccessDenied} />
                                    <Route component={NotFound} />
                                </Switch>
                            </div>
                        </Router>
                    </Fragment>
                </I18nProvider>
            </RoleContext.Provider>
        );
    }
};
