package com.mhe.dev.compiler.logic.core.logic.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TruthTableImplTest
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
    public void fromTruthTableToDecisionTreeOk()
    {

    }
}