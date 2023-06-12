import { combineReducers } from 'redux';
import { i18nReducer } from 'react-redux-i18n';
import { connectRouter } from 'connected-react-router';

import { authReducer } from './auth/index';
import { langReducer } from './lang/index';
import { loadingReducer } from './loading/index';
import { navReducer } from './nav/index';
import { toastReducer } from './toast/index';

const rootReducer = (history) => combineReducers({
    i18n: i18nReducer,
    router: connectRouter(history),
    authReducer,
    langReducer,
    loadingReducer,
    navReducer,
    toastReducer
});

export default rootReducer;