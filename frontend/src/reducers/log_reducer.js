import { CONNECTED, REQUEST_LOGS, FETCH_LOGS, NEW_LOG } from '../actions/index';

const INIT_STATE = {
    logs: [],
    lastLogs: [],
    stompClient: null,
    userID: ''
};

export default (state = INIT_STATE, action) => {

    switch (action.type) {
        case CONNECTED:
            return Object.assign({}, state, {
                stompClient: action.stomp,
                userID: action.userID
            });
        case REQUEST_LOGS:
            state.stompClient.send('/app/last-logs', {}, JSON.stringify({period: action.periodInSeconds, user: state.userID}));
            break;
        case FETCH_LOGS:
            return Object.assign({}, state, {
                lastLogs: action.logs
            });
        case NEW_LOG:
            let logs= [];
            if (state.logs.length > 800) {
                logs = state.logs.slice(400, logs.length - 1);
            } else {
                logs = state.logs.slice(0);
            }

            logs.unshift(action.log);
            return Object.assign({}, state, {
                logs: logs
            });
        default:
            break;
    }

    return state;
};