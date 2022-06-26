package com.mhe.dev.logic.stack.core.logic.model;

import java.util.List;

/**
 * TruthTable.
 */
public interface TruthTable {
    List<String> getLiterals();

    List<Boolean> getValues();

    String getMinLiteral();

    String getMaxLiteral();

    double getEntropy();

    double getAverage();

    boolean isLeaf();

    boolean getLeafValue();

    TruthTable reduceBy(String literal, boolean value);

    int getSize();

    default int getMaxSize() {
        return 1 << getLiterals().size();
    }
}
