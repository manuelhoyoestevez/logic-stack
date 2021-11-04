package mhe.xson;

import mhe.xson.exception.DuplicatedKeyException;

/**
 * XsonObject.
 */
public interface XsonObject extends XsonValue {
    void put(String key, XsonValue obj) throws DuplicatedKeyException;
}
