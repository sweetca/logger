import { createStore, applyMiddleware } from 'redux';
import { createLogger } from 'redux-logger';
import reducers from '../reducers';

const middleware = [];
if (process.env.REACT_APP_DEBUG === 'true') {
    middleware.push(createLogger());
}

export default () => {
    return createStore(
        reducers,
        applyMiddleware(...middleware)
    );
};
