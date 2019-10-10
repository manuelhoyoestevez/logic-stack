package mhe.compiler;

import io.vertx.core.json.JsonObject;

public interface CompilerInterface {
    JsonObject expressionToJson(String expression) throws Exception;
}
