package com.mhe.dev.compiler.logic.core.logic;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import java.util.List;

/**
 * LogicConverter.
 */
public interface LogicConverter
{
    ExpressionTree toExpressionTree(String expression) throws CompilerException;

    TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree);

    DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize);

    ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree, boolean maximize, List<String> weights);
}
