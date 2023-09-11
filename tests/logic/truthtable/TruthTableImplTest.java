package com.mhe.dev.logic.stack.core.logic.truthtable;

import com.mhe.dev.logic.stack.core.logic.model.TruthTableImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;

import org.junit.jupiter.api.Test;

public class TruthTableImplTest
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

    private final static TruthTableImpl truthTable = new TruthTableImpl(literals(), values(valuesC));

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
    public void truthTableParametersOk() throws CompilerIoException
    {
        System.out.println("Truth table");
        System.out.println("===========");
        System.out.println(truthTable.toString());
        /*
        assertFalse(truthTable.isLeaf());
        assertEquals(12, truthTable.getSize());
        assertEquals(16, truthTable.getMaxSize());
        assertEquals(0.5, truthTable.getAverage(), 0.0001);
        assertEquals(1.0, truthTable.getEntropy(), 0.0001);
        assertEquals("a", truthTable.getMaxLiteral());
        assertEquals("d", truthTable.getMinLiteral());
*/
        TruthTableImpl d0 = truthTable.reduceBy("d", false);
        TruthTableImpl d1 = truthTable.reduceBy("d", true);

        System.out.println("d = 0");
        System.out.println("======");
        System.out.println(d0.toString());
        System.out.println("d = 1");
        System.out.println("======");
        System.out.println(d1.toString());

        TruthTableImpl d0a0 = d0.reduceBy("a", false);
        TruthTableImpl d0a1 = d0.reduceBy("a", true);

        System.out.println("d = 0, a = 0");
        System.out.println("============");
        System.out.println(d0a0.toString());
        System.out.println("d = 0, a = 1");
        System.out.println("============");
        System.out.println(d0a1.toString());

        TruthTableImpl d1a0 = d1.reduceBy("a", false);
        TruthTableImpl d1a1 = d1.reduceBy("a", true);

        System.out.println("d = 1, a = 0");
        System.out.println("============");
        System.out.println(d1a0.toString());

        System.out.println("d = 1, a = 1");
        System.out.println("============");
        System.out.println(d1a1.toString());
    }

}