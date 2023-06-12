import { put, all, takeEvery } from 'redux-saga/effects';
import { push, replace, goBack } from 'react-router-redux';

import { REPLACE, NAVIGATE, BACK } from './constants';

export function* replaceSaga(action) {
    const { payload } = action;
    yield put(replace(payload));
}

export function* navigateSaga(action) {
    const { payload } = action;
    yield put(push(payload));
}

export function* backSaga() {
    yield put(goBack());
}

export function* navSaga() {
    yield all([
        takeEvery(REPLACE, replaceSaga),
        takeEvery(NAVIGATE, navigateSaga),
        takeEvery(BACK, backSaga)
    ]);
}