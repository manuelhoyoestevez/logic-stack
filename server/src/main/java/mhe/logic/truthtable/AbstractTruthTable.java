package mhe.logic.truthtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import mhe.logic.AbstractLogicFunction;
import mhe.logic.TruthTable;
import org.json.simple.JSONObject;

/**
 * AbstractTruthTable.
 */
public abstract class AbstractTruthTable extends AbstractLogicFunction implements TruthTable {

    public static final double log2 = Math.log(2);
    /**
     * Mapa de recuentos para true y false.
     */
    private final Map<Boolean, Integer> distribution;
    /**
     * Mapa de recuentos por literal.
     */
    private final Map<String, LiteralDistribution> literalPartition;
    /**
     * Entropía: Nivel de diferenciación entre ceros y unos.
     */
    private Double entropy;
    /**
     * Media: Valor medio de los resultados.
     */
    private Double average;
    /**
     * Literal de decisión para minimizar.
     */
    private String literal;
    /**
     * Literal de decisión para maximizar.
     */
    private String maxLiteral;
    /**
     * Lista de literales en orden inverso.
     */
    private LinkedList<String> reversed;

    /**
     * Constructor.
     *
     * @param literals Lista de literales
     */
    public AbstractTruthTable(List<String> literals) {
        super();
        setLiterals(literals);
        distribution = new HashMap<>();
        distribution.put(false, 0);
        distribution.put(true, 0);
        literalPartition = new HashMap<>();

        for (String literal : getLiterals()) {
            literalPartition.put(literal, new LiteralDistribution(literal));
        }
    }

    public static double log2(double number) {
        return Math.log(number) / log2;
    }

    /**
     * A contiene a B. B es subconjunto de A.
     *
     * @param a Conjunto contenedor
     * @param b conjunto contenido
     * @return true sí A contiene a B
     */
    protected static boolean subset(Map<String, Boolean> a, Map<String, Boolean> b) {
        for (Entry<String, Boolean> entry : b.entrySet()) {
            if (a.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * A menos B. Elementos de A que no están en B.
     *
     * @param a Minuendo
     * @param b Sustraendo
     * @return Diferencia
     */
    protected static Map<String, Boolean> diff(Map<String, Boolean> a, Map<String, Boolean> b) {
        Map<String, Boolean> ret = new HashMap<>();

        // Para todos los elementos de a
        for (Entry<String, Boolean> entry : a.entrySet()) {
            Boolean value = b.get(entry.getKey());

            if (value == null) {
                ret.put(entry.getKey(), entry.getValue());
            } else if (value != entry.getValue()) {
                return null;
            }
        }

        return ret;
    }

    protected static Map<String, Boolean> position2map(int i, List<String> reversed) {
        Map<String, Boolean> ret = new HashMap<>();

        for (int j = 0; j < reversed.size(); j++) {
            ret.put(reversed.get(j), (((i >> j) & 1) == 1));
        }

        return ret;
    }

    protected static Integer map2position(Map<String, Boolean> values, List<String> reversed) {
        int ret = 0;

        for (Entry<String, Boolean> entry : values.entrySet()) {
            int weight = reversed.indexOf(entry.getKey());
            ret += (entry.getValue() ? 1 : 0) << weight;
        }

        return ret;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    @Override
    public String getMaxLiteral() {
        return maxLiteral;
    }

    @Override
    public Integer getRowsCount() {
        return getValues().size();
    }

    @Override
    public Double getEntropy() {
        return entropy;
    }

    @Override
    public Double getAverage() {
        return average;
    }

    @Override
    public Map<Boolean, Integer> getDistribution() {
        return distribution;
    }

    @Override
    public Boolean isLeaf() {
        Double entropy = getEntropy();
        return entropy != null && entropy == 0;
    }

    public Map<String, LiteralDistribution> getLiteralPartition() {
        return literalPartition;
    }

    @Override
    public Boolean getLeafValue() {
        Double avg = getAverage();

        if (avg == null) {
            return null;
        }

        if (avg == 0) {
            return false;
        }

        if (avg == 1) {
            return true;
        }

        return null;
    }

    @Override
    public TruthTable reduceBy(String literal, Boolean value) {
        Map<String, Boolean> map = new HashMap<>();
        map.put(literal, value);
        return reduceBy(map);
    }

    /**
     *  to Json.
     *
     * @return JSON Object
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject ret = new JSONObject();
        ret.put("literals", getLiterals());
        ret.put("entropy", getEntropy());
        ret.put("average", getAverage());
        ret.put("values", getValues());
        return ret;
    }

    @Override
    public String toJsonString() {
        return toJson().toString();
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("L = "
                + getRowsCount() + ", D: [ 0: "
                + getDistribution().get(false) + " | 1: "
                + getDistribution().get(true) + " ], E = "
                + getEntropy() + ", A = "
                + getAverage() + ", F: ");

        if (isLeaf()) {
            ret.append("YES (").append(getLeafValue()).append(")");
        } else {
            ret.append("NO");
        }

        ret.append(". X = ").append(getLiteral()).append("\r\n");

        for (String literal : getLiterals()) {
            ret.append(getLiteralPartition().get(literal).toString()).append("\r\n");
        }

        ret.append("\r\n");

        for (String literal : getLiterals()) {
            ret.append(literal).append("|");
        }

        ret.append("\r\n");

        for (Entry<Integer, Boolean> entry : getValues().entrySet()) {
            int i = entry.getKey();

            Map<String, Boolean> map = position2map(i, getReversedLiterals());
            for (String literal : getLiterals()) {
                ret.append(map.get(literal) ? "1" : "0").append("|");
            }

            ret.append(entry.getValue() ? "1" : "0").append("\r\n");
        }

        return ret.toString();
    }

    @Override
    protected void setLiterals(List<String> literals) {
        super.setLiterals(literals);
        reversed = new LinkedList<>();
        for (String literal : getLiterals()) {
            reversed.addFirst(literal);
        }
    }

    protected List<String> getReversedLiterals() {
        return reversed;
    }

    protected void setBranchLiteral() {
        entropy = calculateEntropy();
        average = calculateAverage();

        Double max = null;
        Double min = null;
        for (String literal : getLiterals()) {
            double earning = entropy + getLiteralPartition().get(literal).getEntropy();

            if (max == null || earning > max) {
                max = earning;
                literal = literal;
            }

            if (min == null || earning <= min) {
                min = earning;
                maxLiteral = literal;
            }
        }
    }

    protected void addValue(Map<String, Boolean> row, Boolean value) {
        if (value != null) {
            Integer counter = getDistribution().get(value) + 1;

            getDistribution().put(value, counter);

            for (String literal : getLiterals()) {
                getLiteralPartition().get(literal).addValue(row.get(literal), value);
            }
        }
    }

    protected Double calculateEntropy() {
        Integer d = getRowsCount();

        if (d == 0) {
            return null;
        }

        double r = 0.0;

        for (Integer value : distribution.values()) {
            double p = (double) value / (double) getRowsCount();
            if (p != 0) {
                r -= p * log2(p);
            }
        }

        return r;
    }

    protected Double calculateAverage() {
        double d = (double) getRowsCount();

        if (d == 0) {
            return null;
        }

        double n = (double) distribution.get(true);
        return n / d;
    }
}
