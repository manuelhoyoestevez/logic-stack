package mhe;

import io.vertx.core.json.JsonObject;
import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.TooManyLiteralsException;

public interface LogicService {
    JsonObject fromExpressionTreeToTruthTable(JsonObject payload) throws InvalidTreeExpressionOperator, TooManyLiteralsException;

    JsonObject fromTruthTableToDecisionTree(JsonObject payload) throws InvalidTruthTableLiteralsException, InvalidTruthTableValuesException;

    JsonObject fromDecisionTreeToExpressionTree(JsonObject payload);

    JsonObject fromDecisionTreeToTruthTable(JsonObject payload);

    JsonObject fromTruthTableToExpressionTree(JsonObject payload);
}
