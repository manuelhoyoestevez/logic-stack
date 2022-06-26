package com.mhe.dev.logic.stack.core.logic.exception;

/**
 * InvalidExpressionTreeOperatorException.
 */
public class InvalidExpressionTreeOperatorException extends LogicException
{
    private static final long serialVersionUID = -9062050965889754570L;

    public InvalidExpressionTreeOperatorException()
    {
        super("Missing logic operator");
    }

    public InvalidExpressionTreeOperatorException(String operator)
    {
        super("Invalid logic operator: " + operator);
    }
}
