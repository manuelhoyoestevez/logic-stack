package com.mhe.dev.compiler.logic.core.logic.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * DecisionTree.
 */
public interface DecisionTree extends GraphVizNode
{
    DecisionTreeType getType();

    TruthTable getTruthTable();

    boolean isLeaf();

    boolean getLeafValue();

    DecisionTree getSubDecisionTree(boolean value);

    String getLiteral();

    Boolean getMode();

    String getExpression();

    DecisionTree setExpression(String expression);
}
