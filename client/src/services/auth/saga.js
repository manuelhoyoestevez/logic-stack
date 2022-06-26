import get from 'lodash.get';
import { put, all, call, takeEvery } from 'redux-saga/effects';

import { LOGIN, LOGOUT } from './constants';
import { loginApi } from './api';
import { setAuth } from './actions';
import { replace } from '../nav/actions';
import { handleError } from '../error/actions';
import { showLoading, hideLoading } from '../loading/actions';
import { AUTH_URL, MAIN_URL } from '../../config/urls';

export function* loginSaga(action) {
    yield put(showLoading());
    const { payload } = action;
    const body = {
        username: payload.username,
        password: payload.password
    }

    const req = yield call(loginApi, body);
    if (req.error) { yield put(handleError(req)); return; }

    const data = get(req, 'data');
    yield put(setAuth(data));

    yield put(replace(MAIN_URL));
    yield put(hideLoading());
}

export function* logoutSaga() {
    yield put(replace(AUTH_URL));
}

export function* authSaga() {
    yield all([
        takeEvery(LOGIN, loginSaga),
        takeEvery(LOGOUT, logoutSaga)
    ]);
}