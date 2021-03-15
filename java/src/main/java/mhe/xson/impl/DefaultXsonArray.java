package mhe.xson.impl;

import java.util.ArrayList;
import java.util.List;

import mhe.xson.XsonArray;
import mhe.xson.XsonObject;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.exception.WrongXsonTypeException;

public class DefaultXsonArray extends DefaultXsonValue implements XsonArray {
    private List<XsonValue> values;

    public DefaultXsonArray() {
        super(null, XsonValueType.ARRAY);
        this.values = new ArrayList<XsonValue>();
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public void add(XsonValue xsonValue) {
        this.values.add(xsonValue);
    }

    @Override
    public String getString(Integer index, String def) throws WrongXsonTypeException {
        return this.values.get(index).toString(def);
    }

    @Override
    public Double getDouble(Integer index, Double def) throws WrongXsonTypeException {
        return this.values.get(index).toDouble(def);
    }

    @Override
    public Integer getInteger(Integer index, Integer def) throws WrongXsonTypeException {
        return this.values.get(index).toInteger(def);
    }

    @Override
    public Boolean getBoolean(Integer index, Boolean def) throws WrongXsonTypeException {
        return this.values.get(index).toBoolean(def);
    }

    @Override
    public XsonObject getXsonObject(Integer index, XsonObject def) throws WrongXsonTypeException {
        XsonValue xsonObject = this.values.get(index);

        if (xsonObject == null) {
            return def;
        }

        if (!xsonObject.isXsonObject()) {
            throw new WrongXsonTypeException(xsonObject.getType());
        }

        return (XsonObject) xsonObject;
    }

    @Override
    public XsonArray getXsonArray(Integer index, XsonArray def) throws WrongXsonTypeException {
        XsonValue xsonArray = this.values.get(index);

        if (xsonArray == null) {
            return def;
        }

        if (!xsonArray.isXsonArray()) {
            throw new WrongXsonTypeException(xsonArray.getType());
        }

        return (XsonArray) xsonArray;
    }

    @Override
    public String toJsonString() {
        String xsonArray = "[";

        boolean f = true;

        for (XsonValue value : this.values) {
            if (f) {
                f = false;
            } else {
                xsonArray += ',';
            }
            xsonArray += value.toJsonString();
        }

        return xsonArray + "]";
    }
}
