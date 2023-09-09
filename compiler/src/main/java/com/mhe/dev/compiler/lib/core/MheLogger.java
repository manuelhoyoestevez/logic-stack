package com.mhe.dev.compiler.lib.core;

/**
 * MheLogger.
 */
public interface MheLogger
{
    void stream(int row, int col, String message);

    void lexer(int row, int col, String message);

    void parser(int row, int col, String message);

    void semantic(int row, int col, String message);

    void warn(int row, int col, String message);

    void error(int row, int col, String message);
}
