package com.mhe.dev.logic.stack.core.logic;

import com.mhe.dev.logic.stack.core.logic.model.DecisionTree;
import com.mhe.dev.logic.stack.core.logic.model.DecisionTreeImpl;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTree;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeImpl;
import com.mhe.dev.logic.stack.core.logic.model.ExpressionTreeType;
import com.mhe.dev.logic.stack.core.logic.model.TruthTable;
import com.mhe.dev.logic.stack.core.logic.model.TruthTableImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * LogicConverterImpl.
 */
public class LogicConverterImpl implements LogicConverter
{
    @Override
    public TruthTable fromExpressionTreeToTruthTable(ExpressionTree expressionTree)
    {
        Map<Integer, Boolean> values = new HashMap<>();
        List<String> literals = expressionTree.getLiterals();

        int r = 1 << literals.size();

        for (int i = 0; i < r; i++)
        {
            int check = r;
            HashMap<String, Boolean> row = new HashMap<>();

            for (String literal : literals)
            {
                check = check >> 1;
                row.put(literal, (i & check) == check);
            }

            values.put(i, expressionTree.reduceBy(row).getMode());
        }

        return new TruthTableImpl(literals, values);
    }

    @Override
    public DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize)
    {
        boolean leaf = truthTable.isLeaf();
        String literal = maximize ? truthTable.getMaxLiteral() : truthTable.getMinLiteral();

        DecisionTreeImpl decisionTree = new DecisionTreeImpl(
            truthTable,
            leaf ? null : literal,
            leaf ? null : fromTruthTableToDecisionTree(truthTable.reduceBy(literal, false), maximize),
            leaf ? null : fromTruthTableToDecisionTree(truthTable.reduceBy(literal, true), maximize)
        );

        ExpressionTree expressionTree = fromDecisionTreeToExpressionTree(decisionTree).reduce();

        return decisionTree.setExpression(expressionTree.getExpression());
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree)
    {
        switch (decisionTree.getType())
        {
            case LEAF:
                return new ExpressionTreeImpl(ExpressionTreeType.OPERATOR, decisionTree.getMode(), null,
                    new TreeSet<>(), new ArrayList<>());

            case LITERAL:
                return new ExpressionTreeImpl(ExpressionTreeType.LITERAL, decisionTree.getMode(),
                    decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>());

            case LATERAL_1:
                TreeSet<ExpressionTree> chr = new TreeSet<>();

                chr.add(new ExpressionTreeImpl(ExpressionTreeType.LITERAL, decisionTree.getMode(),
                    decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                chr.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(!decisionTree.getMode())));

                return new ExpressionTreeImpl(ExpressionTreeType.OPERATOR, false, null, chr,
                    new ArrayList<>());

            default:
                TreeSet<ExpressionTree> children1 = new TreeSet<>();

                children1.add(new ExpressionTreeImpl(ExpressionTreeType.LITERAL, false,
                    decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                children1.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(false)));

                TreeSet<ExpressionTree> children2 = new TreeSet<>();

                children2.add(new ExpressionTreeImpl(ExpressionTreeType.LITERAL, true,
                    decisionTree.getLiteral(), new TreeSet<>(), new ArrayList<>()));

                children2.add(fromDecisionTreeToExpressionTree(decisionTree.getSubDecisionTree(true)));

                TreeSet<ExpressionTree> children = new TreeSet<>();

                children.add(new ExpressionTreeImpl(ExpressionTreeType.OPERATOR, true, null, children1,
                    new ArrayList<>()));

                children.add(new ExpressionTreeImpl(ExpressionTreeType.OPERATOR, true, null, children2,
                    new ArrayList<>()));

                return new ExpressionTreeImpl(ExpressionTreeType.OPERATOR, false, null, children,
                    new ArrayList<>());
        }
    }
}
