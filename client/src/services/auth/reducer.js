import * as constants from './constants';

const initialState = {
    token: undefined
};

const authReducer = function (state = initialState, action) {
    const { type, payload } = action;

    switch (type) {
        case constants.LOGOUT:
            return initialState;
        case constants.SET_AUTH:

            return {
                ...state,
                token: payload.accessToken
            }
        default:
            return state;
    }
}

export default authReducer;