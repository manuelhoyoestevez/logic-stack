import * as constants from './constants';
import { LOGOUT } from '../auth/constants';

const initialState = {
    loading: undefined,
    loadingOK: undefined,
    loadingKO: undefined
};

const loadingReducer = function (state = initialState, action) {
    const { type } = action;

    switch (type) {
        case LOGOUT: {
            return initialState;
        }
        case constants.SHOW_LOADING: {
            return {
                loading: true
            };
        }
        case constants.SHOW_LOADING_OK: {
            return {
                loading: true,
                loadingOK: true,
                loadingKO: false
            };
        }
        case constants.SHOW_LOADING_KO: {
            return {
                loading: true,
                loadingKO: true,
                loadingOK: false
            };
        }
        case constants.HIDE_LOADING: {
            return {
                loading: false,
                loadingOK: false,
                loadingKO: false
            };
        }
        default:
            return state;
    }
}

export default loadingReducer;