package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;

public class AbstractTruthTable implements TruthTable {
	private List<String> literals;
	private List<Boolean> values;
	
	public AbstractTruthTable(List<String> literals, List<Boolean> values) {
		this.literals = literals;
		this.values = values;
	}
	
	@Override
	public List<String> getLiterals() {
		return this.literals;
	}
	
	@Override
	public List<Boolean> getValues() {
		return this.values;
	}

	@Override
	public TruthTable reduceBy(String literal, Boolean value) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(literal, value);
		return this.reduceBy(map);
	}

	@Override
	public TruthTable reduceBy(Map<String, Boolean> values) {
		LinkedList<String> reversed = new LinkedList<String>();
		ArrayList<Boolean> newValues = new ArrayList<Boolean>();
		
		for(String literal : this.getLiterals()) {
			reversed.addFirst(literal);
		}
		
		for(int i = 0; i < this.getValues().size(); i++) {
			for(int j = 0; j < reversed.size(); j++) {
				if(values.get(reversed.get(j)) == (((i << j) & 1) == 1)) {
					newValues.add(this.getValues().get(i));
				}
			}
		}
		
		
		// TODO Auto-generated method stub
		return null;
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
	public Boolean getResult(Integer position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLiteralValues(Integer position) {
		// TODO Auto-generated method stub
		return null;
	}
}
