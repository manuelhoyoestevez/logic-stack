package com.mhe.dev.compiler.logic.core.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.logic.core.logic.exception.TruthTableException;
import com.mhe.dev.compiler.logic.core.logic.model.DecisionTree;
import com.mhe.dev.compiler.logic.core.logic.model.ExpressionTree;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTable;
import com.mhe.dev.compiler.logic.core.logic.model.TruthTableImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LogicConverterImplTest
{
    private final static Boolean [] VALUES_A = {
            true,  false, false, null,
            false, null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private final static Boolean [] VALUES_B = {
            false, false, false, null,
            false, null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private final static Boolean [] VALUES_C = {
            false, false, false, null,
            true,  null,  true,  true,
            null,  true,  false, true,
            false, true,  false, null
    };

    private static final String LOGIC_CODE = ""
            + "A =  cu ->  fc; // Un custom system SIEMPRE es de fecha cerrada\n"
            + "B =  cu -> !md; // Un custom system NUNCA es multidraw\n"
            + "C =  cu -> !dc; // Un custom system NUNCA es un producto destacado\n"
            + "D =  cu -> !dv; // Un custom system NUNCA tiene descuento por volumen\n"
            + "E =  cu -> !ps; // Un custom system SIEMPRE tiene una única participación\n"
            + "F =  cu -> !tk; // Un custom system NUNCA puede ser pagado con tickets\n"
            + "G = !fc -> !md; // Un producto de fecha abierta NUNCA es multidraw\n"
            + "H =  dv -> !pr; // Un producto con descuentos por volumen NUNCA puede tener promociones directas\n"
            + "return [A, B, C, D, E, F, G, H];";

    private final static TruthTableImpl truthTableA = new TruthTableImpl(literals(), values(VALUES_A));
    private final static TruthTableImpl truthTableB = new TruthTableImpl(literals(), values(VALUES_B));
    private final static TruthTableImpl truthTableC = new TruthTableImpl(literals(), values(VALUES_C));

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
        testTruthTable(truthTableA, false, false, "d", null);
    }

    public void testTruthTable(TruthTable truthTable, boolean maximize, boolean leaf, String literal, Boolean mode)
    {
        LogicConverter logicConverter = new LogicConverterImpl();

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, maximize);

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

    @Test
    public void testToExpressionTree1() throws CompilerException
    {
        LogicConverter logicConverter = new LogicConverterImpl();

        ExpressionTree expressionTree = logicConverter.toExpressionTree(LOGIC_CODE);
        String expression = expressionTree.getExpression();

        assertEquals(41, expressionTree.getSize());
        assertEquals("[{!cu,fc},{!cu,!md},{!cu,!dc},{!cu,!dv},{!cu,!ps},{!cu,!tk},{!!fc,!md},{!dv,!pr}]", expression);

        TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(expressionTree);

        assertEquals(0.8675071344816212, truthTable.getEntropy());
        assertEquals(0.2890625, truthTable.getAverage());
        assertEquals("cu", truthTable.getMinLiteral());
        assertEquals("ps", truthTable.getMaxLiteral());
        assertEquals(256, truthTable.getSize());
        assertFalse(truthTable.isLeaf());

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, true);

        ExpressionTree calculatedExpressionTree = logicConverter.fromDecisionTreeToExpressionTree(decisionTree, true, new ArrayList<>());

        assertEquals(1017, calculatedExpressionTree.getSize());
        assertEquals("[{cu,dv,pr,ps,!md,tk,fc,dc},{!cu,dv,pr,ps,md,tk,fc,dc},{!cu,dv,pr,ps,!md,tk,dc},{!cu,!dv,pr,ps,md,tk,fc,dc},{!cu,!dv,pr,ps,md,tk,!fc,dc},{cu,!dv,pr,ps,!md,tk,fc,dc},{!cu,!dv,pr,ps,!md,tk,dc},{!dv,cu,!pr,ps,md,tk,fc,dc},{!dv,cu,!pr,ps,md,tk,!fc,dc},{dv,cu,!pr,ps,!md,tk,fc,dc},{!dv,cu,!pr,ps,!md,tk,dc},{dv,!cu,!pr,ps,md,tk,fc,dc},{dv,!cu,!pr,ps,!md,tk,dc},{!dv,!cu,!pr,ps,tk,dc},{!cu,dv,pr,ps,md,tk,fc,!dc},{!cu,dv,pr,ps,md,tk,!fc,!dc},{cu,dv,pr,ps,!md,tk,fc,!dc},{!cu,dv,pr,ps,!md,tk,!dc},{!cu,!dv,pr,ps,md,tk,fc,!dc},{!cu,!dv,pr,ps,md,tk,!fc,!dc},{cu,!dv,pr,ps,!md,tk,fc,!dc},{!cu,!dv,pr,ps,!md,tk,!dc},{!dv,cu,!pr,ps,md,tk,fc,!dc},{!cu,!pr,ps,md,tk,fc,!dc},{!dv,cu,!pr,ps,md,tk,!fc,!dc},{!cu,!pr,ps,md,tk,!fc,!dc},{dv,cu,!pr,ps,!md,tk,fc,!dc},{!dv,cu,!pr,ps,!md,tk,!dc},{!cu,!pr,ps,!md,tk,!dc},{!cu,dv,pr,ps,md,!tk,fc,dc},{!cu,dv,pr,ps,md,!tk,!fc,dc},{cu,dv,pr,ps,!md,!tk,fc,dc},{!cu,dv,pr,ps,!md,!tk,dc},{!cu,!dv,pr,ps,md,!tk,fc,dc},{!cu,!dv,pr,ps,md,!tk,!fc,dc},{cu,!dv,pr,ps,!md,!tk,fc,dc},{!cu,!dv,pr,ps,!md,!tk,dc},{!dv,cu,!pr,ps,md,!tk,fc,dc},{!cu,!pr,ps,md,!tk,fc,dc},{!dv,cu,!pr,ps,md,!tk,!fc,dc},{!cu,!pr,ps,md,!tk,!fc,dc},{dv,cu,!pr,ps,!md,!tk,fc,dc},{!dv,cu,!pr,ps,!md,!tk,dc},{!cu,!pr,ps,!md,!tk,dc},{!cu,dv,pr,ps,md,!tk,fc,!dc},{!cu,dv,pr,ps,md,!tk,!fc,!dc},{cu,dv,pr,ps,!md,!tk,fc,!dc},{!cu,dv,pr,ps,!md,!tk,!dc},{!cu,!dv,pr,ps,md,!tk,fc,!dc},{!cu,!dv,pr,ps,md,!tk,!fc,!dc},{cu,!dv,pr,ps,!md,!tk,fc,!dc},{!cu,!dv,pr,ps,!md,!tk,!dc},{!dv,cu,!pr,ps,md,!tk,fc,!dc},{!cu,!pr,ps,md,!tk,fc,!dc},{!dv,cu,!pr,ps,md,!tk,!fc,!dc},{!cu,!pr,ps,md,!tk,!fc,!dc},{dv,cu,!pr,ps,!md,!tk,fc,!dc},{!dv,cu,!pr,ps,!md,!tk,!dc},{!cu,!pr,ps,!md,!tk,!dc},{!cu,dv,pr,!ps,md,tk,fc,dc},{!cu,dv,pr,!ps,md,tk,!fc,dc},{cu,dv,pr,!ps,!md,tk,fc,dc},{!cu,dv,pr,!ps,!md,tk,dc},{!cu,!dv,pr,!ps,md,tk,fc,dc},{!cu,!dv,pr,!ps,md,tk,!fc,dc},{cu,!dv,pr,!ps,!md,tk,fc,dc},{!cu,!dv,pr,!ps,!md,tk,dc},{!dv,cu,!pr,!ps,md,tk,fc,dc},{!cu,!pr,!ps,md,tk,fc,dc},{!dv,cu,!pr,!ps,md,tk,!fc,dc},{!cu,!pr,!ps,md,tk,!fc,dc},{dv,cu,!pr,!ps,!md,tk,fc,dc},{!dv,cu,!pr,!ps,!md,tk,dc},{!cu,!pr,!ps,!md,tk,dc},{!cu,dv,pr,!ps,md,tk,fc,!dc},{!cu,dv,pr,!ps,md,tk,!fc,!dc},{cu,dv,pr,!ps,!md,tk,fc,!dc},{!cu,dv,pr,!ps,!md,tk,!dc},{!cu,!dv,pr,!ps,md,tk,fc,!dc},{!cu,!dv,pr,!ps,md,tk,!fc,!dc},{cu,!dv,pr,!ps,!md,tk,fc,!dc},{!cu,!dv,pr,!ps,!md,tk,!dc},{!dv,cu,!pr,!ps,md,tk,fc,!dc},{!cu,!pr,!ps,md,tk,fc,!dc},{!dv,cu,!pr,!ps,md,tk,!fc,!dc},{!cu,!pr,!ps,md,tk,!fc,!dc},{dv,cu,!pr,!ps,!md,tk,fc,!dc},{!dv,cu,!pr,!ps,!md,tk,!dc},{!cu,!pr,!ps,!md,tk,!dc},{!cu,dv,pr,!ps,md,!tk,fc,dc},{!cu,dv,pr,!ps,md,!tk,!fc,dc},{cu,dv,pr,!ps,!md,!tk,fc,dc},{!cu,dv,pr,!ps,!md,!tk,dc},{!cu,!dv,pr,!ps,md,!tk,fc,dc},{!cu,!dv,pr,!ps,md,!tk,!fc,dc},{cu,!dv,pr,!ps,!md,!tk,fc,dc},{!cu,!dv,pr,!ps,!md,!tk,dc},{!dv,cu,!pr,!ps,md,!tk,fc,dc},{!cu,!pr,!ps,md,!tk,fc,dc},{!dv,cu,!pr,!ps,md,!tk,!fc,dc},{!cu,!pr,!ps,md,!tk,!fc,dc},{dv,cu,!pr,!ps,!md,!tk,fc,dc},{!dv,cu,!pr,!ps,!md,!tk,dc},{!cu,!pr,!ps,!md,!tk,dc},{!cu,dv,pr,!ps,md,!tk,fc,!dc},{!cu,dv,pr,!ps,md,!tk,!fc,!dc},{cu,dv,pr,!ps,!md,!tk,fc,!dc},{!cu,dv,pr,!ps,!md,!tk,!dc},{!cu,!dv,pr,!ps,md,!tk,fc,!dc},{!cu,!dv,pr,!ps,md,!tk,!fc,!dc},{cu,!dv,pr,!ps,!md,!tk,fc,!dc},{!cu,!dv,pr,!ps,!md,!tk,!dc},{!dv,cu,!pr,!ps,md,!tk,fc,!dc},{!cu,!pr,!ps,md,!tk,fc,!dc},{!dv,cu,!pr,!ps,md,!tk,!fc,!dc},{!cu,!pr,!ps,md,!tk,!fc,!dc},{dv,cu,!pr,!ps,!md,!tk,fc,!dc},{!dv,cu,!pr,!ps,!md,!tk,!dc},{!cu,!pr,!ps,!md,!tk,!dc}]", calculatedExpressionTree.getExpression());
    }

    @Test
    public void testToExpressionTree2() throws CompilerException
    {
        LogicConverter logicConverter = new LogicConverterImpl();

        ExpressionTree expressionTree = logicConverter.toExpressionTree("return [{!cu,fc},{!cu,!md},{!cu,!dc},{!cu,!dv},{!cu,!ps},{!cu,!tk},{!!fc,!md},{!dv,!pr}] == [{cu,dv,pr,ps,!md,tk,fc,dc},{!cu,dv,pr,ps,md,tk,fc,dc},{!cu,dv,pr,ps,!md,tk,dc},{!cu,!dv,pr,ps,md,tk,fc,dc},{!cu,!dv,pr,ps,md,tk,!fc,dc},{cu,!dv,pr,ps,!md,tk,fc,dc},{!cu,!dv,pr,ps,!md,tk,dc},{!dv,cu,!pr,ps,md,tk,fc,dc},{!dv,cu,!pr,ps,md,tk,!fc,dc},{dv,cu,!pr,ps,!md,tk,fc,dc},{!dv,cu,!pr,ps,!md,tk,dc},{dv,!cu,!pr,ps,md,tk,fc,dc},{dv,!cu,!pr,ps,!md,tk,dc},{!dv,!cu,!pr,ps,tk,dc},{!cu,dv,pr,ps,md,tk,fc,!dc},{!cu,dv,pr,ps,md,tk,!fc,!dc},{cu,dv,pr,ps,!md,tk,fc,!dc},{!cu,dv,pr,ps,!md,tk,!dc},{!cu,!dv,pr,ps,md,tk,fc,!dc},{!cu,!dv,pr,ps,md,tk,!fc,!dc},{cu,!dv,pr,ps,!md,tk,fc,!dc},{!cu,!dv,pr,ps,!md,tk,!dc},{!dv,cu,!pr,ps,md,tk,fc,!dc},{!cu,!pr,ps,md,tk,fc,!dc},{!dv,cu,!pr,ps,md,tk,!fc,!dc},{!cu,!pr,ps,md,tk,!fc,!dc},{dv,cu,!pr,ps,!md,tk,fc,!dc},{!dv,cu,!pr,ps,!md,tk,!dc},{!cu,!pr,ps,!md,tk,!dc},{!cu,dv,pr,ps,md,!tk,fc,dc},{!cu,dv,pr,ps,md,!tk,!fc,dc},{cu,dv,pr,ps,!md,!tk,fc,dc},{!cu,dv,pr,ps,!md,!tk,dc},{!cu,!dv,pr,ps,md,!tk,fc,dc},{!cu,!dv,pr,ps,md,!tk,!fc,dc},{cu,!dv,pr,ps,!md,!tk,fc,dc},{!cu,!dv,pr,ps,!md,!tk,dc},{!dv,cu,!pr,ps,md,!tk,fc,dc},{!cu,!pr,ps,md,!tk,fc,dc},{!dv,cu,!pr,ps,md,!tk,!fc,dc},{!cu,!pr,ps,md,!tk,!fc,dc},{dv,cu,!pr,ps,!md,!tk,fc,dc},{!dv,cu,!pr,ps,!md,!tk,dc},{!cu,!pr,ps,!md,!tk,dc},{!cu,dv,pr,ps,md,!tk,fc,!dc},{!cu,dv,pr,ps,md,!tk,!fc,!dc},{cu,dv,pr,ps,!md,!tk,fc,!dc},{!cu,dv,pr,ps,!md,!tk,!dc},{!cu,!dv,pr,ps,md,!tk,fc,!dc},{!cu,!dv,pr,ps,md,!tk,!fc,!dc},{cu,!dv,pr,ps,!md,!tk,fc,!dc},{!cu,!dv,pr,ps,!md,!tk,!dc},{!dv,cu,!pr,ps,md,!tk,fc,!dc},{!cu,!pr,ps,md,!tk,fc,!dc},{!dv,cu,!pr,ps,md,!tk,!fc,!dc},{!cu,!pr,ps,md,!tk,!fc,!dc},{dv,cu,!pr,ps,!md,!tk,fc,!dc},{!dv,cu,!pr,ps,!md,!tk,!dc},{!cu,!pr,ps,!md,!tk,!dc},{!cu,dv,pr,!ps,md,tk,fc,dc},{!cu,dv,pr,!ps,md,tk,!fc,dc},{cu,dv,pr,!ps,!md,tk,fc,dc},{!cu,dv,pr,!ps,!md,tk,dc},{!cu,!dv,pr,!ps,md,tk,fc,dc},{!cu,!dv,pr,!ps,md,tk,!fc,dc},{cu,!dv,pr,!ps,!md,tk,fc,dc},{!cu,!dv,pr,!ps,!md,tk,dc},{!dv,cu,!pr,!ps,md,tk,fc,dc},{!cu,!pr,!ps,md,tk,fc,dc},{!dv,cu,!pr,!ps,md,tk,!fc,dc},{!cu,!pr,!ps,md,tk,!fc,dc},{dv,cu,!pr,!ps,!md,tk,fc,dc},{!dv,cu,!pr,!ps,!md,tk,dc},{!cu,!pr,!ps,!md,tk,dc},{!cu,dv,pr,!ps,md,tk,fc,!dc},{!cu,dv,pr,!ps,md,tk,!fc,!dc},{cu,dv,pr,!ps,!md,tk,fc,!dc},{!cu,dv,pr,!ps,!md,tk,!dc},{!cu,!dv,pr,!ps,md,tk,fc,!dc},{!cu,!dv,pr,!ps,md,tk,!fc,!dc},{cu,!dv,pr,!ps,!md,tk,fc,!dc},{!cu,!dv,pr,!ps,!md,tk,!dc},{!dv,cu,!pr,!ps,md,tk,fc,!dc},{!cu,!pr,!ps,md,tk,fc,!dc},{!dv,cu,!pr,!ps,md,tk,!fc,!dc},{!cu,!pr,!ps,md,tk,!fc,!dc},{dv,cu,!pr,!ps,!md,tk,fc,!dc},{!dv,cu,!pr,!ps,!md,tk,!dc},{!cu,!pr,!ps,!md,tk,!dc},{!cu,dv,pr,!ps,md,!tk,fc,dc},{!cu,dv,pr,!ps,md,!tk,!fc,dc},{cu,dv,pr,!ps,!md,!tk,fc,dc},{!cu,dv,pr,!ps,!md,!tk,dc},{!cu,!dv,pr,!ps,md,!tk,fc,dc},{!cu,!dv,pr,!ps,md,!tk,!fc,dc},{cu,!dv,pr,!ps,!md,!tk,fc,dc},{!cu,!dv,pr,!ps,!md,!tk,dc},{!dv,cu,!pr,!ps,md,!tk,fc,dc},{!cu,!pr,!ps,md,!tk,fc,dc},{!dv,cu,!pr,!ps,md,!tk,!fc,dc},{!cu,!pr,!ps,md,!tk,!fc,dc},{dv,cu,!pr,!ps,!md,!tk,fc,dc},{!dv,cu,!pr,!ps,!md,!tk,dc},{!cu,!pr,!ps,!md,!tk,dc},{!cu,dv,pr,!ps,md,!tk,fc,!dc},{!cu,dv,pr,!ps,md,!tk,!fc,!dc},{cu,dv,pr,!ps,!md,!tk,fc,!dc},{!cu,dv,pr,!ps,!md,!tk,!dc},{!cu,!dv,pr,!ps,md,!tk,fc,!dc},{!cu,!dv,pr,!ps,md,!tk,!fc,!dc},{cu,!dv,pr,!ps,!md,!tk,fc,!dc},{!cu,!dv,pr,!ps,!md,!tk,!dc},{!dv,cu,!pr,!ps,md,!tk,fc,!dc},{!cu,!pr,!ps,md,!tk,fc,!dc},{!dv,cu,!pr,!ps,md,!tk,!fc,!dc},{!cu,!pr,!ps,md,!tk,!fc,!dc},{dv,cu,!pr,!ps,!md,!tk,fc,!dc},{!dv,cu,!pr,!ps,!md,!tk,!dc},{!cu,!pr,!ps,!md,!tk,!dc}];");

        assertEquals(3021, expressionTree.getSize());
        assertEquals("[{[{!cu,fc},{!cu,!md},{!cu,!dc},{!cu,!dv},{!cu,!ps},{!cu,!tk},{!!fc,!md},{!dv,!pr}],![{cu,dv,pr,ps,!md,tk,fc,dc},{!cu,dv,pr,ps,md,tk,fc,dc},{!cu,dv,pr,ps,!md,tk,dc},{!cu,!dv,pr,ps,md,tk,fc,dc},{!cu,!dv,pr,ps,md,tk,!fc,dc},{cu,!dv,pr,ps,!md,tk,fc,dc},{!cu,!dv,pr,ps,!md,tk,dc},{!dv,cu,!pr,ps,md,tk,fc,dc},{!dv,cu,!pr,ps,md,tk,!fc,dc},{dv,cu,!pr,ps,!md,tk,fc,dc},{!dv,cu,!pr,ps,!md,tk,dc},{dv,!cu,!pr,ps,md,tk,fc,dc},{dv,!cu,!pr,ps,!md,tk,dc},{!dv,!cu,!pr,ps,tk,dc},{!cu,dv,pr,ps,md,tk,fc,!dc},{!cu,dv,pr,ps,md,tk,!fc,!dc},{cu,dv,pr,ps,!md,tk,fc,!dc},{!cu,dv,pr,ps,!md,tk,!dc},{!cu,!dv,pr,ps,md,tk,fc,!dc},{!cu,!dv,pr,ps,md,tk,!fc,!dc},{cu,!dv,pr,ps,!md,tk,fc,!dc},{!cu,!dv,pr,ps,!md,tk,!dc},{!dv,cu,!pr,ps,md,tk,fc,!dc},{!cu,!pr,ps,md,tk,fc,!dc},{!dv,cu,!pr,ps,md,tk,!fc,!dc},{!cu,!pr,ps,md,tk,!fc,!dc},{dv,cu,!pr,ps,!md,tk,fc,!dc},{!dv,cu,!pr,ps,!md,tk,!dc},{!cu,!pr,ps,!md,tk,!dc},{!cu,dv,pr,ps,md,!tk,fc,dc},{!cu,dv,pr,ps,md,!tk,!fc,dc},{cu,dv,pr,ps,!md,!tk,fc,dc},{!cu,dv,pr,ps,!md,!tk,dc},{!cu,!dv,pr,ps,md,!tk,fc,dc},{!cu,!dv,pr,ps,md,!tk,!fc,dc},{cu,!dv,pr,ps,!md,!tk,fc,dc},{!cu,!dv,pr,ps,!md,!tk,dc},{!dv,cu,!pr,ps,md,!tk,fc,dc},{!cu,!pr,ps,md,!tk,fc,dc},{!dv,cu,!pr,ps,md,!tk,!fc,dc},{!cu,!pr,ps,md,!tk,!fc,dc},{dv,cu,!pr,ps,!md,!tk,fc,dc},{!dv,cu,!pr,ps,!md,!tk,dc},{!cu,!pr,ps,!md,!tk,dc},{!cu,dv,pr,ps,md,!tk,fc,!dc},{!cu,dv,pr,ps,md,!tk,!fc,!dc},{cu,dv,pr,ps,!md,!tk,fc,!dc},{!cu,dv,pr,ps,!md,!tk,!dc},{!cu,!dv,pr,ps,md,!tk,fc,!dc},{!cu,!dv,pr,ps,md,!tk,!fc,!dc},{cu,!dv,pr,ps,!md,!tk,fc,!dc},{!cu,!dv,pr,ps,!md,!tk,!dc},{!dv,cu,!pr,ps,md,!tk,fc,!dc},{!cu,!pr,ps,md,!tk,fc,!dc},{!dv,cu,!pr,ps,md,!tk,!fc,!dc},{!cu,!pr,ps,md,!tk,!fc,!dc},{dv,cu,!pr,ps,!md,!tk,fc,!dc},{!dv,cu,!pr,ps,!md,!tk,!dc},{!cu,!pr,ps,!md,!tk,!dc},{!cu,dv,pr,!ps,md,tk,fc,dc},{!cu,dv,pr,!ps,md,tk,!fc,dc},{cu,dv,pr,!ps,!md,tk,fc,dc},{!cu,dv,pr,!ps,!md,tk,dc},{!cu,!dv,pr,!ps,md,tk,fc,dc},{!cu,!dv,pr,!ps,md,tk,!fc,dc},{cu,!dv,pr,!ps,!md,tk,fc,dc},{!cu,!dv,pr,!ps,!md,tk,dc},{!dv,cu,!pr,!ps,md,tk,fc,dc},{!cu,!pr,!ps,md,tk,fc,dc},{!dv,cu,!pr,!ps,md,tk,!fc,dc},{!cu,!pr,!ps,md,tk,!fc,dc},{dv,cu,!pr,!ps,!md,tk,fc,dc},{!dv,cu,!pr,!ps,!md,tk,dc},{!cu,!pr,!ps,!md,tk,dc},{!cu,dv,pr,!ps,md,tk,fc,!dc},{!cu,dv,pr,!ps,md,tk,!fc,!dc},{cu,dv,pr,!ps,!md,tk,fc,!dc},{!cu,dv,pr,!ps,!md,tk,!dc},{!cu,!dv,pr,!ps,md,tk,fc,!dc},{!cu,!dv,pr,!ps,md,tk,!fc,!dc},{cu,!dv,pr,!ps,!md,tk,fc,!dc},{!cu,!dv,pr,!ps,!md,tk,!dc},{!dv,cu,!pr,!ps,md,tk,fc,!dc},{!cu,!pr,!ps,md,tk,fc,!dc},{!dv,cu,!pr,!ps,md,tk,!fc,!dc},{!cu,!pr,!ps,md,tk,!fc,!dc},{dv,cu,!pr,!ps,!md,tk,fc,!dc},{!dv,cu,!pr,!ps,!md,tk,!dc},{!cu,!pr,!ps,!md,tk,!dc},{!cu,dv,pr,!ps,md,!tk,fc,dc},{!cu,dv,pr,!ps,md,!tk,!fc,dc},{cu,dv,pr,!ps,!md,!tk,fc,dc},{!cu,dv,pr,!ps,!md,!tk,dc},{!cu,!dv,pr,!ps,md,!tk,fc,dc},{!cu,!dv,pr,!ps,md,!tk,!fc,dc},{cu,!dv,pr,!ps,!md,!tk,fc,dc},{!cu,!dv,pr,!ps,!md,!tk,dc},{!dv,cu,!pr,!ps,md,!tk,fc,dc},{!cu,!pr,!ps,md,!tk,fc,dc},{!dv,cu,!pr,!ps,md,!tk,!fc,dc},{!cu,!pr,!ps,md,!tk,!fc,dc},{dv,cu,!pr,!ps,!md,!tk,fc,dc},{!dv,cu,!pr,!ps,!md,!tk,dc},{!cu,!pr,!ps,!md,!tk,dc},{!cu,dv,pr,!ps,md,!tk,fc,!dc},{!cu,dv,pr,!ps,md,!tk,!fc,!dc},{cu,dv,pr,!ps,!md,!tk,fc,!dc},{!cu,dv,pr,!ps,!md,!tk,!dc},{!cu,!dv,pr,!ps,md,!tk,fc,!dc},{!cu,!dv,pr,!ps,md,!tk,!fc,!dc},{cu,!dv,pr,!ps,!md,!tk,fc,!dc},{!cu,!dv,pr,!ps,!md,!tk,!dc},{!dv,cu,!pr,!ps,md,!tk,fc,!dc},{!cu,!pr,!ps,md,!tk,fc,!dc},{!dv,cu,!pr,!ps,md,!tk,!fc,!dc},{!cu,!pr,!ps,md,!tk,!fc,!dc},{dv,cu,!pr,!ps,!md,!tk,fc,!dc},{!dv,cu,!pr,!ps,!md,!tk,!dc},{!cu,!pr,!ps,!md,!tk,!dc}]},{![{!cu,fc},{!cu,!md},{!cu,!dc},{!cu,!dv},{!cu,!ps},{!cu,!tk},{!!fc,!md},{!dv,!pr}],[{cu,dv,pr,ps,!md,tk,fc,dc},{!cu,dv,pr,ps,md,tk,fc,dc},{!cu,dv,pr,ps,!md,tk,dc},{!cu,!dv,pr,ps,md,tk,fc,dc},{!cu,!dv,pr,ps,md,tk,!fc,dc},{cu,!dv,pr,ps,!md,tk,fc,dc},{!cu,!dv,pr,ps,!md,tk,dc},{!dv,cu,!pr,ps,md,tk,fc,dc},{!dv,cu,!pr,ps,md,tk,!fc,dc},{dv,cu,!pr,ps,!md,tk,fc,dc},{!dv,cu,!pr,ps,!md,tk,dc},{dv,!cu,!pr,ps,md,tk,fc,dc},{dv,!cu,!pr,ps,!md,tk,dc},{!dv,!cu,!pr,ps,tk,dc},{!cu,dv,pr,ps,md,tk,fc,!dc},{!cu,dv,pr,ps,md,tk,!fc,!dc},{cu,dv,pr,ps,!md,tk,fc,!dc},{!cu,dv,pr,ps,!md,tk,!dc},{!cu,!dv,pr,ps,md,tk,fc,!dc},{!cu,!dv,pr,ps,md,tk,!fc,!dc},{cu,!dv,pr,ps,!md,tk,fc,!dc},{!cu,!dv,pr,ps,!md,tk,!dc},{!dv,cu,!pr,ps,md,tk,fc,!dc},{!cu,!pr,ps,md,tk,fc,!dc},{!dv,cu,!pr,ps,md,tk,!fc,!dc},{!cu,!pr,ps,md,tk,!fc,!dc},{dv,cu,!pr,ps,!md,tk,fc,!dc},{!dv,cu,!pr,ps,!md,tk,!dc},{!cu,!pr,ps,!md,tk,!dc},{!cu,dv,pr,ps,md,!tk,fc,dc},{!cu,dv,pr,ps,md,!tk,!fc,dc},{cu,dv,pr,ps,!md,!tk,fc,dc},{!cu,dv,pr,ps,!md,!tk,dc},{!cu,!dv,pr,ps,md,!tk,fc,dc},{!cu,!dv,pr,ps,md,!tk,!fc,dc},{cu,!dv,pr,ps,!md,!tk,fc,dc},{!cu,!dv,pr,ps,!md,!tk,dc},{!dv,cu,!pr,ps,md,!tk,fc,dc},{!cu,!pr,ps,md,!tk,fc,dc},{!dv,cu,!pr,ps,md,!tk,!fc,dc},{!cu,!pr,ps,md,!tk,!fc,dc},{dv,cu,!pr,ps,!md,!tk,fc,dc},{!dv,cu,!pr,ps,!md,!tk,dc},{!cu,!pr,ps,!md,!tk,dc},{!cu,dv,pr,ps,md,!tk,fc,!dc},{!cu,dv,pr,ps,md,!tk,!fc,!dc},{cu,dv,pr,ps,!md,!tk,fc,!dc},{!cu,dv,pr,ps,!md,!tk,!dc},{!cu,!dv,pr,ps,md,!tk,fc,!dc},{!cu,!dv,pr,ps,md,!tk,!fc,!dc},{cu,!dv,pr,ps,!md,!tk,fc,!dc},{!cu,!dv,pr,ps,!md,!tk,!dc},{!dv,cu,!pr,ps,md,!tk,fc,!dc},{!cu,!pr,ps,md,!tk,fc,!dc},{!dv,cu,!pr,ps,md,!tk,!fc,!dc},{!cu,!pr,ps,md,!tk,!fc,!dc},{dv,cu,!pr,ps,!md,!tk,fc,!dc},{!dv,cu,!pr,ps,!md,!tk,!dc},{!cu,!pr,ps,!md,!tk,!dc},{!cu,dv,pr,!ps,md,tk,fc,dc},{!cu,dv,pr,!ps,md,tk,!fc,dc},{cu,dv,pr,!ps,!md,tk,fc,dc},{!cu,dv,pr,!ps,!md,tk,dc},{!cu,!dv,pr,!ps,md,tk,fc,dc},{!cu,!dv,pr,!ps,md,tk,!fc,dc},{cu,!dv,pr,!ps,!md,tk,fc,dc},{!cu,!dv,pr,!ps,!md,tk,dc},{!dv,cu,!pr,!ps,md,tk,fc,dc},{!cu,!pr,!ps,md,tk,fc,dc},{!dv,cu,!pr,!ps,md,tk,!fc,dc},{!cu,!pr,!ps,md,tk,!fc,dc},{dv,cu,!pr,!ps,!md,tk,fc,dc},{!dv,cu,!pr,!ps,!md,tk,dc},{!cu,!pr,!ps,!md,tk,dc},{!cu,dv,pr,!ps,md,tk,fc,!dc},{!cu,dv,pr,!ps,md,tk,!fc,!dc},{cu,dv,pr,!ps,!md,tk,fc,!dc},{!cu,dv,pr,!ps,!md,tk,!dc},{!cu,!dv,pr,!ps,md,tk,fc,!dc},{!cu,!dv,pr,!ps,md,tk,!fc,!dc},{cu,!dv,pr,!ps,!md,tk,fc,!dc},{!cu,!dv,pr,!ps,!md,tk,!dc},{!dv,cu,!pr,!ps,md,tk,fc,!dc},{!cu,!pr,!ps,md,tk,fc,!dc},{!dv,cu,!pr,!ps,md,tk,!fc,!dc},{!cu,!pr,!ps,md,tk,!fc,!dc},{dv,cu,!pr,!ps,!md,tk,fc,!dc},{!dv,cu,!pr,!ps,!md,tk,!dc},{!cu,!pr,!ps,!md,tk,!dc},{!cu,dv,pr,!ps,md,!tk,fc,dc},{!cu,dv,pr,!ps,md,!tk,!fc,dc},{cu,dv,pr,!ps,!md,!tk,fc,dc},{!cu,dv,pr,!ps,!md,!tk,dc},{!cu,!dv,pr,!ps,md,!tk,fc,dc},{!cu,!dv,pr,!ps,md,!tk,!fc,dc},{cu,!dv,pr,!ps,!md,!tk,fc,dc},{!cu,!dv,pr,!ps,!md,!tk,dc},{!dv,cu,!pr,!ps,md,!tk,fc,dc},{!cu,!pr,!ps,md,!tk,fc,dc},{!dv,cu,!pr,!ps,md,!tk,!fc,dc},{!cu,!pr,!ps,md,!tk,!fc,dc},{dv,cu,!pr,!ps,!md,!tk,fc,dc},{!dv,cu,!pr,!ps,!md,!tk,dc},{!cu,!pr,!ps,!md,!tk,dc},{!cu,dv,pr,!ps,md,!tk,fc,!dc},{!cu,dv,pr,!ps,md,!tk,!fc,!dc},{cu,dv,pr,!ps,!md,!tk,fc,!dc},{!cu,dv,pr,!ps,!md,!tk,!dc},{!cu,!dv,pr,!ps,md,!tk,fc,!dc},{!cu,!dv,pr,!ps,md,!tk,!fc,!dc},{cu,!dv,pr,!ps,!md,!tk,fc,!dc},{!cu,!dv,pr,!ps,!md,!tk,!dc},{!dv,cu,!pr,!ps,md,!tk,fc,!dc},{!cu,!pr,!ps,md,!tk,fc,!dc},{!dv,cu,!pr,!ps,md,!tk,!fc,!dc},{!cu,!pr,!ps,md,!tk,!fc,!dc},{dv,cu,!pr,!ps,!md,!tk,fc,!dc},{!dv,cu,!pr,!ps,!md,!tk,!dc},{!cu,!pr,!ps,!md,!tk,!dc}]}]", expressionTree.getExpression());

        TruthTable truthTable = logicConverter.fromExpressionTreeToTruthTable(expressionTree);

        assertEquals(0, truthTable.getEntropy());
        assertEquals(1, truthTable.getAverage());
        assertEquals("cu", truthTable.getMinLiteral());
        assertEquals("cu", truthTable.getMaxLiteral());
        assertEquals(256, truthTable.getSize());
        assertTrue(truthTable.getLeafValue());

        DecisionTree decisionTree = logicConverter.fromTruthTableToDecisionTree(truthTable, false);

        assertTrue(decisionTree.isLeaf());
        assertSame(truthTable, decisionTree.getTruthTable());
        assertNull(decisionTree.getLiteral());
        assertEquals(true, decisionTree.getMode());

        ExpressionTree calculatedExpressionTree = logicConverter.fromDecisionTreeToExpressionTree(decisionTree, false, new ArrayList<>());

        assertEquals(1, calculatedExpressionTree.getSize());
        assertEquals("1", calculatedExpressionTree.getExpression());
    }
}