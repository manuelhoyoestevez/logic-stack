package com.mhe.dev.logic.stack.core.logic;

import java.util.List;

/**
 * TruthTable.
 */
public interface TruthTable {
    List<String> getLiterals();

    String getMinLiteral();

    String getMaxLiteral();

    double getEntropy();

    double getAverage();

    boolean isLeaf();

    boolean getLeafValue();

    TruthTable reduceBy(String literal, boolean value);

    String toJsonString();

    int getSize();

    default int getMaxSize() {
        return 1 << this.getLiterals().size();
    }
}
