import * as constants from './constants';

const initialState = {
    language: undefined
};

const langReducer = function (state = initialState, action) {
    const { type, payload } = action;

    switch (type) {
        case constants.SET_LANGUAGE: {
            return {
                ...state,
                language: payload
            };
        }
        default:
            return state;
    }
}

export default langReducer;