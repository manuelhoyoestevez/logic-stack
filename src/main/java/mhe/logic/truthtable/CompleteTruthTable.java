package mhe.logic.truthtable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mhe.logic.TruthTable;

public class CompleteTruthTable extends AbstractTruthTable {
	private Map<Integer, Boolean> values;

	public CompleteTruthTable(List<String> literals, List<Boolean> values) {
		super(literals);
		this.values = new ArrayMap(values);


		for(int i = 0; i < values.size(); i++) {
			this.addValue(position2map(i, this.getReversedLiterals()), values.get(i));
		}

		this.setBranchLiteral();
	}

	@Override
	public Map<Integer, Boolean> getValues() {
		return this.values;
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
			Map<String, Boolean> auxValues = position2map(i, this.getReversedLiterals());

			if(subset(auxValues, values)) {
				newValues.add(this.getValues().get(i));
			}
		}

		return new CompleteTruthTable(newLiterals, newValues);
	}





	public static void main(String[] args) {
		List<String> literals = new ArrayList<String>();
		literals.add("a");
		literals.add("b");
		literals.add("c");
		literals.add("d");

		List<Boolean> values = new ArrayList<Boolean>();
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(false);
		values.add(false);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(true);
		values.add(false);
		values.add(false);

		CompleteTruthTable table = new CompleteTruthTable(literals, values);

		System.out.println(table.toJson().toString());
		System.out.println(table.toString());

		//DecisionTree decisionTree = table.toDecisionTree();

		//System.out.println(GraphViz.drawTree(decisionTree, "asdasd"));
	}
}
