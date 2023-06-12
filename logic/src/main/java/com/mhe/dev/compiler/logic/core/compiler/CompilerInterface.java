package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;

/**
 * CompilerInterface.
 */
public interface CompilerInterface
{
    String expressionToJson(String expression) throws CompilerException;
}