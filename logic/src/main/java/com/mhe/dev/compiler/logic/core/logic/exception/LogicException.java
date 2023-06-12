package com.mhe.dev.compiler.logic.core.logic.exception;

/**
 * LogicException.
 */
public class LogicException extends Exception
{
    private static final long serialVersionUID = 6831925068612144652L;

    public LogicException(String message)
    {
        super(message);
    }

    public LogicException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
