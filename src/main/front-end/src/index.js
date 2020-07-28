import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import HttpsApp from "./HttpsApp";
import axios from 'axios';

axios.defaults.baseURL = "http://localhost:8080/boorger";

ReactDOM.render(
    <React.StrictMode>
        <HttpsApp />
    </React.StrictMode>,
    document.getElementById("root")
);
