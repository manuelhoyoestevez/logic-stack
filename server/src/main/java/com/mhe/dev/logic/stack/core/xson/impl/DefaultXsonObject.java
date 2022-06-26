package com.mhe.dev.logic.stack.core.xson.impl;

import com.mhe.dev.logic.stack.core.compiler.mhe.UtilString;
import com.mhe.dev.logic.stack.core.xson.XsonObject;
import com.mhe.dev.logic.stack.core.xson.XsonValue;
import com.mhe.dev.logic.stack.core.xson.XsonValueType;
import com.mhe.dev.logic.stack.core.xson.exception.DuplicatedKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DefaultXsonObject.
 */
public class DefaultXsonObject extends DefaultXsonValue implements XsonObject
{
    private final List<String> keys;
    private final Map<String, XsonValue> values;

    /**
     * Constructor.
     */
    public DefaultXsonObject()
    {
        super(null, XsonValueType.OBJECT);
        keys = new ArrayList<>();
        values = new HashMap<>();
    }

    @Override
    public void put(String key, XsonValue obj) throws DuplicatedKeyException
    {
        XsonValue aux = values.get(key);
        if (aux != null)
        {
            throw new DuplicatedKeyException(key, aux, obj);
        }
        keys.add(key);
        values.put(key, obj);
    }

    @Override
    public String toJsonString()
    {
        boolean f = true;
        StringBuilder xsonObject = new StringBuilder("{");
        for (String key : keys)
        {
            if (f)
            {
                f = false;
            } else
            {
                xsonObject.append(',');
            }

            xsonObject.append(UtilString.escapeString(key)).append(":").append(values.get(key).toJsonString());
        }

        return xsonObject + "}";
    }
}
