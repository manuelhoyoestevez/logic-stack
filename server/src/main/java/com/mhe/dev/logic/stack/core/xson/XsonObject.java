package com.mhe.dev.logic.stack.core.xson;

import com.mhe.dev.logic.stack.core.xson.exception.DuplicatedKeyException;

/**
 * XsonObject.
 */
public interface XsonObject extends XsonValue {
    void put(String key, XsonValue obj) throws DuplicatedKeyException;
}
