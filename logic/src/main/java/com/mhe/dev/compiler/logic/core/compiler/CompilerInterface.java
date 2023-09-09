package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.MheLogger;

/**
 * CompilerInterface.
 */
public interface CompilerInterface
{
    String expressionToJson(String expression, MheLogger logger) throws CompilerException;
}
