import get from 'lodash.get';

export const selectLoading = (state) => get(state, 'loadingReducer.loading');
export const selectLoadingOK = (state) => get(state, 'loadingReducer.loadingOK');
export const selectLoadingKO = (state) => get(state, 'loadingReducer.loadingKO');