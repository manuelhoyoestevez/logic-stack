package com.mhe.dev.logic.stack.core.logic.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * DecisionTree.
 */
public interface DecisionTree extends GraphVizNode {
    DecisionTreeType getType();

    boolean isLeaf();

    boolean getLeafValue();

    DecisionTree getSubDecisionTree(boolean value);

    String getLiteral();

    boolean getMode();

    double getEntropy();

    double getAverage();
}
