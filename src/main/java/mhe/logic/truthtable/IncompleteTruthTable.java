package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.simple.JSONObject;
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
		Map<Integer,Boolean> newValues = new HashMap<Integer,Boolean>();
		
		for(String lit : this.getLiterals()) {
			if(!removedLiterals.contains(lit)) {
				newLiterals.add(lit);
			}
		}
		
		for(Entry<Integer, Boolean> entry : this.values.entrySet()) {
			Map<String, Boolean> auxValues = position2map(entry.getKey(), this.getReversedLiterals());
			
			if(subset(auxValues, values)) {
				newValues.put(entry.getKey(), entry.getValue());
			}
		}
		
		return new IncompleteTruthTable(newLiterals, newValues);
	}
	
	public JSONObject toJson() {
		return null;
		
	}

}
