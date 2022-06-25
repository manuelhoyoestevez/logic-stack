package com.mhe.dev.logic.stack.core.model;

import java.util.ArrayList;
import java.util.List;

public class LogicExpressionTree
{
    private final LogicExpressionType operator;

    private final String literal;

    private final List<String> order;

    private final List<LogicExpressionTree> children;

    public LogicExpressionTree(
        LogicExpressionType operator,
        String literal,
        List<String> order,
        List<LogicExpressionTree> children
    )
    {
        this.operator = operator;
        this.literal = literal;
        this.order = order == null ? new ArrayList<>() : order;
        this.children = children == null ? new ArrayList<>(): children;
    }

    public LogicExpressionType getOperator()
    {
        return operator;
    }

    public String getLiteral()
    {
        return literal;
    }

    public List<String> getOrder()
    {
        return order;
    }

    public List<LogicExpressionTree> getChildren()
    {
        return children;
    }
}
