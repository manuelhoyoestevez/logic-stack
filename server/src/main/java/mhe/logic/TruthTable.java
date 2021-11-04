package mhe.logic;

import java.util.Map;

/**
 * TruthTable.
 */
public interface TruthTable extends LogicFunction {
    String getLiteral();

    String getMaxLiteral();

    Integer getRowsCount();

    Double getEntropy();

    Double getAverage();

    Boolean isLeaf();

    Boolean getLeafValue();

    Map<Integer, Boolean> getValues();

    Map<Boolean, Integer> getDistribution();

    TruthTable reduceBy(String literal, Boolean value);

    TruthTable reduceBy(Map<String, Boolean> values);
}
