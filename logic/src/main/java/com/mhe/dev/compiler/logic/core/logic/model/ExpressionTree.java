package com.mhe.dev.compiler.logic.core.logic.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Expression Tree.
 */
public interface ExpressionTree extends GraphVizNode
{
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
