package com.mhe.dev.logic.stack.core.logic;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * DecisionTree.
 */
public interface DecisionTree extends GraphVizNode {
    String toJsonString();

    DecisionTreeType getType();

    boolean isLeaf();

    boolean getLeafValue();

    String getLiteral();

    boolean getMode();

    double getEntropy();

    double getAverage();
}
