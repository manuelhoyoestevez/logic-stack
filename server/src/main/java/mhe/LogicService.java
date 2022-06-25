package mhe;

import io.vertx.core.json.JsonObject;
import com.mhe.dev.logic.stack.core.logic.exception.InvalidDecisionTreeParameterException;
import com.mhe.dev.logic.stack.core.logic.exception.InvalidExpressionTreeOperatorException;
import com.mhe.dev.logic.stack.core.logic.exception.InvalidTruthTableLiteralsException;
import com.mhe.dev.logic.stack.core.logic.exception.InvalidTruthTableValuesException;
import com.mhe.dev.logic.stack.core.logic.exception.JsonParseException;
import com.mhe.dev.logic.stack.core.logic.exception.TooManyLiteralsException;

/**
 * LogicService.
 */
public interface LogicService {
    JsonObject fromExpressionTreeToTruthTable(JsonObject payload) throws JsonParseException,
            InvalidExpressionTreeOperatorException, TooManyLiteralsException;

    JsonObject fromTruthTableToDecisionTree(JsonObject payload) throws JsonParseException,
            InvalidTruthTableLiteralsException, InvalidTruthTableValuesException;

    JsonObject fromDecisionTreeToExpressionTree(JsonObject payload) throws JsonParseException,
            InvalidDecisionTreeParameterException;
}
