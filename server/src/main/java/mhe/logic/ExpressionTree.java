package mhe.logic;

import java.util.Map;
import java.util.Set;
import mhe.graphviz.GraphVizNode;

/**
 * Expression Tree.
 */
public interface ExpressionTree extends LogicFunction, GraphVizNode {
    ExpressionTreeType getType();

    String getLiteral();

    String getExpression();

    Boolean getMode();

    Boolean isFinal();

    Boolean equivalent(ExpressionTree expressionTree);

    Boolean complementary(ExpressionTree expressionTree);

    Set<ExpressionTree> getChildren();

    ExpressionTree generateNot();

    ExpressionTree reduce();

    ExpressionTree reduceBy(Map<String, Boolean> values);
}
