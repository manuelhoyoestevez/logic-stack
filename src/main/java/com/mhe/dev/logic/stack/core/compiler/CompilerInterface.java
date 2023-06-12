package com.mhe.dev.logic.stack.core.compiler;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;

/**
 * CompilerInterface.
 */
public interface CompilerInterface
{
    String expressionToJson(String expression) throws CompilerException;
}
