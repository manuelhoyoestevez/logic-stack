package mhe.xson;

import java.util.List;

import mhe.xson.exception.WrongXsonTypeException;

public interface XsonArray extends XsonValue, List<XsonValue> {
    String getString(Integer index, String def) throws WrongXsonTypeException;
    Double getDouble(Integer index, Double def) throws WrongXsonTypeException;
    Integer getInteger(Integer index, Integer def) throws WrongXsonTypeException;
    Boolean getBoolean(Integer index, Boolean def) throws WrongXsonTypeException;
    XsonObject getXsonObject(Integer index,XsonObject def) throws WrongXsonTypeException;
    XsonArray getXsonArray(Integer index, XsonArray def) throws WrongXsonTypeException;
}
