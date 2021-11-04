package mhe.xson;

/**
 * XsonArray.
 */
public interface XsonArray extends XsonValue {
    int size();

    void add(XsonValue obj);
}
