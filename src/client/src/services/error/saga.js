import { put, all, takeEvery } from 'redux-saga/effects';

import { HANDLE_ERROR } from './constants';
import { showToast } from '../toast/actions';
import { showLoadingKO } from '../loading/actions';

export function* handleErrorSaga(action) {
    yield put(showLoadingKO());
    const { payload } = action;
    const { error, toast } = payload;

    if (error.response) {
        console.log(error.response.data);
        console.log(error.response.status);
        console.log(error.response.headers);
    } else if (error.request) {
        console.log(error.request);
    } else {
        console.log('Error', error.message);
    }

    if (toast) {
        yield put(showToast({ open: true, key: toast.key, type: toast.type }));
    }
}

export function* errorSaga() {
    yield all([
        takeEvery(HANDLE_ERROR, handleErrorSaga)
    ]);
}