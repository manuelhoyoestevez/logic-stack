import * as constants from './constants';

export const showLoading = () => ({ type: constants.SHOW_LOADING });
export const showLoadingOK = () => ({ type: constants.SHOW_LOADING_OK });
export const showLoadingKO = () => ({ type: constants.SHOW_LOADING_KO });
export const hideLoading = () => ({ type: constants.HIDE_LOADING });