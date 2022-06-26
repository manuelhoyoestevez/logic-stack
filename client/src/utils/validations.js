export const isNullOrEmpty = function (value) {
    return value === undefined || value === null;
};

export const isNullOrEmptyString = function (value) {
    return Number(value) === 0 || value === undefined || value === null || value === '';
};

export const isNullOrEmptyNumber = function (value) {
    return value === undefined || value === null || value === '' || Number(value) < 0;
};

export const isZero = function (value) {
    return Number(value) === 0;
};

export const isNotChecked = function (value) {
    return !value;
};

export const isNotStartDate = function (value, field) {
    const reference = document.getElementById(field.reference);
    const start = new Date(value);
    const end = new Date(reference.value);

    return !(start < end);
};

export const isNotEndDate = function (value, field) {
    const reference = document.getElementById(field.reference);
    const start = new Date(reference.value);
    const end = new Date(value);

    return !(end > start);
};