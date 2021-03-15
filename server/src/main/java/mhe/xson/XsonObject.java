package mhe.xson;

import mhe.xson.exception.DuplicatedKeyException;
import mhe.xson.exception.WrongXsonTypeException;

public interface XsonObject extends XsonValue {
    void put(String key, XsonValue obj) throws DuplicatedKeyException;

    String getString(String key, String def) throws WrongXsonTypeException;

    Double getDouble(String key, Double def) throws WrongXsonTypeException;

    Integer getInteger(String key, Integer def) throws WrongXsonTypeException;

    Boolean getBoolean(String key, Boolean def) throws WrongXsonTypeException;

    XsonObject getXsonObject(String key, XsonObject def) throws WrongXsonTypeException;

    XsonArray getXsonArray(String key, XsonArray def) throws WrongXsonTypeException;
}
