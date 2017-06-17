const CONNECTED = 'CONNECTED';
const REQUEST_LOGS = 'REQUEST_LOGS';
const FETCH_LOGS = 'FETCH_LOGS';
const FETCH_AVAILABLE_LOGS = 'FETCH_AVAILABLE_LOGS';
const SWITCH_LOGS = 'SWITCH_LOGS';
const NEW_LOG = 'NEW_LOG';

const connected = (stomp, userID) => {
    return {
        type: CONNECTED,
        stomp: stomp,
        userID: userID
    };
};

const requestLogs = (periodInSeconds) => {
    return {
        type: REQUEST_LOGS,
        periodInSeconds: periodInSeconds
    };
};

const fetchLogs = (logs) => {
    return {
        type: FETCH_LOGS,
        logs: logs
    };
};

const fetchAvailableLogs = (logs) => {
    return {
        type: FETCH_AVAILABLE_LOGS,
        logs: logs
    };
};

const switchLogs = (id) => {
    return {
        type: SWITCH_LOGS,
        id: id
    };
};

const newLog = (log) => {
    return {
        type: NEW_LOG,
        log: log
    };
};

export {
    CONNECTED, connected, REQUEST_LOGS, requestLogs ,
    FETCH_LOGS, fetchLogs, FETCH_AVAILABLE_LOGS, fetchAvailableLogs,
    SWITCH_LOGS, switchLogs, NEW_LOG, newLog
};
