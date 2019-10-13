package mhe.logic;

import mhe.logic.exception.InvalidTreeExpressionOperator;
import mhe.logic.exception.InvalidTruthTableLiteralsException;
import mhe.logic.exception.InvalidTruthTableValuesException;
import mhe.logic.exception.JsonParseException;
import mhe.logic.exception.TooManyLiteralsException;

public interface Builder {
    TruthTable     fromJsonToTruthTable(String json) throws JsonParseException, InvalidTruthTableLiteralsException, InvalidTruthTableValuesException;
    DecisionTree   fromJsonToDecisionTree(String json) throws JsonParseException;
    ExpressionTree fromJsonToExpressionTree(String json) throws JsonParseException, InvalidTreeExpressionOperator;
    TruthTable     fromExpressionTreeToTruthTable(ExpressionTree expressionTree) throws TooManyLiteralsException;
    DecisionTree   fromTruthTableToDecisionTree(TruthTable expressionTree);
    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree expressionTree);
    DecisionTree   fromTruthTableToExpressionTree(TruthTable expressionTree);
}
