package mhe.xson.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    public boolean add(XsonValue e) {
        return this.values.add(e);
    }

    @Override
    public void add(int index, XsonValue element) {
        this.values.add(index, element);
    }

    @Override
    public boolean addAll(Collection<? extends XsonValue> c) {
        return this.values.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends XsonValue> c) {
        return this.values.addAll(c);
    }

    @Override
    public void clear() {
        this.values.clear();
    }

    @Override
    public boolean contains(Object o) {
        return this.values.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.values.containsAll(c);
    }

    @Override
    public XsonValue get(int index) {
        return this.values.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.values.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    @Override
    public Iterator<XsonValue> iterator() {
        return this.values.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.values.lastIndexOf(o);
    }

    @Override
    public ListIterator<XsonValue> listIterator() {
        return this.values.listIterator();
    }

    @Override
    public ListIterator<XsonValue> listIterator(int index) {
        return this.values.listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        return this.values.remove(o);
    }

    @Override
    public XsonValue remove(int index) {
        return this.values.remove(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.values.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.values.retainAll(c);
    }

    @Override
    public XsonValue set(int index, XsonValue element) {
        return this.values.set(index, element);
    }

    @Override
    public int size() {
        return this.values.size();
    }

    @Override
    public List<XsonValue> subList(int fromIndex, int toIndex) {
        return this.values.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return this.values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.toArray(a);
    }

    @Override
    public String getString(Integer index, String def) throws WrongXsonTypeException {
        return this.get(index).toString(def);
    }

    @Override
    public Double getDouble(Integer index, Double def) throws WrongXsonTypeException {
        return this.get(index).toDouble(def);
    }

    @Override
    public Integer getInteger(Integer index, Integer def) throws WrongXsonTypeException {
        return this.get(index).toInteger(def);
    }

    @Override
    public Boolean getBoolean(Integer index, Boolean def) throws WrongXsonTypeException {
        return this.get(index).toBoolean(def);
    }

    @Override
    public XsonObject getXsonObject(Integer index, XsonObject def) throws WrongXsonTypeException {
        XsonValue xsonObject = this.get(index);

        if(xsonObject == null) {
            return def;
        }

        if(!xsonObject.isXsonObject()) {
            throw new WrongXsonTypeException(xsonObject.getType());
        }

        return (XsonObject) xsonObject;
    }

    @Override
    public XsonArray getXsonArray(Integer index, XsonArray def) throws WrongXsonTypeException {
        XsonValue xsonArray = this.get(index);

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
        String xsonArray = "[";
        
        boolean f = true;
        
        for(XsonValue value : this.values ) {
          if(f) {
            f = false;
          }
          else {
            xsonArray += ',';
          }
          xsonArray += value.toJsonString();
        }

        return xsonArray + "]";
    }
}
