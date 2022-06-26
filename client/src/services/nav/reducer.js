import { LOGOUT } from '../auth/constants';

const initialState = {
};

const navReducer = function (state = initialState, action) {
    const { type } = action;

    switch (type) {
        case LOGOUT: {
            return initialState;
        }
        default:
            return state;
    }
}

export default navReducer;