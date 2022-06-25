package com.mhe.dev.logic.stack.core.model;

public enum LogicExpressionType
{
    AND("and"),
    OR("or"),
    NOT("not"),
    LITERAL("literal");

    private final String operator;

    LogicExpressionType(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }

    public static LogicExpressionType fromOperator(String operator)
    {
        if (operator == null)
        {
            return null;
        }

        for (LogicExpressionType b : LogicExpressionType.values()) {
            if (b.operator.equals(operator)) {
                return b;
            }
        }

        throw new IllegalArgumentException("Unexpected value '" + operator + "'");
    }
}
