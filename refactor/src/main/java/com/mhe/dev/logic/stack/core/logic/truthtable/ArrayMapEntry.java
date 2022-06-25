package com.mhe.dev.logic.stack.core.logic.truthtable;

import java.util.Map;

/**
 * ArrayMapEntry.
 */
public class ArrayMapEntry implements Map.Entry<Integer, Boolean>, Comparable<ArrayMapEntry> {
    private final Integer key;
    private Boolean value;

    public ArrayMapEntry(Integer key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public Boolean setValue(Boolean value) {
        this.value = value;
        return true;
    }

    @Override
    public int compareTo(ArrayMapEntry arrayMapEntry) {
        return getKey() - arrayMapEntry.getKey();
    }
}
