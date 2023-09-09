package com.mhe.dev.compiler.logic.core.logic.model;

/**
 * DecisionTree.
 */
public interface DecisionTree
{
    DecisionTreeType getType();

    TruthTable getTruthTable();

    boolean isLeaf();

    boolean getLeafValue();

    DecisionTree getSubDecisionTree(boolean value);

    String getLiteral();

    Boolean getMode();
}
