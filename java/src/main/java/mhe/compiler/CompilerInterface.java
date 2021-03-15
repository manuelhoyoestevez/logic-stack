package mhe.compiler;

import mhe.compiler.exception.CompilerException;

public interface CompilerInterface {
    String expressionToJson(String expression) throws CompilerException;
}
