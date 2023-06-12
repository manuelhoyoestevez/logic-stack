import createSagaMiddleware from 'redux-saga';
import { createStore, applyMiddleware, compose } from 'redux';

import Config from './config/config';
import rootSaga from './services/sagas';
import rootReducer from './services/reducers';
import { createBrowserHistory } from 'history';
import { routerMiddleware } from 'connected-react-router';
import { loadState, saveState } from './utils/session';
import { showToast } from './services/toast/actions';
import { showLoadingKO } from './services/loading/actions';

export const history = createBrowserHistory({ basename: Config.basename });

export default function configureStore() {
    const persistedState = loadState();
    const sagaMiddleware = createSagaMiddleware();
    const composeEnhancers = typeof window === 'object' && window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__']
        ? window['__REDUX_DEVTOOLS_EXTENSION_COMPOSE__']({})
        : compose;

    const enhancer = composeEnhancers(applyMiddleware(sagaMiddleware, routerMiddleware(history)));
    const store = createStore(rootReducer(history), persistedState, enhancer);
    store.subscribe(() => { saveState(store.getState()); });
    sagaMiddleware.run(rootSaga, store.dispatch).toPromise().catch(e => {
        store.dispatch(showLoadingKO());
        store.dispatch(showToast({ open: true, key: 'errors.default', type: 'error' }));
    });

    return store;
}