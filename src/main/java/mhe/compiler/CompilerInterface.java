package mhe.compiler;

import org.json.simple.JSONObject;

import mhe.compiler.exception.CompilerException;

public interface CompilerInterface {
    JSONObject expressionToJson(String expression) throws CompilerException;
}
