package mhe.logic;

import java.util.Map;

import org.json.simple.JSONObject;

public interface TruthTable extends LogicFunction {
    String  getBranchLiteral();
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
    JSONObject toJson();
}
