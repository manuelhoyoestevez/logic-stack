package mhe.compiler.logic;

import java.util.*;
import java.util.Map.Entry;
import mhe.graphviz.*;

public class LogicFunctionCache implements LogicFunctionCacheInterface {
	class Row {
		Map<String, Boolean> row;
		Boolean result;
		
		Row(Map<String, Boolean> row, Boolean result){
			this.row = row;
			this.result = result;
		}

		String toString(Set<String> usedLiterals) {
			String ret = "";
			
			for(String literal : usedLiterals) {
				ret+= (this.row.get(literal) ? "1" : "0") + "|";
			}
			
			ret += this.result ? "1" : "0";
			
			return ret;
		}
		
		String toJsonString(Set<String> usedLiterals) {
			String ret = "[";
			
			for(String literal : usedLiterals) {
				ret+= this.row.get(literal) ? "1" : "0";
				ret+= ",";
			}
			
			ret+= this.result ? "1" : "0";
			
			
			return ret + "]";
		}
	}
	
	public static final double log2 = Math.log(2);
	
	public static double log2(double number) {
		return Math.log(number) / log2;
	}
	
	private boolean calculated;
	private boolean expanded;
	private LogicFunctionInterface function;
	private List<Row> rows;
	private Map<Boolean, Integer> distribution;
	private Map<String, LogicLiteralCache> litPartition;
	private Map<Boolean, LogicFunctionCacheInterface> branches;
	private String branchLiteral;
	
	private void addRow(Row row) {
		this.rows.add(row);
		Integer value = this.distribution.get(row.result) + 1;
		this.distribution.put(row.result, value);
	}
	
	public LogicFunctionCache(LogicFunctionInterface function) {
		this.function = function;
		this.calculated = false;
		this.expanded = false;
		this.rows = new LinkedList<Row>();
		this.distribution = new HashMap<Boolean, Integer>();
		this.distribution.put(false, 0);
		this.distribution.put(true,  0);
		this.litPartition = new HashMap<String, LogicLiteralCache>();
		this.branches = new HashMap<Boolean, LogicFunctionCacheInterface>();
	}
	
	@Override
	public boolean isCalculated() {
		return this.calculated;
	}
	
	@Override
	public boolean isExpanded() {
		return this.expanded;
	}
	
	@Override
	public LogicFunctionInterface getLogicFunction() {
		return this.function;
	}
	
	@Override
	public Integer getRowsCount() {
		return this.rows.size();
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

	@Override
	public LogicFunctionCacheInterface calculate() {
		if(!this.isCalculated()) {
			this.calculated = true;

			int raw, length = 1;
			LinkedList<String> reversed = new LinkedList<String>();

			for(String literal : this.getLogicFunction().getUsedLiterals()) {
				length = length * 2;
				reversed.addFirst(literal);
				litPartition.put(literal, new LogicLiteralCache(literal));
			}

			for(int i = 0; i < length; i++) {
				raw = i;
				HashMap<String, Boolean> row = new HashMap<String, Boolean>();
				
				for(String literal : reversed) {
					row.put(literal, (raw & 1) == 1);
					raw = raw >> 1;
				}
				
				Boolean value = this.getLogicFunction().getValueFrom(row);
				
				if(value != null) {
					this.addRow(new Row(row, value));
					
					for(String literal : reversed) {
						litPartition.get(literal).addValue(row.get(literal), value);
					}
				}
			}
			
			Double entropy = this.getEntropy(), max = null;
			for(String literal : reversed) {
				Double earning = entropy + this.litPartition.get(literal).getEntropy();
				
				if(max == null || earning > max) {
					max = earning;
					this.branchLiteral = literal;
				}
			}
		}

		return this;
	}
	
	@Override
	public LogicFunctionCacheInterface expand() {
		
		if(this.isLeaf()) {
			this.expanded = true;
		}
		else if(!this.isExpanded()) {
			this.expanded = true;
			Map<String, Boolean> values0 = new HashMap<String, Boolean>();
			values0.put(this.branchLiteral, false);
			
			Map<String, Boolean> values1 = new HashMap<String, Boolean>();
			values1.put(this.branchLiteral, true);
			
			this.branches.put(false, (new LogicFunctionCache(this.getLogicFunction().reduceBy(values0))).calculate().expand());
			this.branches.put(true,  (new LogicFunctionCache(this.getLogicFunction().reduceBy(values1))).calculate().expand());
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		String ret = "L = " + this.getRowsCount() + ", D: [ 0: " + this.distribution.get(false) + " | 1: " + this.distribution.get(true) + " ], E = " + this.getEntropy() + ", A = " + this.getAverage() + ", F: ";
		
		if(this.isLeaf()) {
			ret+= "YES (" + this.getLeafValue() + ")";
		}
		else {
			ret+= "NO";
		}
		
		ret+= "\r\n";
		
		for(String literal : this.getLogicFunction().getUsedLiterals()) {
			ret+= this.litPartition.get(literal).toString() + "\r\n";
		}
		
		ret+= "\r\n";
		
		for(String literal : this.getLogicFunction().getUsedLiterals()) {
			ret+= literal + "|";
		}
		
		ret+= "\r\n";
		
		for(Row row : this.rows) {
			ret += row.toString(this.getLogicFunction().getUsedLiterals()) + "\r\n";
		}
		
		return ret;
	}

	@Override
	public int getSerial() {
		return this.hashCode();
	}

	@Override
	public String getShape() {
		return "\"rectangle\"";
	}

	@Override
	public String getLabel() {
		return "\"" + this.getLogicFunction().getExpression() + "\"";
	}

	@Override
	public String getColor() {
		return "\"" + (this.isLeaf() ? this.getLeafValue() ? "blue" : "red" : "black") + "\"";
	}

	@Override
	public int compareTo(GraphVizNode arg0) {
		return this.getSerial() - arg0.getSerial();
	}
	
	@Override
	public Collection<GraphVizLink> getLinks() {
		ArrayList<GraphVizLink> ret = new ArrayList<GraphVizLink>();
		for(Entry<Boolean, LogicFunctionCacheInterface> entry : this.branches.entrySet()) {
			ret.add(new GraphVizDefaultLink(this, entry.getValue(), null, "\"" + this.branchLiteral + " = " + (entry.getKey() ? "1" : "0") + "\"", null));
		}
		return ret;
	}
	
	@Override
	public String jsonTruthTable() {
		String ret = "{\"literals\":[";
		Set<String> literals = this.getLogicFunction().getUsedLiterals();
		
		boolean f = true;
		
		for(String literal : literals) {
			if(f) {
				f = false;
			}
			else {
				ret+= ",";
			}
			
			ret+= "\"" + literal + "\"";
		}
		
		ret+= "],\"rows\":[";
				
		f = true;
		
		for(Row row : this.rows) {
			if(f) {
				f = false;
			}
			else {
				ret+= ",";
			}
			ret += row.toJsonString(literals);
		}
		
		return ret + "]}";
	}
	
	@Override
	public String jsonDecisionTree() {
		if(this.isLeaf()) {
			return this.getLeafValue() ? "1" : "0";
		}
		
		String ret = "{\"literal\":\"" + this.branchLiteral + "\"";
		ret+= ",\"expression\":\"" + this.function.getExpression() + "\"";
		ret+= ",\"entropy\":" + this.getEntropy();
		ret+= ",\"average\":" + this.getAverage();
		ret+= ",\"false\":" + this.branches.get(false).jsonDecisionTree();
		ret+= ",\"true\":" + this.branches.get(true).jsonDecisionTree();
		return ret + "}";
	}
}
