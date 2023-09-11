package com.mhe.dev.logic.stack.core.xson.impl;

import com.mhe.dev.logic.stack.core.xson.XsonArray;
import com.mhe.dev.logic.stack.core.xson.XsonValue;
import com.mhe.dev.logic.stack.core.xson.XsonValueType;
import java.util.ArrayList;
import java.util.List;

/**
 * DefaultXsonArray.
 */
public class DefaultXsonArray extends DefaultXsonValue implements XsonArray
{
    private final List<XsonValue> values;

    public DefaultXsonArray()
    {
        super(null, XsonValueType.ARRAY);
        values = new ArrayList<>();
    }

    @Override
    public int size()
    {
        return values.size();
    }

    @Override
    public void add(XsonValue xsonValue)
    {
        values.add(xsonValue);
    }

    @Override
    public String toJsonString()
    {
        StringBuilder xsonArray = new StringBuilder("[");

        boolean f = true;

        for (XsonValue value : values)
        {
            if (f)
            {
                f = false;
            } else
            {
                xsonArray.append(',');
            }
            xsonArray.append(value.toJsonString());
        }

        return xsonArray + "]";
    }
}
