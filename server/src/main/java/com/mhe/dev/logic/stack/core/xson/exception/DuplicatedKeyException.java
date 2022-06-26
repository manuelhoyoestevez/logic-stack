package com.mhe.dev.logic.stack.core.xson.exception;

import com.mhe.dev.logic.stack.core.xson.XsonValue;

/**
 * DuplicatedKeyException.
 */
public class DuplicatedKeyException extends Exception
{
    private static final long serialVersionUID = 5720709647455162805L;
    private final String key;
    private final XsonValue previous;
    private final XsonValue current;

    /**
     * Constructor.
     *
     * @param key      Key
     * @param previous Previous.
     * @param current  Current.
     */
    public DuplicatedKeyException(String key, XsonValue previous, XsonValue current)
    {
        this.key = key;
        this.current = current;
        this.previous = previous;
    }

    public String getKey()
    {
        return key;
    }
}
