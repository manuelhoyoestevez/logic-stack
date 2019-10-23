package mhe.logic;

import java.util.Map;

public interface TruthTable extends LogicFunction {
    String  getLiteral();
    String  getMaxLiteral();
    Integer getRowsCount();
    Double  getEntropy();
    Double  getAverage();
    Boolean isLeaf();
    Boolean getLeafValue();
    Boolean getResult(Integer position);
    Boolean getResult(Map<String, Boolean> values);
    Map<Integer, Boolean> getValues();
    Map<Boolean, Integer> getDistribution();
    TruthTable reduceBy(String literal, Boolean value);
    TruthTable reduceBy(Map<String, Boolean> values);
}
