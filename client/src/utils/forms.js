import { I18n } from 'react-redux-i18n';
import * as validations from './validations';

export const isValid = (fields, data) => {
    const fieldsKeys = fields.map(f => f.type !== 'title' && f.type !== 'offset' ? f.id : '').filter(f => f !== '');
    const dataKeys = Object.keys(data);
    const checksKeys = fieldsKeys.map(fk => dataKeys.indexOf(fk) > -1);

    return checksKeys.every(ck => ck);
};

export const setDefaultErrors = (currentErrors, fields) => {
    const newErrors = fields.reduce((o, f) => {
        o[f.id] = false;
        return o;
    }, {});

    return { ...currentErrors, ...newErrors };
};

export const setDefaultErrorMessages = (currentErrorMessages, fields) => {
    const newErrorMessages = fields.reduce((o, f) => {
        o[f.id] = '';
        return o;
    }, {});

    return { ...currentErrorMessages, ...newErrorMessages };
};

export const setError = (id, errors, field, value) => {
    errors[id] = field.validation
        ? field.validation.validators.map(v => validations[v](value, field)).some(v => v)
        : false;
    return errors;
};

export const setErrorMessage = (id, errors, errorMessages, field) => {
    errorMessages[id] = errors[id]
        ? I18n.t(field.validation.message)
        : '';
    return errorMessages;
};