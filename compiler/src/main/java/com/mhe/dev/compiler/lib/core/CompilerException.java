package com.mhe.dev.compiler.lib.core;

/**
 * Compiler Exception.
 */
public class CompilerException extends Exception
{
    private final int row;
    private final int col;

    /**
     * Constructor.
     *
     * @param row     Row
     * @param col     Column
     * @param message Message
     * @param cause   Cause
     */
    public CompilerException(int row, int col, String message, Throwable cause)
    {
        super(message, cause);
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }
}
