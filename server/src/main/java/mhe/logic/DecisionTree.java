package mhe.logic;

import java.util.Map;
import mhe.graphviz.GraphVizNode;

/**
 * DecisionTree.
 */
public interface DecisionTree extends LogicFunction, GraphVizNode {
    DecisionTreeType getType();

    Boolean isLeaf();

    Boolean getLeafValue();

    String getLiteral();

    Boolean getMode();

    Double getEntropy();

    Double getAverage();

    DecisionTree reduceBy(String literal, Boolean value);

    DecisionTree reduceBy(Map<String, Boolean> values);

    DecisionTree getSubDecisionTree(boolean value);
}
