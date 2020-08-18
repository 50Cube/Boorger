import React from "react";
import ReactDOM from "react-dom";
import "./css/index.css";
import 'semantic-ui-css/semantic.min.css';
import HttpsApp from "./HttpsApp";
import axios from 'axios';

axios.defaults.baseURL = process.env.REACT_APP_BASE_URL;

ReactDOM.render(
    <React.StrictMode>
        <HttpsApp />
    </React.StrictMode>,
    document.getElementById("root")
);
