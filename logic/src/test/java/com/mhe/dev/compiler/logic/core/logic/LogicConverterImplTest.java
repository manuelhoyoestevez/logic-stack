package com.mhe.dev.compiler.logic.core.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import com.mhe.dev.compiler.logic.core.logic.exception.TruthTableException;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTreeType;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTableImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LogicConverterImplTest
{
    private final static Boolean [] valuesA = {
            true,  false, false, null,
            false, null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private final static Boolean [] valuesB = {
            false, false, false, null,
            false, null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private final static Boolean [] valuesC = {
            false, false, false, null,
            true,  null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private final static TruthTableImpl truthTableA = new TruthTableImpl(literals(), values(valuesA));
    private final static TruthTableImpl truthTableB = new TruthTableImpl(literals(), values(valuesB));
    private final static TruthTableImpl truthTableC = new TruthTableImpl(literals(), values(valuesC));

    private static List<String> literals()
    {
        ArrayList<String> literals = new ArrayList<>();
        literals.add("a");
        literals.add("b");
        literals.add("c");
        literals.add("d");
        return literals;
    }

    private static Map<Integer, Boolean> values(Boolean[] values)
    {
        Map<Integer, Boolean> ret = new HashMap<>();

        for (int i = 0; i < values.length; i++) {
            Boolean bool = values[i];

            if (bool != null) {
                ret.put(i, bool);
            }
        }

        return ret;
    }

    @Test
    public void testTruthTableA()
    {
        //testTruthTable(truthTableA, DecisionTreeType.COMPLETE, false, false, "d", null);

        //String [] weigths = {"d", "a", "b", "c"};

        LogicConverter logicConverter = new LogicConverterImpl();

        System.out.println(truthTableA);

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTableA, false);

        ExpressionTree expressionTree = logicConverter.fromDecisionTreeToExpressionTree(decisionTree);

        ExpressionTree reducedExpressionTree = expressionTree.reduce();

        TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(reducedExpressionTree);

        System.out.println(truthTable);
    }

    public void testTruthTable(TruthTable truthTable, DecisionTreeType type, boolean maximize, boolean leaf, String literal, Boolean mode)
    {
        LogicConverter logicConverter = new LogicConverterImpl();

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, maximize);

        assertEquals(type, decisionTree.getType());
        assertSame(truthTable, decisionTree.getTruthTable());
        assertEquals(leaf, decisionTree.isLeaf());
        assertEquals(literal, decisionTree.getLiteral());
        assertEquals(mode, decisionTree.getMode());

        if (!leaf)
        {
            try
            {
                decisionTree.getLeafValue();
                fail("Must throw TruthTableException");
            } catch (TruthTableException truthTableException)
            {
                assertEquals("TruthTable is not leaf", truthTableException.getMessage());
            }
        }
    }
}