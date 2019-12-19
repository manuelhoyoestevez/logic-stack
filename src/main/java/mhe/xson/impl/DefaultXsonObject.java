package mhe.xson.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhe.compiler.mhe.UtilString;
import mhe.xson.XsonArray;
import mhe.xson.XsonObject;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.exception.DuplicatedKeyException;
import mhe.xson.exception.WrongXsonTypeException;

public class DefaultXsonObject extends DefaultXsonValue implements XsonObject {
    private List<String> keys;
    private Map<String, XsonValue> values;

    public DefaultXsonObject() {
        super(null, XsonValueType.OBJECT);
        this.keys = new ArrayList<String>();
        this.values = new HashMap<String, XsonValue>();
    }

    @Override
    public void put(String key, XsonValue obj) throws DuplicatedKeyException {
        XsonValue aux = this.values.get(key);
        if (aux != null) {
            throw new DuplicatedKeyException(key, aux, obj);
        }
        this.keys.add(key);
        this.values.put(key, obj);
    }

    @Override
    public String getString(String key, String def) throws WrongXsonTypeException {
        return this.values.get(key).toString(def);
    }

    @Override
    public Double getDouble(String key, Double def) throws WrongXsonTypeException {
        return this.values.get(key).toDouble(def);
    }

    @Override
    public Integer getInteger(String key, Integer def) throws WrongXsonTypeException {
        return this.values.get(key).toInteger(def);
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) throws WrongXsonTypeException {
        return this.values.get(key).toBoolean(def);
    }

    @Override
    public XsonObject getXsonObject(String key, XsonObject def) throws WrongXsonTypeException {
        XsonValue xsonObject = this.values.get(key);

        if (xsonObject == null) {
            return def;
        }

        if (!xsonObject.isXsonObject()) {
            throw new WrongXsonTypeException(xsonObject.getType());
        }

        return (XsonObject) xsonObject;
    }

    @Override
    public XsonArray getXsonArray(String key, XsonArray def) throws WrongXsonTypeException {
        XsonValue xsonArray = this.values.get(key);

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
        boolean f = true;
        String xsonObject = "{";
        try {
            for (String key : this.keys) {
                if (f) {
                    f = false;
                } else {
                    xsonObject += ',';
                }

                xsonObject += UtilString.escapeString(key) + ":" + this.values.get(key).toJsonString();
            }
        } catch (Exception ex) {
        }

        return xsonObject + "}";
    }
}
