package com.mhe.dev.compiler.logic.core.logic;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.MheDummyLogger;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.logic.core.compiler.LogicCompiler;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTreeImpl;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTreeImpl;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTableImpl;
import com.mhe.dev.compiler.logic.core.xson.XsonCompiler;
import com.mhe.dev.compiler.logic.core.xson.XsonValue;
import com.mhe.dev.compiler.logic.core.xson.impl.DefaultXsonValue;
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
    private final MheLogger logger = new MheDummyLogger();
    private final LogicCompiler logicCompiler = new LogicCompiler();
    private final XsonCompiler xsonCompiler = new XsonCompiler();

    @Override
    public ExpressionTree toExpressionTree(String expression) throws CompilerException
    {
        String json = logicCompiler.expressionToJson(expression, logger);

        XsonValue xsonValue = xsonCompiler.compile(json, logger);

        return fromXsonValueToExpressionTree(xsonValue);
    }

    private ExpressionTree fromXsonValueToExpressionTree(XsonValue xsonValue)
    {
        Map<String, XsonValue> map = xsonValue.toMap();

        String operator = map.get("operator").toString(null);
        String literal = xsonValue.getString("literal", null);

        List<String> weights = xsonValue
                .get("order", DefaultXsonValue.createXsonArray())
                .toList()
                .stream()
                .map(x -> x.toString(null))
                .collect(Collectors.toList());

        List<ExpressionTree> children = xsonValue
                .get("children", DefaultXsonValue.createXsonArray())
                .toList()
                .stream()
                .map(this::fromXsonValueToExpressionTree)
                .collect(Collectors.toList());

        switch(operator)
        {
            case "literal":
                return ExpressionTreeImpl.createLiteralExpressionTree(true, literal);
            case "and":
                return ExpressionTreeImpl.createOperatorExpressionTree(true, children, weights);
            case "or":
                return ExpressionTreeImpl.createOperatorExpressionTree(false, children, weights);
            case "not":
                return ExpressionTreeImpl.createNotExpressionTree(children.get(0));
            default:
                throw new RuntimeException("Invalid operator: " + operator);
        }
    }

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
                .map(e -> ExpressionTreeImpl.createLiteralExpressionTree(mode == e.getValue(), e.getKey()))
                .collect(Collectors.toList());

        return ExpressionTreeImpl.createOperatorExpressionTree(mode, children, weights);
    }

    @Override
    public ExpressionTree fromDecisionTreeToExpressionTree(
            DecisionTree decisionTree,
            boolean maximize,
            List<String> weights
    )
    {
        Map<String, Boolean> currentRoute = new HashMap<>();
        List<Map<String, Boolean>> routes = new ArrayList<>();
        run(decisionTree, currentRoute, routes, false);

        List<ExpressionTree> childrenA = routes
                .stream()
                .map(r -> routeToTerm(r, false, weights))
                .collect(Collectors.toList());

        ExpressionTreeImpl a = ExpressionTreeImpl.createOperatorExpressionTree(true, childrenA, weights);

        routes.clear();
        run(decisionTree, currentRoute, routes, true);
        List<ExpressionTree> childrenB = routes
                .stream()
                .map(r -> routeToTerm(r, true, weights))
                .collect(Collectors.toList());

        ExpressionTreeImpl b = ExpressionTreeImpl.createOperatorExpressionTree(false, childrenB, weights);

        if (maximize)
        {
            return a.getSize() >= b.getSize() ? a : b;
        } else
        {
            return a.getSize() <= b.getSize() ? a : b;
        }
    }
}
