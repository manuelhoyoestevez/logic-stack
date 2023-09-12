package com.mhe.dev.compiler.logic.core.xson.impl;

import com.mhe.dev.compiler.logic.core.xson.UtilString;
import com.mhe.dev.compiler.logic.core.xson.XsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DefaultXsonValue.
 */
public class DefaultXsonValue implements XsonValue
{
    protected final Object value;
    private final List<XsonValue> list;
    private final Map<String, XsonValue> map;

    public static DefaultXsonValue createXsonValue(Object value)
    {
        return new DefaultXsonValue(value, null, null);
    }

    public static DefaultXsonValue createXsonArray(List<XsonValue> list)
    {
        return new DefaultXsonValue(list, list, null);
    }

    public static DefaultXsonValue createXsonArray()
    {
        return createXsonArray(new ArrayList<>());
    }

    public static DefaultXsonValue createXsonObject(Map<String, XsonValue> map)
    {
        return new DefaultXsonValue(map, null, map);
    }

    public static DefaultXsonValue createXsonObject()
    {
        return createXsonObject(new HashMap<>());
    }

    private DefaultXsonValue(Object value, List<XsonValue> list, Map<String, XsonValue> map)
    {
        this.value = value;
        this.list = list;
        this.map = map;
    }
    
    public Class<?> getValueClass()
    {
        return value == null ? null: value.getClass();
    }

    @Override
    public int getSize()
    {
        return list != null ? list.size() : map != null ? map.size() : 0;
    }

    @Override
    public Double toDouble()
    {
        return toDouble(null);
    }

    @Override
    public Double toDouble(Double def)
    {
        return value == null ? def : Double.parseDouble(value.toString());
    }

    @Override
    public Long toLong()
    {
        return toLong(null);
    }

    @Override
    public Long toLong(Long def)
    {
        return value == null ? def : Long.parseLong(value.toString());
    }

    @Override
    public Integer toInteger()
    {
        return toInteger(null);
    }

    @Override
    public Integer toInteger(Integer def)
    {
        return value == null ? def : Integer.parseInt(value.toString());
    }

    @Override
    public Boolean toBoolean()
    {
        return toBoolean(null);
    }

    @Override
    public Boolean toBoolean(Boolean def)
    {
        return value == null ? def : Boolean.parseBoolean(value.toString());
    }

    @Override
    public String toString(String def)
    {
        return value == null ? def : value.toString();
    }

    @Override
    public List<XsonValue> toList(List<XsonValue> def)
    {
        return list == null ? def : list;
    }

    @Override
    public Map<String, XsonValue> toMap(Map<String, XsonValue> def)
    {
        return map == null ? def : map;
    }

    @Override
    public XsonValue get(int position, XsonValue def)
    {
        return list == null || position >= list.size() ? def : list.get(position);
    }

    @Override
    public XsonValue get(String key, XsonValue def)
    {
        return map == null || !map.containsKey(key) ? def : map.get(key);
    }

    @Override
    public String getString(String key, String def)
    {
        return map == null || !map.containsKey(key) ? def : map.get(key).toString(def);
    }

    @Override
    public String toJsonString()
    {
        if (value == null)
        {
            return null;
        }

        if(value instanceof String)
        {
            return UtilString.escapeString(value.toString());
        }

        if (list != null)
        {
            return "[" + list.stream().map(XsonValue::toJsonString).collect(Collectors.joining(",")) + "]";
        }

        if (map != null)
        {
            return "{" + map.entrySet().stream().map(this::mapEntry).collect(Collectors.joining(",")) + "}";
        }

        return value.toString();
    }

    private String mapEntry(Map.Entry<String, XsonValue> e)
    {
        return mapEntry(e.getKey(), e.getValue());
    }

    private String mapEntry(String key, XsonValue val)
    {
        return UtilString.escapeString(key) + ":" + val.toString();
    }
}
