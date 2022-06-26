import { all } from 'redux-saga/effects';

import { authSaga } from './auth/index';
import { errorSaga } from './error/index';
import { langSaga } from './lang/index';
import { navSaga } from './nav/index';

export default function* rootSaga() {
    yield all([
        authSaga(),
        errorSaga(),
        langSaga(),
        navSaga()
    ]);
};