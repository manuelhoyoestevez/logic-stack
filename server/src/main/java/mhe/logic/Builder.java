package mhe.logic;

import mhe.logic.exception.InvalidDecisionTreeParameterException;
import mhe.logic.exception.InvalidExpressionTreeOperatorException;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;

/**
 * Builder.
 */
public interface Builder {
    TruthTable fromJsonToTruthTable(String json) throws JsonParseException, InvalidTruthTableLiteralsException,
            InvalidTruthTableValuesException;

    DecisionTree fromJsonToDecisionTree(String json) throws JsonParseException, InvalidDecisionTreeParameterException;

    ExpressionTree fromJsonToExpressionTree(String json) throws JsonParseException,
            InvalidExpressionTreeOperatorException;

    TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree) throws TooManyLiteralsException;

    DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize);

    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree);
}
