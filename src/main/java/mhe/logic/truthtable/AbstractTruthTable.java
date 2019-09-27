package mhe.logic.truthtable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;

public abstract class AbstractTruthTable implements TruthTable {
	/**
	 * 
	 */
	public static final double log2 = Math.log(2);
	
	/**
	 * 
	 * @param number
	 * @return
	 */
	public static double log2(double number) {
		return Math.log(number) / log2;
	}
	
	/**
	 * 
	 */
	private List<String> literals;
	
	/**
	 * 
	 */
	private LinkedList<String> reversed;
	
	/**
	 * Distribución de trues y falses
	 */
	private Map<Boolean, Integer> distribution;
	
	/**
	 * 
	 */
	private Map<String, LiteralDistribution> literalPartition;
	
	
	public AbstractTruthTable(List<String> literals) {
		this.literals = literals;
		this.reversed = new LinkedList<String>();
		this.distribution = new HashMap<Boolean, Integer>();
		this.literalPartition = new HashMap<String, LiteralDistribution>();
		for(String literal : this.getLiterals()) {
			this.reversed.addFirst(literal);
			this.literalPartition.put(literal, new LiteralDistribution(literal));
		}
	}
	
	public Map<Boolean, Integer> getDistribution() {
		return this.distribution;
	}
	
	public Map<String, LiteralDistribution> getLiteralPartition() {
		return literalPartition;
	}
	
	@Override
	public List<String> getLiterals() {
		return this.literals;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Boolean getResult(Map<String, Boolean> values) {
		return this.getResult(this.map2Position(values));
	}
	
	@Override
	public Double getEntropy() {
		Integer d = this.getRowsCount();
		
		if(d == 0) {
			return null;
		}
		else {
			Double r = 0.0;
			
			for(Integer value : this.distribution.values()) {
				double P = (double) value / (double) this.getRowsCount();
				if(P != 0) {
					r -= P * log2(P);
				}
			}
			
			return r;
		}
	}

	@Override
	public Double getAverage() {
		Double d = (double) this.getRowsCount();
		
		if(d == 0) {
			return null;
		}
		else {
			Double n = (double) this.distribution.get(true);
			return n / d;
		}
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
	
	protected List<String> getReversedLiterals() {
		return this.reversed;
	}
	
	protected Map<String, Boolean> position2map(int i) {
		Map<String, Boolean> ret = new HashMap<String, Boolean>();
		
		for(int j = 0; j < this.reversed.size(); j++) {
			ret.put(this.reversed.get(j), (((i << j) & 1) == 1));
		}
		
		return ret;
	}
	
	protected Integer map2Position(Map<String, Boolean> values) {
		return null;
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
}
