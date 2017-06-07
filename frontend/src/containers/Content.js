import React, { Component } from 'react';
import { connect as ReduxConnect } from 'react-redux';
import { bindActionCreators } from 'redux';
import { connected, newLog, requestLogs, fetchLogs } from '../actions/index';
import Logs from '../components/log/Logs';
import './Content.css';
import loading from './loader.svg';
import refresh from './refresh.png';

const SockJS = window.SockJS;
const Stomp = window.Stomp;

class Content extends Component {
    constructor(props) {
        super(props);
        this.state = {
            realTime: true,
            period: '5'
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        if (!this.props.stompClient) {
            setTimeout(() => {
                let api_url = process.env.REACT_APP_API_URL;
                const userID = Math.random().toString(36).slice(2);

                const socket = new SockJS(api_url);
                const stompClient = Stomp.over(socket);
                stompClient.connect({}, (frame) => {
                    stompClient.subscribe('/topic/log', (data) => {
                        this.props.newLog(JSON.parse(data.body));
                    });
                    stompClient.subscribe('/topic/last-logs-' + userID, (data) => {
                        this.props.fetchLogs(JSON.parse(data.body));
                    });
                });

                this.props.connected(stompClient, userID);
            }, 1000);
        }
    }

    switchMode(realTime) {
        const period = parseInt(this.state.period, 10);
        this.props.requestLogs(period < 1 ? 1 : period);

        this.setState({
            realTime: realTime,
            period: period < 1 ? '1' : this.state.period
        });
    }

    handleChange(event) {
        this.setState({
            realTime: this.state.realTime,
            period: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        const period = parseInt(this.state.period, 10);
        this.props.requestLogs(period < 1 ? 1 : period);
    }

    renderList() {
        if (!this.props.stompClient) {
            return (
                <div className="loaderBox">
                    <img src={loading} className="loader" alt="loading" />
                </div>
            );
        }

        if (this.state.realTime) {
            return (
                <Logs logs={this.props.logs}/>
            );
        } else {
            return (
                <Logs logs={this.props.lastLogs}/>
            );
        }
    }

    render() {
        return (
            <div className="Content">
                <div className="navigation">
                    {!this.state.realTime &&
                    <a href="#" className="actionBtn" onClick={() => this.switchMode(true)}>goto RealTime mode</a>
                    }
                    {!this.state.realTime &&
                    <form onSubmit={this.handleSubmit}>
                        <label for="period">period :</label>
                        <input id="period" value={this.state.period} onChange={this.handleChange}/>
                    </form>
                    }
                    {!this.state.realTime &&
                    <a href="#" className="refreshBtn" onClick={() => this.switchMode(false)}>
                        <img src={refresh} className="refreshImg" alt="refresh" />
                    </a>
                    }
                    {this.state.realTime &&
                    <a href="#" className="actionBtn" onClick={() => this.switchMode(false)}>goto LastPeriod mode</a>
                    }
                </div>
                { this.renderList() }
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    return {
        logs: state.data.logs,
        lastLogs: state.data.lastLogs,
        stompClient: state.data.stompClient
    };
};

const mapDispatchToProps = (dispatch) => {
    return bindActionCreators(
        { connected, newLog, requestLogs, fetchLogs },
        dispatch
    );
};

export default ReduxConnect(mapStateToProps, mapDispatchToProps)(Content);