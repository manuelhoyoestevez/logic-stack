import get from 'lodash.get';

export const selectToast = (state) => get(state, 'toastReducer.toast');