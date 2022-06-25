package com.mhe.dev.logic.stack.core.logic.exception;

/**
 * InvalidDecisionTreeParameterException.
 */
public class InvalidDecisionTreeParameterException extends LogicException {
    private static final long serialVersionUID = 969012670202746265L;

    public InvalidDecisionTreeParameterException(String parameter, Object value) {
        super("Invalid decision tree parameter '" + parameter + "': '" + value + "'");
    }
}
