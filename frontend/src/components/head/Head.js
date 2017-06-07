import React, { Component } from 'react';
import './Head.css';
import logo from './logo.png';

class Header extends Component {
    render() {
        return (
            <div className="Head">
                <img src={logo} alt="logo"/>
            </div>
        );
    }
}

export default Header;