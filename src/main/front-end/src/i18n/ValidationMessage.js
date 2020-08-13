import React from "react";
import Translate from "./Translate";
import '../css/ValidationMessages.css';

export default function ValidationMessage(props) {
    if(!props.valid) {
        return(
            <div className='error-msg'>{props.message ? Translate(props.message) : ''}</div>
        )
    } else return null;
}