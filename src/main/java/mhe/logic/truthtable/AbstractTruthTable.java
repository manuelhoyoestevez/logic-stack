package mhe.logic.truthtable;

import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.ArrayList;

import mhe.logic.AbstractLogicFunction;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;
import mhe.logic.decisiontree.AbstractDecisionTree;

public abstract class AbstractTruthTable extends AbstractLogicFunction implements TruthTable {

	/**
	 * Mapa de recuentos para trues y falses
	 */
	private Map<Boolean, Integer> distribution;

	/**
	 * Mapa de recuentos por literal
	 */
	private Map<String, LiteralDistribution> literalPartition;
	
	private Double entropy;
	private Double average;

	/**
	 * Literal de decisión
	 */
	private String branchLiteral = null;

	/**
	 * Constructor
	 * @param literals Lista de literales
	 */
	public AbstractTruthTable(List<String> literals) {
		super(literals);
		
		this.distribution = new HashMap<Boolean, Integer>();
		this.distribution.put(false, 0);
		this.distribution.put(true, 0);
		this.literalPartition = new HashMap<String, LiteralDistribution>();
		for(String literal : this.getLiterals()) {
			this.literalPartition.put(literal, new LiteralDistribution(literal));
		}
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
	public TruthTable reduceBy(String literal, Boolean value) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(literal, value);
		return (TruthTable) this.reduceBy(map);
	}

	@Override
	public ExpressionTree toExpressionTree() {
		return this.toDecisionTree().toExpressionTree();
	}

	@Override
	public TruthTable toTruthTable() {
		return this;
	}

	@Override
	public DecisionTree toDecisionTree() {
		if(this.isLeaf()) {
			return new AbstractDecisionTree(new ArrayList<String>(), null, this.getAverage(), this.getEntropy(), null, null);
		}
		
		return new AbstractDecisionTree(
				this.getLiterals(),
				this.getBranchLiteral(),
				this.getAverage(),
				this.getEntropy(),
				this.reduceBy(this.getBranchLiteral(), false).toDecisionTree(),
				this.reduceBy(this.getBranchLiteral(),  true).toDecisionTree()
		);
	}
	
	@Override
	public Boolean getResult(Map<String, Boolean> values) {
		return this.getResult(map2position(values, this.getReversedLiterals()));
	}
	
	@Override
	public Boolean isLeaf() {
		Double entropy = this.getEntropy();
		return entropy != null && entropy == 0;
	}

	@Override
	public Boolean getLeafValue() {
		Double avg = this.getAverage();
		if(avg == null) {
			return null;
		}
		else if(avg == 0) {
			return false;
		}
		else if(avg == 1) {
			return true;
		}
		else {
			return null;
		}
	}
	
	public String getBranchLiteral() {
		return this.branchLiteral;
	}
	
	public Map<String, LiteralDistribution> getLiteralPartition() {
		return literalPartition;
	}

	protected String setBranchLiteral() {
		this.entropy = this.calculateEntropy();
		this.average = this.calculateAverage();
		
		Double max = null;
		for(String literal : this.getLiterals()) {
			Double earning = entropy + this.getLiteralPartition().get(literal).getEntropy();
			
			if(max == null || earning > max) {
				max = earning;
				this.branchLiteral = literal;
			}
		}
		
		return this.branchLiteral;
	}
	
	protected void addValue (Map<String, Boolean> row, Boolean value) {
		if(value != null) {
			Integer counter = this.getDistribution().get(value);
			
			counter++;
			
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

		for(Entry<String, Boolean> entry : a.entrySet()) {
			Boolean value = b.get(entry.getKey());

			if(value == null) {
				ret.put(entry.getKey(), value);
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
