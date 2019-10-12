package mhe.logic;

import org.json.simple.JSONObject;

import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.TooManyLiteralsException;

public interface Builder {
    TruthTable     fromJsonToTruthTable(JSONObject json) throws InvalidTruthTableLiteralsException, InvalidTruthTableValuesException;
    DecisionTree   fromJsonToDecisionTree(JSONObject json);
    ExpressionTree fromJsonToExpressionTree(JSONObject json) throws InvalidTreeExpressionOperator;

    TruthTable     fromExpressionTreeToTruthTable(ExpressionTree expressionTree) throws TooManyLiteralsException;
    DecisionTree   fromTruthTableToDecisionTree(TruthTable expressionTree);
    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree expressionTree);

    DecisionTree   fromTruthTableToExpressionTree(TruthTable expressionTree);
}
