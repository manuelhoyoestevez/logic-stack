package mhe.logic;

import java.util.Map;

import mhe.graphviz.GraphVizNode;

public interface DecisionTree extends LogicFunction, GraphVizNode {
    String  getLiteral();
    Boolean isLeaf();
    Boolean getLeafValue();
    Double  getEntropy();
    Double  getAverage();
    String  toJsonString();

    DecisionTree reduceBy(String literal, Boolean value);
    DecisionTree reduceBy(Map<String, Boolean> values);

    DecisionTree getSubDecisionTree(boolean value);
}
