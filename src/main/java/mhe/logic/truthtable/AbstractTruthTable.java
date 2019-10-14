package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import mhe.logic.AbstractLogicFunction;
import mhe.logic.TruthTable;

public abstract class AbstractTruthTable extends AbstractLogicFunction implements TruthTable {

    /**
     * Mapa de recuentos para trues y falses
     */
    private Map<Boolean, Integer> distribution;

    /**
     * Mapa de recuentos por literal
     */
    private Map<String, LiteralDistribution> literalPartition;

    /**
     * Entropía: Nivel de diferenciación entre ceros y unos.
     */
    private Double entropy;

    /**
     * Media: Valor medio de los resultados
     */
    private Double average;

    /**
     * Literal de decisión
     */
    private String literal;

    /**
     * Lista de literales en orden inverso
     */
    private LinkedList<String> reversed;

    /**
     * Constructor
     * @param literals Lista de literales
     */
    public AbstractTruthTable(List<String> literals) {
        super();
        this.setLiterals(literals);
        this.distribution = new HashMap<Boolean, Integer>();
        this.distribution.put(false, 0);
        this.distribution.put(true, 0);
        this.literalPartition = new HashMap<String, LiteralDistribution>();

        for(String literal : this.getLiterals()) {
            this.literalPartition.put(literal, new LiteralDistribution(literal));
        }
    }

    @Override
    public String getLiteral() {
        return this.literal;
    }

    @Override
    public Integer getRowsCount() {
        return this.getValues().size();
    }

    @Override
    public Boolean getResult(Integer position) {
        return this.getValues().get(position);
    }

    @Override
    public Boolean getResult(Map<String, Boolean> values) {
        return this.getResult(map2position(values, this.getReversedLiterals()));
    }

    @Override
    public Double getEntropy() {
        return this.entropy;
    }

    @Override
    public Double getAverage() {
        return this.average;
    }

    @Override
    public Map<Boolean, Integer> getDistribution() {
        return this.distribution;
    }

    @Override
    public Boolean isLeaf() {
        Double entropy = this.getEntropy();
        return entropy != null && entropy == 0;
    }

    public Map<String, LiteralDistribution> getLiteralPartition() {
        return literalPartition;
    }

    @Override
    public Boolean getLeafValue() {
        Double avg = this.getAverage();

        if(avg == null) {
            return null;
        }

        if(avg == 0) {
            return false;
        }

        if(avg == 1) {
            return true;
        }

        return null;
    }

    @Override
    public TruthTable reduceBy(String literal, Boolean value) {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put(literal, value);
        return this.reduceBy(map);
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject ret = new JSONObject();
        ret.put("literals", this.getLiterals());
        ret.put("entropy",  this.getEntropy());
        ret.put("average",  this.getAverage());
        ret.put("values",   this.getValues());
        return ret;
    }

    @Override
    public String toJsonString() {
        return this.toJson().toString();
    }

    @Override
    public String toString() {
        String ret = "L = "
                + this.getRowsCount() + ", D: [ 0: "
                + this.getDistribution().get(false) + " | 1: "
                + this.getDistribution().get(true) + " ], E = "
                + this.getEntropy() + ", A = "
                + this.getAverage() + ", F: ";

        if(this.isLeaf()) {
            ret+= "YES (" + this.getLeafValue() + ")";
        }
        else {
            ret+= "NO";
        }

        ret+= ". X = " + this.getLiteral() + "\r\n";

        for(String literal : this.getLiterals()) {
            ret+= this.getLiteralPartition().get(literal).toString() + "\r\n";
        }

        ret+= "\r\n";

        for(String literal : this.getLiterals()) {
            ret+= literal + "|";
        }

        ret+= "\r\n";

        for(Entry<Integer, Boolean> entry : this.getValues().entrySet()) {
            int i = entry.getKey();

            Map<String, Boolean> map = position2map(i, this.getReversedLiterals());
            for(String literal : this.getLiterals()) {
                ret += (map.get(literal) ? "1" : "0") + "|";
            }

            ret+= (entry.getValue() ? "1" : "0") + "\r\n";
        }

        return ret;
    }

    @Override
    protected void setLiterals(List<String> literals) {
        super.setLiterals(literals);
        this.reversed = new LinkedList<String>();
        for(String literal : this.getLiterals()) {
            this.reversed.addFirst(literal);
        }
    }

    protected List<String> getReversedLiterals() {
        return this.reversed;
    }

    protected String setBranchLiteral() {
        this.entropy = this.calculateEntropy();
        this.average = this.calculateAverage();

        Double max = null;
        for(String literal : this.getLiterals()) {
            Double earning = entropy + this.getLiteralPartition().get(literal).getEntropy();

            if(max == null || earning > max) {
                max = earning;
                this.literal = literal;
            }
        }

        return this.literal;
    }

    protected void addValue (Map<String, Boolean> row, Boolean value) {
        if(value != null) {
            Integer counter = this.getDistribution().get(value) + 1;

            this.getDistribution().put(value, counter);

            for(String literal : this.getLiterals()) {
                this.getLiteralPartition().get(literal).addValue(row.get(literal), value);
            }
        }
    }

    protected Double calculateEntropy() {
        Integer d = this.getRowsCount();

        if(d == 0) {
            return null;
        }

        Double r = 0.0;

        for(Integer value : this.distribution.values()) {
            double P = (double) value / (double) this.getRowsCount();
            if(P != 0) {
                r -= P * log2(P);
            }
        }

        return r;
    }

    protected Double calculateAverage() {
        Double d = (double) this.getRowsCount();

        if(d == 0) {
            return null;
        }

        Double n = (double) this.distribution.get(true);
        return n / d;
    }

    public static final double log2 = Math.log(2);

    public static double log2(double number) {
        return Math.log(number) / log2;
    }

    /**
     * A contiene a B. B es subconjunto de A.
     * @param a Conjunto contenedor
     * @param b conjunto contenido
     * @return true sí A contiene a B
     */
    protected static boolean subset(Map<String, Boolean> a, Map<String, Boolean> b) {
        for(Entry<String, Boolean> entry : b.entrySet()) {
            if(a.get(entry.getKey()) != entry.getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * A menos B. Elementos de A que no están en B.
     * @param a
     * @param b
     * @return
     */
    protected static Map<String, Boolean> diff(Map<String, Boolean> a, Map<String, Boolean> b) {
        Map<String, Boolean> ret = new HashMap<String, Boolean>();

        // Para todos los elementos de a
        for(Entry<String, Boolean> entry : a.entrySet()) {
            Boolean value = b.get(entry.getKey());

            if(value == null) {
                ret.put(entry.getKey(), entry.getValue());
            }
            else if(value != entry.getValue()) {
                return null;
            }
        }

        return ret;
    }

    protected static Map<String, Boolean> position2map(int i, List<String> reversed) {
        Map<String, Boolean> ret = new HashMap<String, Boolean>();

        for(int j = 0; j < reversed.size(); j++) {
            ret.put(reversed.get(j), (((i >> j) & 1) == 1));
        }

        return ret;
    }

    protected static Integer map2position(Map<String, Boolean> values, List<String> reversed) {
        Integer ret = 0;

        for (Entry<String, Boolean> entry : values.entrySet()) {
            int weight = reversed.indexOf(entry.getKey());
            ret += (entry.getValue() ? 1 : 0) << weight;
        }

        return ret;
    }

    public static void main(String[] args) {
        List<String> reversed = new ArrayList<String>();
        reversed.add("b");
        reversed.add("a");
        reversed.add("d");
        reversed.add("c");

        int position = 4;

        Map<String, Boolean> map = position2map(position, reversed);

        JSONObject jsonValues = values2Json(map);

        System.out.println(jsonValues.toString());

        Object position2 = map2position(map, reversed);

        System.out.println(position2);
    }
}
