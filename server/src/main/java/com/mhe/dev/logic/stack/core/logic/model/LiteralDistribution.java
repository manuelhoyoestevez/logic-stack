package com.mhe.dev.logic.stack.core.logic.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

class LiteralDistribution
{
    private static final double LOG_2 = Math.log(2);
    private final String literal;
    private final Map<Boolean, Integer> totals;
    private final Map<Boolean, Map<Boolean, Integer>> subtotals;
    private int total;

    public LiteralDistribution(String literal)
    {
        this.total = 0;
        this.literal = literal;

        this.totals = new HashMap<>();
        this.subtotals = new HashMap<>();

        Map<Boolean, Integer> zeros = new HashMap<>();
        Map<Boolean, Integer> ones = new HashMap<>();

        zeros.put(false, 0);
        zeros.put(true, 0);

        ones.put(false, 0);
        ones.put(true, 0);

        this.totals.put(false, 0);
        this.totals.put(true, 0);

        this.subtotals.put(false, zeros);
        this.subtotals.put(true, ones);
    }

    private static double log2(double number)
    {
        return Math.log(number) / LOG_2;
    }

    public String getLiteral()
    {
        return this.literal;
    }

    public void addValue(boolean litValue, boolean varValue)
    {
        total++;
        totals.compute(litValue, (k, n) -> n == null ? 1 : n + 1);
        subtotals.get(litValue).compute(varValue, (k, n) -> n == null ? 1 : n + 1);
    }

    public double getEntropy()
    {
        if (total <= 0.0)
        {
            return 0.0;
        }

        double r = 0.0;

        for (Entry<Boolean, Integer> entry : totals.entrySet())
        {
            double s = 0.0;
            double i = entry.getValue();

            if (i > 0.0)
            {
                for (Integer j : subtotals.get(entry.getKey()).values())
                {
                    if (j > 0)
                    {
                        double p = ((double) j) / i;
                        s += p * log2(p);
                    }
                }
                r += s * i / total;
            }
        }

        return r;
    }

    public String toString()
    {
        boolean e;
        boolean f = true;

        StringBuilder ret = new StringBuilder("" + this.literal + ": E = " + this.getEntropy() + ",\tT = " + this.total
            + ", D: { ");

        for (Entry<Boolean, Integer> entry1 : this.totals.entrySet())
        {
            if (f)
            {
                f = false;
            } else
            {
                ret.append(", ");
            }

            ret.append(entry1.getKey() ? "1" : "0").append(": ").append(entry1.getValue()).append(" [ ");

            e = true;

            for (Entry<Boolean, Integer> entry2 : this.subtotals.get(entry1.getKey()).entrySet())
            {
                if (e)
                {
                    e = false;
                } else
                {
                    ret.append(" | ");
                }
                ret.append(entry2.getKey() ? "1" : "0").append(": ").append(entry2.getValue());
            }

            ret.append(" ]");
        }

        ret.append(" }");

        return ret.toString();
    }
}
