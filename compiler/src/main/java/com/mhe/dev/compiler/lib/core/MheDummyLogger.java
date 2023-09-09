package com.mhe.dev.compiler.lib.core;

/**
 * MheDummyLogger.
 */
public class MheDummyLogger implements MheLogger
{
    @Override
    public void stream(int col, int row, String message)
    {

    }

    @Override
    public void lexer(int col, int row, String message)
    {

    }

    @Override
    public void parser(int col, int row, String message)
    {

    }

    @Override
    public void semantic(int col, int row, String message)
    {

    }

    @Override
    public void warn(int col, int row, String message)
    {

    }

    @Override
    public void error(int col, int row, String message)
    {

    }
}
