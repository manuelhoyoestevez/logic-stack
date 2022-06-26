package com.mhe.dev.logic.stack.core.xson;

import com.mhe.dev.logic.stack.core.xson.exception.WrongXsonTypeException;

/**
 * XsonValue.
 */
public interface XsonValue
{
    Object getValue();

    XsonValueType getType();

    Double toDouble() throws WrongXsonTypeException;

    Double toDouble(Double def) throws WrongXsonTypeException;

    Integer toInteger() throws WrongXsonTypeException;

    Integer toInteger(Integer def) throws WrongXsonTypeException;

    Boolean toBoolean() throws WrongXsonTypeException;

    Boolean toBoolean(Boolean def) throws WrongXsonTypeException;

    String toString(String def) throws WrongXsonTypeException;

    String toJsonString();
}
