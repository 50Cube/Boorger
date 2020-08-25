import React, { Component } from "react";
import Translate from '../i18n/Translate';
import '../css/AccessDenied.css';

export default class AccessDenied extends Component {

    render() {
        return (
            <div className="div403">
                <div className="maincontainer">
                    <div className="bat">
                        <img className="wing leftwing"
                             src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading"/>
                            <img className="body"
                                 src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-body.png" alt="bat"/>
                                <img className="wing rightwing"
                            src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading" />
                    </div>
                    <div className="bat">
                        <img className="wing leftwing"
                             src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading"/>
                            <img className="body"
                                 src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-body.png" alt="bat"/>
                                <img className="wing rightwing"
                                     src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading"/>
                    </div>
                    <div className="bat">
                        <img className="wing leftwing"
                             src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading"/>
                            <img className="body"
                                 src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-body.png" alt="bat"/>
                                <img className="wing rightwing"
                                     src="https://www.blissfullemon.com/wp-content/uploads/2018/09/bat-wing.png" alt="loading"/>
                    </div>
                    <img className="foregroundimg"
                         src="https://www.blissfullemon.com/wp-content/uploads/2018/09/HauntedHouseForeground.png"
                         alt="haunted house"/>

                </div>
                <h1 className="errorcode">ERROR 403</h1>
                <div className="errortext">{Translate('accessDenied')}</div>
            </div>
        )
    }
}
