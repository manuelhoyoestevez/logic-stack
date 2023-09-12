package com.mhe.dev.compiler.logic.core.xson;

import java.util.List;
import java.util.Map;

/**
 * XsonValue.
 */
public interface XsonValue
{
    int getSize();

    Class<?> getValueClass();

    Double toDouble();

    Double toDouble(Double def);

    Long toLong();

    Long toLong(Long def);

    Integer toInteger();

    Integer toInteger(Integer def);

    Boolean toBoolean();

    Boolean toBoolean(Boolean def);

    String toString(String def);

    default List<XsonValue> toList()
    {
        return toList(null);
    }

    List<XsonValue> toList(List<XsonValue> def);

    default Map<String, XsonValue> toMap()
    {
        return toMap(null);
    }

    Map<String, XsonValue> toMap(Map<String, XsonValue> def);

    XsonValue get(int position, XsonValue def);

    XsonValue get(String key, XsonValue def);

    String getString(String key, String def);

    String toJsonString();
}
