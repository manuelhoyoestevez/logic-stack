package com.mhe.dev.logic.stack.core.xson;

/**
 * XsonArray.
 */
public interface XsonArray extends XsonValue
{
    int size();

    void add(XsonValue obj);
}
