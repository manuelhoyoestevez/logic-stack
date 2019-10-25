package mhe.xson.impl;

import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.exception.WrongXsonTypeException;

public class DefaultXsonValue implements XsonValue {
    Object value;
    XsonValueType type;

    public DefaultXsonValue(Object value, XsonValueType type){
        this.value = value;
        this.type = type;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public XsonValueType getType() {
        return this.type;
    }

    @Override
    public Boolean isXsonObject() {
        return this.getType() == XsonValueType.OBJECT;
    }

    @Override
    public Boolean isXsonArray() {
        return this.getType() == XsonValueType.ARRAY;
    }

    @Override
    public Double toDouble() throws WrongXsonTypeException {
        return this.toDouble(null);
    }

    @Override
    public Integer toInteger() throws WrongXsonTypeException {
        return this.toInteger(null);
    }

    @Override
    public Boolean toBoolean() throws WrongXsonTypeException {
        return this.toBoolean(null);
    }

    @Override
    public String toString(String def) throws WrongXsonTypeException {
        if(this.getType() != XsonValueType.STRING) {
            throw new WrongXsonTypeException(this.getType());
        }

        return (String) this.getValue();
    }

    @Override
    public Double toDouble(Double def) throws WrongXsonTypeException {
        if(this.getType() != XsonValueType.DECIMAL) {
            throw new WrongXsonTypeException(this.getType());
        }

        return (Double) this.getValue();
    }

    @Override
    public Integer toInteger(Integer def) throws WrongXsonTypeException {
        if(this.getType() != XsonValueType.INTEGER) {
            throw new WrongXsonTypeException(this.getType());
        }

        return (Integer) this.getValue();
    }

    @Override
    public Boolean toBoolean(Boolean def) throws WrongXsonTypeException {
        if(this.getType() != XsonValueType.BOOLEAN) {
            throw new WrongXsonTypeException(this.getType());
        }

        return (Boolean) this.getValue();
    }

    @Override
    public String toJsonString() {
        // TODO Auto-generated method stub
        return null;
    }
}
