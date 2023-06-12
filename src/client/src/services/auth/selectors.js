import get from 'lodash.get';

export const selectToken = (state) => get(state, 'authReducer.token');