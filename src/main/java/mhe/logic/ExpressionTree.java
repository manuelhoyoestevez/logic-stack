package mhe.logic;

import java.util.Map;
import java.util.Set;

import mhe.graphviz.GraphVizNode;

public interface ExpressionTree extends LogicFunction, GraphVizNode {
    String getLiteral();
    String getExpression();
    ExpressionTreeType getType();
    Boolean getMode();
    Boolean isFinal();
    Boolean equivalent(ExpressionTree expressionTree);
    Boolean complementary(ExpressionTree expressionTree);
    Set<ExpressionTree> getChildren();
    ExpressionTree generateNot();
    ExpressionTree reduce();
    ExpressionTree reduceBy(String literal, Boolean value);
    ExpressionTree reduceBy(Map<String, Boolean> values);
}
