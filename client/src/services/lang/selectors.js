import get from 'lodash.get';
import Config from '../../config/config';

export const selectLanguage = (state) => get(state, 'langReducer.language') || Config.defaultLanguage;