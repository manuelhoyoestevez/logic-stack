package com.mhe.dev.logic.stack.core.xson.impl;

import com.mhe.dev.logic.stack.core.compiler.mhe.UtilString;
import com.mhe.dev.logic.stack.core.xson.XsonValue;
import com.mhe.dev.logic.stack.core.xson.XsonValueType;
import com.mhe.dev.logic.stack.core.xson.exception.WrongXsonTypeException;

/**
 * DefaultXsonValue.
 */
public class DefaultXsonValue implements XsonValue {
    Object value;
    XsonValueType type;

    public DefaultXsonValue(Object value, XsonValueType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public XsonValueType getType() {
        return type;
    }

    @Override
    public Double toDouble() throws WrongXsonTypeException {
        return toDouble(null);
    }

    @Override
    public Double toDouble(Double def) throws WrongXsonTypeException {
        if (getType() != XsonValueType.DECIMAL) {
            throw new WrongXsonTypeException(getType());
        }

        return (Double) getValue();
    }

    @Override
    public Integer toInteger() throws WrongXsonTypeException {
        return toInteger(null);
    }

    @Override
    public Integer toInteger(Integer def) throws WrongXsonTypeException {
        if (getType() != XsonValueType.INTEGER) {
            throw new WrongXsonTypeException(getType());
        }

        return (Integer) getValue();
    }

    @Override
    public Boolean toBoolean() throws WrongXsonTypeException {
        return toBoolean(null);
    }

    @Override
    public Boolean toBoolean(Boolean def) throws WrongXsonTypeException {
        if (getType() != XsonValueType.BOOLEAN) {
            throw new WrongXsonTypeException(getType());
        }

        return (Boolean) getValue();
    }

    @Override
    public String toString(String def) throws WrongXsonTypeException {
        if (getType() != XsonValueType.STRING) {
            throw new WrongXsonTypeException(getType());
        }

        return (String) getValue();
    }

    @Override
    public String toJsonString() {
        if (getType() == XsonValueType.STRING) {
            try {
                return UtilString.escapeString(toString(""));
            } catch (WrongXsonTypeException e) {
                return getValue().toString();
            }
        }
        return getValue().toString();
    }
}
