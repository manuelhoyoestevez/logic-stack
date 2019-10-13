package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONObject;

import mhe.logic.TruthTable;

public class IncompleteTruthTable extends AbstractTruthTable {
	private Map<Integer, Boolean> values;

	public IncompleteTruthTable(List<String> literals, Map<Integer,Boolean> values) {
		super(literals);
		this.values = values;

		for(Entry<Integer, Boolean> entry : values.entrySet()) {
			this.addValue(position2map(entry.getKey(), this.getReversedLiterals()), entry.getValue());
		}

		this.setBranchLiteral();
	}

	@Override
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
    @SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject ret = new JSONObject();
		ret.put("literals", this.getLiterals());
		ret.put("values", this.getValues());
		return ret;
	}

	public static void main(String[] args) {
		List<String> literals = new ArrayList<String>();
		literals.add("a");
		literals.add("b");
		literals.add("c");
		literals.add("d");

		Map<Integer,Boolean> values = new HashMap<Integer,Boolean>();
		values.put( 0, true);
		values.put( 1, true);
		values.put( 2, true);
		values.put( 3, true);
		values.put( 4, true);
		values.put( 5, true);
		values.put( 6, true);
		values.put( 7, false);
		values.put( 8, true);
		values.put( 9, true);
		values.put(10, true);
		values.put(11, true);
		values.put(12, true);
		values.put(13, true);
		values.put(14, true);
		values.put(15, true);


		IncompleteTruthTable table = new IncompleteTruthTable(literals, values);

		//System.out.println(table.toJsonString());
		System.out.println(table.toString());

		//DecisionTree decisionTree = table.toDecisionTree();

		//System.out.println(GraphViz.drawTree(decisionTree, "asdasd"));
	}
}
