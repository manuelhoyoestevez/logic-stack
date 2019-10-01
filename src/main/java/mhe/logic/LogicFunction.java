package mhe.logic;

import java.util.Map;
import java.util.List;

public interface LogicFunction {
	public List<String> getLiterals();
	public LogicFunction reduceBy(String literal, Boolean value);
	public LogicFunction reduceBy(Map<String, Boolean> values);
	public ExpressionTree toExpressionTree();
	public TruthTable toTruthTable();
	public DecisionTree toDecisionTree();
	public String toJsonString();
}
