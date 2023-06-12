import { LOGOUT } from '../auth/constants';
import * as constants from './constants';

const initialState = {
    toast: undefined
};

const toastReducer = function (state = initialState, action) {
    const { type, payload } = action;

    switch (type) {
        case LOGOUT: {
            return initialState;
        }
        case constants.SHOW_TOAST: {
            return {
                toast: payload
            };
        }
        case constants.SHOW_TOAST_PARAMS: {
            return {
                toast: payload
            };
        }
        case constants.HIDE_TOAST: {
            return initialState;
        }
        default:
            return state;
    }
}

export default toastReducer;