package com.mhe.dev.logic.stack.core.logic.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * Expression Tree.
 */
public interface ExpressionTree extends GraphVizNode {
    List<String> getLiterals();

    List<String> getWeights();

    String toJsonString();

    ExpressionTreeType getType();

    String getLiteral();

    String getExpression();

    boolean getMode();

    boolean isFinal();

    boolean equivalent(ExpressionTree expressionTree);

    boolean complementary(ExpressionTree expressionTree);

    Set<ExpressionTree> getChildren();

    ExpressionTree generateNot();

    ExpressionTree reduce();

    ExpressionTree reduceBy(Map<String, Boolean> values);
}
