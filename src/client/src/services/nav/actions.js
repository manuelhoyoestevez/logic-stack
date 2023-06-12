import * as constants from './constants';

export const replace = (url) => ({ type: constants.REPLACE, payload: url });
export const navigate = (url) => ({ type: constants.NAVIGATE, payload: url });
export const back = () => ({ type: constants.BACK });