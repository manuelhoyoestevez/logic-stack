package com.mhe.dev.logic.stack.core.logic.truthtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.mhe.dev.logic.stack.core.logic.TruthTable;

/**
 * AbstractTruthTable.
 */
public class AbstractTruthTable implements TruthTable
{
    private static final int MAX_LITERALS = 12;
    private static final double LOG_2 = Math.log(2);
    private static double log2(double number) {
        return Math.log(number) / LOG_2;
    }

    /**
     * Lista de literales.
     */
    private final List<String> literals;

    /**
     * Valores: Las claves enteras en binario representan los literales revertidos.
     *
     * Ejemplo: Literals: [a, b, c, d], clave 13 (1101) d = 1, c = 1, b = 0, a = 1
     */
    private final Map<Integer, Boolean> values;

    /**
     * Mapa de recuentos para true y false.
     */
    private final Map<Boolean, Integer> distribution = new HashMap<>();

    /**
     * Mapa de recuentos por literal.
     */
    private final Map<String, LiteralDistribution> literalPartition = new HashMap<>();

    /**
     * Entropía: Nivel de diferenciación entre ceros y unos.
     */
    private final Double entropy;

    /**
     * Media: Valor medio de los resultados.
     */
    private final Double average;

    /**
     * Literal de decisión para minimizar.
     */
    private final String minLiteral;

    /**
     * Literal de decisión para maximizar.
     */
    private final String maxLiteral;

    /**
     * Lista de literales en orden inverso.
     */
    private final LinkedList<String> reversed = new LinkedList<>();

    /**
     * Constructor.
     *
     * @param literals Lista de literales
     */
    public AbstractTruthTable(List<String> literals, Map<Integer, Boolean> values) throws TruthTableException {
        if (literals.size() > MAX_LITERALS)
        {
            throw new TruthTableException("Truth table have too many literal: " + literals.size() + ". Maximum is " + MAX_LITERALS);
        }

        if (values.size() < 1) {
            throw new TruthTableException("Truth table must have at least 1 value");
        }

        int maximum = 1 << literals.size();

        if (values.size() > maximum) {
            throw new TruthTableException("Truth table have too many entries: " + values.size() + ". Maximum is " + maximum);
        }

        this.literals = literals;
        this.values = values;

        checkLiterals();

        for (String literal : literals) {
            reversed.addFirst(literal);
            literalPartition.put(literal, new LiteralDistribution(literal));
        }

        distribution.put(false, 0);
        distribution.put(true, 0);

        for (Map.Entry<Integer, Boolean> entry : this.values.entrySet()) {
            Integer key = entry.getKey();

            if (key == null) {
                throw new TruthTableException("Null key in values");
            }

            if (key < 0 || key >= maximum) {
                throw new TruthTableException("Invalid key in values: " + key + ". Must be between [0, " + (maximum - 1) + "]");
            }

            Boolean value = entry.getValue();

            if (value == null) {
                throw new TruthTableException("Null value in values for key: " + key);
            }

            distribution.compute(value, (k, v) -> v == null ? 1 : v + 1);

            Map<String, Boolean> row = position2map(key);

            for (String literal : literals) {
                literalPartition.get(literal).addValue(row.get(literal), value);
            }
        }

        entropy = calculateEntropy();
        average = calculateAverage();

        Double max = null;
        Double min = null;
        String mnLiteral = null;
        String mxLiteral = null;

        for (String literal : literals) {
            double earning = entropy + literalPartition.get(literal).getEntropy();

            if (max == null || earning > max) {
                max = earning;
                mnLiteral = literal;
            }

            if (min == null || earning < min) {
                min = earning;
                mxLiteral = literal;
            }
        }

        minLiteral = mnLiteral;
        maxLiteral = mxLiteral;
    }

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public List<String> getLiterals() {
        return literals;
    }

    @Override
    public String getMinLiteral() {
        return minLiteral;
    }

    @Override
    public String getMaxLiteral() {
        return maxLiteral;
    }

    @Override
    public double getEntropy() {
        return entropy;
    }

    @Override
    public double getAverage() {
        return average;
    }

    @Override
    public boolean isLeaf() {
        return entropy == 0.0;
    }

    @Override
    public boolean getLeafValue() {
        double avg = average;

        if (avg == 0) {
            return false;
        }

        if (avg == 1) {
            return true;
        }

        throw new TruthTableException("TruthTable is not leaf");
    }

    @Override
    public AbstractTruthTable reduceBy(String literal, boolean value)
    {
        if (isLeaf()) {
            throw new TruthTableException("Leaves is not reducible");
        }

        List<String> newLiterals = literals.stream().filter(l -> !l.equals(literal)).collect(Collectors.toList());
        LinkedList<String> newReversedLiterals = new LinkedList<>();
        Map<Integer, Boolean> newValues = new HashMap<>();

        for (String lit : newLiterals) {
            newReversedLiterals.addFirst(lit);
        }

        for (Entry<Integer, Boolean> entry : this.values.entrySet()) {
            Map<String, Boolean> literalValues = position2map(entry.getKey());

            if (literalValues.get(literal).equals(value)) {
                Map<String, Boolean> newMap = new HashMap<>();

                for (String lit : newLiterals) {
                    newMap.put(lit, literalValues.get(lit));
                }

                Integer newKey = map2position(newMap, newReversedLiterals);
                newValues.put(newKey, entry.getValue());
            }
        }

        return new AbstractTruthTable(newLiterals, newValues);
    }

    @Override
    public String toJsonString()
    {
        return "{}";
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(""
            + "size:       : " + values.size() + ",\n"
            + "entropy     : " + entropy + ",\n"
            + "average     : " + average + ",\n"
            + "leaf        : " + (isLeaf() ? ("YES (" + getLeafValue() + ")" ) : ("NO")) + ",\n"
            + "minimal     : " + minLiteral + ",\n"
            + "maximal     : " + maxLiteral + ",\n"
            + "distribution: [ 0: " + distribution.get(false) + " | 1: "+ distribution.get(true) + " ],\n"
        );

        for (String literal : literals) {
            ret.append(this.literalPartition.get(literal).toString()).append("\r\n");
        }

        ret.append("\r\n");

        for (String literal : literals) {
            ret.append(literal).append("|");
        }

        ret.append("\r\n");

        for (int i = 0; i < (1 << literals.size()); i++) {
            Boolean value = values.get(i);
            char valueString = value == null ? 'X' : value ? '1' : '0';

            Map<String, Boolean> map = position2map(i);

            for (String literal : literals) {
                ret.append(map.get(literal) ? "1" : "0").append("|");
            }

            ret.append(" ").append(valueString).append("\r\n");

        }

        return ret.toString();
    }

    private void checkLiterals() {
        Map<String, Boolean> literalCount = new HashMap<>();

        for (String literal : literals)
        {
            if (literalCount.get(literal) != null) {
                throw new TruthTableException("Literal '" + literal + "' is repeated");
            }

            literalCount.put(literal, true);
        }
    }

    private double calculateEntropy() {
        double d = this.values.size();

        double r = 0.0;

        for (Integer value : distribution.values()) {
            double p = (double) value / d;
            if (p != 0) {
                r -= p * log2(p);
            }
        }

        return r;
    }

    private double calculateAverage() {
        double d = this.values.size();
        double n = distribution.get(true);
        return n / d;
    }

    private Map<String, Boolean> position2map(int i) {
        Map<String, Boolean> ret = new HashMap<>();

        for (int j = 0; j < reversed.size(); j++) {
            ret.put(reversed.get(j), (((i >> j) & 1) == 1));
        }

        return ret;
    }

    private static int map2position(Map<String, Boolean> values, List<String> reversed) {
        int ret = 0;

        for (Entry<String, Boolean> entry : values.entrySet()) {
            int weight = reversed.indexOf(entry.getKey());
            ret += (entry.getValue() ? 1 : 0) << weight;
        }

        return ret;
    }
}
