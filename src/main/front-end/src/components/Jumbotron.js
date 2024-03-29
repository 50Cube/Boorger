import React from 'react';
import { Jumbotron as Jumbo, Container } from "react-bootstrap";
import styled from 'styled-components';
import image from '../assets/navbarImage.jpg';
import Translate from '../i18n/Translate';

const Styles = styled.div`
    .jumbo {
        background: url(${image}) no-repeat;
        background-size: cover;
        font-weight: bold;
        color: #ccc;
        height: 200px;
        position: relative;
        z-index: -2;
    }
    
    .overlay {
        background-color: #000;
        opacity: 0.3;
        position: absolute;
        left: 0;
        top: 0;
        right: 0;
        bottom: 0;
        z-index: -1;
    }
`;

export const Jumbotron = () => (
    <Styles>
        <Jumbo fluid className="jumbo">
            <div className="overlay" />
            <Container>
                <h1>{Translate('motto')}</h1>
            </Container>
        </Jumbo>
    </Styles>
);
