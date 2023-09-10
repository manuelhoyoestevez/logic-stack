package com.mhe.dev.compiler.logic.core.logic.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Expression Tree.
 */
public interface ExpressionTree
{
    List<String> getLiterals();

    List<String> getWeights();

    String toJsonString();

    ExpressionTreeType getType();

    String getLiteral();

    boolean getMode();

    int getSize();

    Collection<ExpressionTree> getChildren();

    ExpressionTree replaceBy(Map<String, Boolean> values);

    ExpressionTree generateNot();

    ExpressionTree reduce();

    /**
     * Get first child.
     *
     * @return first child
     */
    default ExpressionTree getFirst()
    {
        return getChildren()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ExpressionTree has no children"));
    }

    default boolean equivalent(ExpressionTree expressionTree)
    {
        return getExpression().equals(expressionTree.getExpression());
    }

    default boolean complementary(ExpressionTree expressionTree)
    {
        return generateNot().equivalent(expressionTree) || expressionTree.generateNot().equivalent(this);
    }

    /**
     * Get expression.
     *
     * @return expression
     */
    default String getExpression()
    {
        StringBuilder ret = new StringBuilder();
        if (getType() == ExpressionTreeType.LITERAL)
        {
            ret.append(getMode() ? "" : "!").append(getLiteral());
        }
        if (getType() == ExpressionTreeType.NOT)
        {
            ret.append("!").append(getFirst().getExpression());
        }
        if (getType() == ExpressionTreeType.OPERATOR)
        {
            switch (getChildren().size())
            {
                case 0:
                    ret.append(getMode() ? "1" : "0");
                    break;
                case 1:
                    ret.append(getFirst().getExpression());
                    break;
                default:
                    boolean f = true;

                    ret.append(getMode() ? "[" : "{");

                    for (ExpressionTree child : getChildren())
                    {
                        if (f)
                        {
                            f = false;
                        } else
                        {
                            ret.append(",");
                        }
                        ret.append(child.getExpression());
                    }
                    ret.append(getMode() ? "]" : "}");
            }
        }
        return ret.toString();
    }

    /**
     * Calculate value from terms in values.
     *
     * @param values terms
     * @return values
     */
    default boolean calculate(HashMap<String, Boolean> values)
    {
        if (getType() == ExpressionTreeType.LITERAL)
        {
            if (!values.containsKey(getLiteral()))
            {
                throw new RuntimeException("values doesn't contain literal: " + getLiteral());
            }

            return values.get(getLiteral()) == getMode();
        }

        if (getType() == ExpressionTreeType.NOT)
        {
            return !getFirst().calculate(values);
        }

        for (ExpressionTree child : getChildren())
        {
            boolean val = child.calculate(values);

            if (val != getMode())
            {
                return val;
            }
        }

        return getMode();
    }
}
