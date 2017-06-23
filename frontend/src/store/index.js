import Perf from 'react-addons-perf';
import { createStore, applyMiddleware } from 'redux';
import { createLogger } from 'redux-logger';
import reducers from '../reducers';

const middleware = [];
if (process.env.REACT_APP_DEBUG === 'true') {
    window.Perf = Perf;
    middleware.push(createLogger());
}

export default () => {
    return createStore(
        reducers,
        applyMiddleware(...middleware)
    );
};
