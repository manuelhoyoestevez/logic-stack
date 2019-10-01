package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mhe.logic.TruthTable;

public class IncompleteTruthTable extends AbstractTruthTable {
	private Map<Integer,Boolean> values;

	public IncompleteTruthTable(List<String> literals, Map<Integer,Boolean> values) {
		super(literals);
		this.values = values;
		
		for(Entry<Integer, Boolean> entry : values.entrySet()) {
			this.addValue(position2map(entry.getKey(), this.getReversedLiterals()), entry.getValue());
		}
		
		this.setBranchLiteral();
	}
	
	public Map<Integer,Boolean> getValues() {
		return this.values;
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
	public TruthTable reduceBy(Map<String, Boolean> values) {
		Set<String> removedLiterals = values.keySet();
		List<String> newLiterals = new ArrayList<String>();
		LinkedList<String> newReversedLiterals = new LinkedList<String>();
		Map<Integer,Boolean> newValues = new HashMap<Integer,Boolean>();
		
		for(String lit : this.getLiterals()) {
			if(!removedLiterals.contains(lit)) {
				newLiterals.add(lit);
				newReversedLiterals.addFirst(lit);
			}
		}
		
		for(Entry<Integer, Boolean> entry : this.values.entrySet()) {
			Map<String, Boolean> auxValues = position2map(entry.getKey(), this.getReversedLiterals());
			Map<String, Boolean> diff = diff(auxValues, values);
			
			if(diff != null) {				
				newValues.put(map2position(diff, newReversedLiterals), entry.getValue());
			}
		}
		
		return new IncompleteTruthTable(newLiterals, newValues);
	}

	@Override
	public String toJsonString() {
		// TODO Auto-generated method stub
		return null;
	}
}
