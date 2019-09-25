package mhe.logic.truthtable;

import java.util.List;
import java.util.Map;

import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.LogicFunction;
import mhe.logic.TruthTable;

public class AbstractTruthTable implements TruthTable {
	
	// literales y valores
	public AbstractTruthTable(List<String> literals) {
		
	}

	@Override
	public LogicFunction reduceBy(String literal, Boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicFunction reduceBy(Map<String, Boolean> values) {
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
	public List<String> getLiterals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getRowsCount() {
		// TODO Auto-generated method stub
		return null;
	}
}
