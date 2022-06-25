package com.mhe.dev.logic.stack.core.logic;

import java.util.List;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * DecisionTree.
 */
public interface DecisionTree extends GraphVizNode {
    List<String> getLiterals();

    String toJsonString();

    DecisionTreeType getType();

    boolean isLeaf();

    boolean getLeafValue();

    String getLiteral();

    boolean getMode();

    double getEntropy();

    double getAverage();

    DecisionTree reduceBy(String literal, Boolean value);

    DecisionTree getSubDecisionTree(boolean value);
}
