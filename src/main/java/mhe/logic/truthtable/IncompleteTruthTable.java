package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mhe.logic.LogicFunction;
import mhe.logic.TruthTable;

public class IncompleteTruthTable extends AbstractTruthTable {
	private Map<Integer,Boolean> values;

	public IncompleteTruthTable(List<String> literals, Map<Integer,Boolean> values) {
		super(literals);
		this.values = values;
	}
	
	public Map<Integer,Boolean> getValues() {
		return this.values;
	}

	@Override
	public Boolean getResult(Integer position) {
		return this.values.get(position);
	}

	@Override
	public TruthTable reduceBy(Map<String, Boolean> values) {
		Set<String> removedLiterals = values.keySet();
		List<String> newLiterals = new ArrayList<String>();
		Map<Integer,Boolean> newValues = new HashMap<Integer,Boolean>();
		
		for(String lit : this.getLiterals()) {
			if(!removedLiterals.contains(lit)) {
				newLiterals.add(lit);
			}
		}
		
		for(Entry<Integer, Boolean> entry : this.getValues().entrySet()) {
			Integer i = entry.getKey();
			Map<String, Boolean> auxValues = this.position2map(i);
			
			if(subset(auxValues, values)) {
				newValues.put(key, value);
			}
		}
		
		return new IncompleteTruthTable(newLiterals, newValues);
	}

}
