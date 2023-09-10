package com.mhe.dev.compiler.logic.core.json;

import java.util.List;

/**
 * JsonNode.
 */
public class JsonNode
{
    private final String operator;

    private final String literal;

    private final List<String> order;

    private final List<JsonNode> children;

    /**
     * Constructor.
     *
     * @param operator operator
     * @param literal  literal
     * @param order    order
     * @param children children
     */
    public JsonNode(String operator, String literal, List<String> order, List<JsonNode> children)
    {
        this.operator = operator;
        this.literal = literal;
        this.order = order;
        this.children = children;
    }

    public String getOperator()
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

    public List<JsonNode> getChildren()
    {
        return children;
    }
}
