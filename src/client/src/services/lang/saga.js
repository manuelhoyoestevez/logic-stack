import { put, takeEvery } from 'redux-saga/effects';
import { SET_LOCALE, LOAD_TRANSLATIONS } from 'react-redux-i18n';

import { SET_TRANSLATIONS } from './constants';
import { setLanguage } from './actions';
import Config from '../../config/config';
import Translations from 'Translations';

export function* setTranslationsSaga(action) {
    const lang = action.payload.language || Config.defaultLanguage;

    yield put({ type: LOAD_TRANSLATIONS, translations: Translations });
    yield put({ type: SET_LOCALE, locale: lang });
    yield put(setLanguage(lang));

    if (action.payload.refresh) {
        location.reload();
    }
}

export function* langSaga() {
    yield takeEvery(SET_TRANSLATIONS, setTranslationsSaga);
}