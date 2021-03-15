package mhe.xson;

import mhe.xson.exception.WrongXsonTypeException;

public interface XsonValue {
    Object getValue();
    XsonValueType getType();

    Double  toDouble()  throws WrongXsonTypeException;
    Integer toInteger() throws WrongXsonTypeException;
    Boolean toBoolean() throws WrongXsonTypeException;

    String  toString(String def)   throws WrongXsonTypeException;
    Double  toDouble(Double def)   throws WrongXsonTypeException;
    Integer toInteger(Integer def) throws WrongXsonTypeException;
    Boolean toBoolean(Boolean def) throws WrongXsonTypeException;

    Boolean isXsonObject();
    Boolean isXsonArray();

    String toJsonString();
}
