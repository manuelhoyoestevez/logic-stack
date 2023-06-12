import * as constants from './constants';

export const setTranslations = (language) => ({ type: constants.SET_TRANSLATIONS, payload: language });
export const setLanguage = (language) => ({ type: constants.SET_LANGUAGE, payload: language });