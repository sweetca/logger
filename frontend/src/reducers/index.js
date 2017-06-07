import { combineReducers } from 'redux';
import LogReducer from './log_reducer';

const rootReducer = combineReducers({
    data: LogReducer
});

export default rootReducer;