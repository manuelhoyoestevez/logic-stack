import * as constants from './constants';

export const login = (user) => ({ type: constants.LOGIN, payload: user });
export const logout = () => ({ type: constants.LOGOUT });
export const setAuth = (auth) => ({ type: constants.SET_AUTH, payload: auth });