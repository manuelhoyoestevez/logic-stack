import * as constants from './constants';

export const handleError = (error) => ({ type: constants.HANDLE_ERROR, payload: error });