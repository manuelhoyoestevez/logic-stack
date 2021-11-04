package mhe.compiler;

import mhe.compiler.exception.CompilerException;

/**
 * CompilerInterface.
 */
public interface CompilerInterface {
    String expressionToJson(String expression) throws CompilerException;
}
