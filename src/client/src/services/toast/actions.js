import * as constants from './constants';

export const showToast = (toast) => ({ type: constants.SHOW_TOAST, payload: toast });
export const showToastParams = (toast) => ({ type: constants.SHOW_TOAST_PARAMS, payload: toast });
export const hideToast = () => ({ type: constants.HIDE_TOAST });