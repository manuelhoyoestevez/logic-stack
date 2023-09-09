package com.mhe.dev.compiler.logic.core.logic;

import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTreeImpl;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTreeImpl;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTreeType;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTableImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            values.put(i, expressionTree.calculate(row));
        }

        return new TruthTableImpl(literals, values);
    }

    @Override
    public DecisionTree fromTruthTableToDecisionTree(TruthTable truthTable, boolean maximize)
    {
        boolean leaf = truthTable.isLeaf();
        String literal = maximize ? truthTable.getMaxLiteral() : truthTable.getMinLiteral();

        return new DecisionTreeImpl(
            truthTable,
            leaf ? null : literal,
            leaf ? null : fromTruthTableToDecisionTree(truthTable.reduceBy(literal, false), maximize),
            leaf ? null : fromTruthTableToDecisionTree(truthTable.reduceBy(literal, true), maximize)
        );
    }

    private Map<String, Boolean> copy(Map<String, Boolean> route)
    {
        return route.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void run(
            DecisionTree decisionTree,
            Map<String, Boolean> currentRoute,
            List<Map<String, Boolean>> routes,
            boolean target
    )
    {
        if (decisionTree.isLeaf())
        {
            if (target == decisionTree.getLeafValue())
            {
                routes.add(copy(currentRoute));
            }

            return;
        }

        String literal = decisionTree.getLiteral();

        currentRoute.put(literal, false);

        run(decisionTree.getSubDecisionTree(false), currentRoute, routes, target);

        currentRoute.put(literal, true);

        run(decisionTree.getSubDecisionTree(true), currentRoute, routes, target);

        currentRoute.remove(literal);
    }

    private ExpressionTree routeToTerm(Map<String, Boolean> route, boolean mode, List<String> weights)
    {
        List<ExpressionTree> children = route
                .entrySet()
                .stream()
                .map(e -> ExpressionTreeImpl.createLiteralExpressionTree(e.getValue(), e.getKey()))
                .collect(Collectors.toList());

        return ExpressionTreeImpl.createOperatorExpressionTree(mode, children, weights);
    }

    private ExpressionTree fromDecisionTreeToMaxiExpressionTree(DecisionTree decisionTree, List<String> weights)
    {
        Map<String, Boolean> currentRoute = new HashMap<>();
        List<Map<String, Boolean>> routes = new ArrayList<>();
        run(decisionTree, currentRoute, routes, true);

        List<ExpressionTree> children = routes
                .stream()
                .map(r -> routeToTerm(r, true, weights))
                .collect(Collectors.toList());

        return ExpressionTreeImpl.createOperatorExpressionTree(false, children, weights);
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree)
    {
        return fromDecisionTreeToMaxiExpressionTree(decisionTree, new ArrayList<>());
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(DecisionTree decisionTree, List<String> weights)
    {
        return fromDecisionTreeToMaxiExpressionTree(decisionTree, weights);
    }
}
