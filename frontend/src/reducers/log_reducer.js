import { CONNECTED, REQUEST_LOGS, FETCH_LOGS, FETCH_AVAILABLE_LOGS, SWITCH_LOGS, NEW_LOG } from '../actions/index';

const browser_limit = 400;

const INIT_STATE = {
    logs: [],
    lastLogs: [],
    stompClient: null,
    userID: '',
    availableLogs: [],
    currentLog: {
        id: null,
        fileName: null,
        shortName: null,
        logs: [],
        lastLogs: [],
    }
};

export default (state = INIT_STATE, action) => {

    switch (action.type) {

        case CONNECTED:
            return Object.assign({}, state, {
                stompClient: action.stomp,
                userID: action.userID
            });

        case REQUEST_LOGS:
            state.stompClient.send('/app/last-logs', {}, JSON.stringify({period: action.periodInSeconds, user: state.userID, id: state.currentLog.id}));
            break;

        case FETCH_LOGS:
            const logsToFetch = Object.assign([], state.availableLogs);

            const targetToFetch = logsToFetch.filter((l) => {
                return l.id === state.currentLog.id;
            })[0];

            targetToFetch.lastLogs = action.logs.slice(0, action.logs.length > browser_limit ? browser_limit : action.logs.length);

            return Object.assign({}, state, {
                availableLogs: logsToFetch
            });

        case FETCH_AVAILABLE_LOGS:
            const startLogs = action.logs.map(l => {
                return Object.assign({}, l, {
                    logs: [],
                    lastLogs: []
                });
            });
            return Object.assign({}, state, {
                availableLogs: startLogs,
                currentLog: action.logs[0]
            });

        case SWITCH_LOGS:
            let currentLog = state.availableLogs.filter((l) => {
                return l.id === action.id;
            })[0];

            currentLog = Object.assign({}, currentLog, {
                logs: null,
                lastLogs: null,
            });

            return Object.assign({}, state, {
                currentLog: currentLog
            });

        case NEW_LOG:
            const availableLogs = Object.assign([], state.availableLogs);

            const targetData = availableLogs.filter((l) => {
                return l.id === action.log.id;
            })[0];

            let logs = [];
            if (targetData.logs.length > browser_limit) {
                logs = targetData.logs.slice((browser_limit / 2), logs.length - 1);
            } else {
                logs = targetData.logs.slice(0);
            }

            logs.unshift(action.log);
            targetData.logs = logs;

            return Object.assign({}, state, {
                availableLogs: availableLogs
            });

        default:
            break;
    }

    return state;
};