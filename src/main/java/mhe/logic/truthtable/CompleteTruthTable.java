package mhe.logic.truthtable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;

import mhe.logic.TruthTable;

public class CompleteTruthTable extends AbstractTruthTable {
	private List<Boolean> values;
	private String branchLiteral = null;
	

	public CompleteTruthTable(List<String> literals, List<Boolean> values) {
		super(literals);
		this.values = values;

		for(int i = 0; i < values.size(); i++) {
			Boolean value = values.get(i);
			Map<String, Boolean> row = this.position2map(i);
			
			if(value != null) {
				Integer counter = this.getDistribution().get(value);
				counter = counter == null ? 1 : counter + 1;
				this.getDistribution().put(value, counter);
		
				
				for(String literal : this.getReversedLiterals()) {
					this.getLiteralPartition().get(literal).addValue(row.get(literal), value);
				}
			}
		}
		
		Double entropy = this.getEntropy(), max = null;
		for(String literal : this.getReversedLiterals()) {
			Double earning = entropy + this.getLiteralPartition().get(literal).getEntropy();
			
			if(max == null || earning > max) {
				max = earning;
				this.branchLiteral = literal;
			}
		}
	}
	
	public List<Boolean> getValues() {
		return this.values;
	}
	
	public String getBranchLiteral() {
		return this.branchLiteral;
	}
	
	@Override
	public Integer getRowsCount() {
		return this.getValues().size();
	}
	
	@Override
	public Boolean getResult(Integer position) {
		if(this.values.size() >= position) {
			return null;
		}

		return this.getValues().get(position);
	}
	
	@Override
	public TruthTable reduceBy(Map<String, Boolean> values) {
		Set<String> removedLiterals = values.keySet();
		List<String> newLiterals = new ArrayList<String>();
		List<Boolean> newValues = new ArrayList<Boolean>();
		
		for(String lit : this.getLiterals()) {
			if(!removedLiterals.contains(lit)) {
				newLiterals.add(lit);
			}
		}
		
		for(int i = 0; i < this.getValues().size(); i++) {
			Map<String, Boolean> auxValues = this.position2map(i);
			
			if(subset(auxValues, values)) {
				newValues.add(this.getValues().get(i));
			}
		}
		
		return new CompleteTruthTable(newLiterals, newValues);
	}
	/*
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
	*/
	
	public JSONObject toJson() {
		return null;
		
	}
}
