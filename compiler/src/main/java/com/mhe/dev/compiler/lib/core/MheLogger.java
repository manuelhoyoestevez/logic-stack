package com.mhe.dev.compiler.lib.core;

/**
 * MheLogger.
 */
public interface MheLogger
{
    void stream(int col, int row, String message);

    void lexer(int col, int row, String message);

    void parser(int col, int row, String message);

    void semantic(int col, int row, String message);

    void warn(int col, int row, String message);

    void error(int col, int row, String message);
}
