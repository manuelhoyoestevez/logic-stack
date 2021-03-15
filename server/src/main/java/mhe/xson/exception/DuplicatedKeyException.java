package mhe.xson.exception;

import mhe.xson.XsonValue;

public class DuplicatedKeyException extends Exception {
    private static final long serialVersionUID = 5720709647455162805L;
    private String key;
    private XsonValue previous;
    private XsonValue current;

    public DuplicatedKeyException(String key, XsonValue previous, XsonValue current) {
        this.key = key;
        this.setPrevious(previous);
        this.setCurrent(current);
    }

    public String getKey() {
        return key;
    }

    public XsonValue getCurrent() {
        return current;
    }

    public void setCurrent(XsonValue current) {
        this.current = current;
    }

    public XsonValue getPrevious() {
        return previous;
    }

    public void setPrevious(XsonValue previous) {
        this.previous = previous;
    }

}
