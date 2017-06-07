import React, { Component } from 'react';
import './Logs.css';

class Log extends Component {
    render() {
        const logStyle = this.props.log.logType.charAt(0);
        let style = 'status ';
        switch (logStyle) {
            case 'E':
                style += 'error';
                break;
            case 'W':
                style += 'warn';
                break;
            default:
                style += 'info';
        }
        return (
            <div className="Log">
                <div className="date">{this.props.log.date}</div>
                <div className={style}>{this.props.log.logType}</div>
                <div className="msg">{this.props.log.msg}</div>
            </div>
        );
    }
}

class Logs extends Component {
    render() {
        return (
            <div className="Logs">
                {this.props.logs.map((l) => {
                    return (<Log key={l.timestamp} log={l}/>);
                })}
            </div>
        );
    }
}

export default Logs;
