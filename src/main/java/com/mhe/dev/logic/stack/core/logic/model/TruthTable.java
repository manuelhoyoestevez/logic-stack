package com.mhe.dev.logic.stack.core.logic.model;

import java.util.List;
import java.util.Map;

/**
 * TruthTable.
 */
public interface TruthTable
{
    List<String> getLiterals();

    List<Boolean> getValues();

    String getValuesAsString();

    String getMinLiteral();

    String getMaxLiteral();

    double getEntropy();

    double getAverage();

    boolean isLeaf();

    boolean getLeafValue();

    TruthTable reduceBy(String literal, boolean value);

    int getSize();

    Map<Boolean, Integer> getDistribution();

    Map<String, LiteralDistribution> getLiteralPartition();

    default int getMaxSize()
    {
        return 1 << getLiterals().size();
    }
}
