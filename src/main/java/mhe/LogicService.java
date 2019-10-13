package mhe;

import io.vertx.core.json.JsonObject;
import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;

public interface LogicService {
    JsonObject fromExpressionTreeToTruthTable(JsonObject payload) throws JsonParseException, InvalidTreeExpressionOperator, TooManyLiteralsException;

    JsonObject fromTruthTableToDecisionTree(JsonObject payload) throws JsonParseException, InvalidTruthTableLiteralsException, InvalidTruthTableValuesException;

    JsonObject fromDecisionTreeToExpressionTree(JsonObject payload) throws JsonParseException;

    JsonObject fromDecisionTreeToTruthTable(JsonObject payload) throws JsonParseException;

    JsonObject fromTruthTableToExpressionTree(JsonObject payload) throws JsonParseException;
}
