package com.mhe.dev.compiler.logic.core.logic;

import com.mhe.dev.logic.stack.core.logic.model.DecisionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTree;
import com.mhe.dev.logic.stack.core.logic.model.TruthTable;

/**
 * LogicConverter.
 */
public interface LogicConverter
{
    TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree);

    DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize);

    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree);
}
