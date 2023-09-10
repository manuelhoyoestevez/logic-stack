package com.mhe.dev.compiler.logic.core.logic;

import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import java.util.List;

/**
 * LogicConverter.
 */
public interface LogicConverter
{
    TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree);

    DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize);

    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree, boolean target, List<String> weights);
}
