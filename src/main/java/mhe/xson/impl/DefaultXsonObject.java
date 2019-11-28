package mhe.xson.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mhe.compiler.mhe.UtilString;
import mhe.xson.XsonArray;
import mhe.xson.XsonObject;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
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
    public void clear() {
        this.keys.clear();
        this.values.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.values.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.values.containsValue(value);
    }

    @Override
    public Set<Entry<String, XsonValue>> entrySet() {
        return this.values.entrySet();
    }

    @Override
    public XsonValue get(Object key) {
        return this.values.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return this.values.keySet();
    }

    @Override
    public XsonValue put(String key, XsonValue value) {
        this.keys.add(key);
        return this.values.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends XsonValue> m) {
        this.keys.addAll(m.keySet());
        this.values.putAll(m);
    }

    @Override
    public XsonValue remove(Object key) {
        this.keys.remove(key);
        return this.values.remove(key);
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public Collection<XsonValue> values() {
        return this.values.values();
    }

    @Override
    public String getString(String key, String def) throws WrongXsonTypeException {
        return this.get(key).toString(def);
    }

    @Override
    public Double getDouble(String key, Double def) throws WrongXsonTypeException {
        return this.get(key).toDouble(def);
    }

    @Override
    public Integer getInteger(String key, Integer def) throws WrongXsonTypeException {
        return this.get(key).toInteger(def);
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) throws WrongXsonTypeException {
        return this.get(key).toBoolean(def);
    }

    @Override
    public XsonObject getXsonObject(String key, XsonObject def) throws WrongXsonTypeException {
        XsonValue xsonObject = this.get(key);

        if(xsonObject == null) {
            return def;
        }

        if(!xsonObject.isXsonObject()) {
            throw new WrongXsonTypeException(xsonObject.getType());
        }

        return (XsonObject) xsonObject;
    }

    @Override
    public XsonArray getXsonArray(String key, XsonArray def) throws WrongXsonTypeException {
        XsonValue xsonArray = this.get(key);

        if(xsonArray == null) {
            return def;
        }

        if(!xsonArray.isXsonArray()) {
            throw new WrongXsonTypeException(xsonArray.getType());
        }

        return (XsonArray) xsonArray;
    }
    
    @Override
    public String toJsonString() {
      boolean f = true;
      String xsonObject = "{";
      try {
        for(String key : this.keys) {
          if(f) {
            f = false;
          }
          else {
            xsonObject += ',';
          }
          
          xsonObject += UtilString.escapeString(key) + ":" + this.values.get(key).toJsonString();
        }
      }
      catch(Exception ex) {}

      return xsonObject + "}";
    }
}
