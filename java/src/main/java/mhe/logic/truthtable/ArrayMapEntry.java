package mhe.logic.truthtable;

import java.util.Map;

public class ArrayMapEntry implements Map.Entry<Integer, Boolean>, Comparable<ArrayMapEntry> {
    private Integer key;
    private Boolean value;

    public ArrayMapEntry(Integer key, Boolean value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return this.key;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public Boolean setValue(Boolean value) {
        this.value = value;
        return true;
    }

    @Override
    public int compareTo(ArrayMapEntry arrayMapEntry) {
        return this.getKey() - arrayMapEntry.getKey();
    }
}
