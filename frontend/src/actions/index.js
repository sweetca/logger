const CONNECTED = 'CONNECTED';
const REQUEST_LOGS = 'REQUEST_LOGS';
const FETCH_LOGS = 'FETCH_LOGS';
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

const newLog = (log) => {
    return {
        type: NEW_LOG,
        log: log
    };
};

export { CONNECTED, connected, REQUEST_LOGS, requestLogs , FETCH_LOGS, fetchLogs, NEW_LOG, newLog };
