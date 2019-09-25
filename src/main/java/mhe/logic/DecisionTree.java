package mhe.logic;

import java.util.Set;

import mhe.graphviz.GraphVizNode;

public interface DecisionTree extends LogicFunction, GraphVizNode {
	public DecisionTree getSubDecisionTree(boolean value);
	public Set<String> getUsedLiterals();
	public String  getLiteral();
	public Boolean isLeaf();
	public Boolean getLeafValue();
	public Double  getEntropy();
	public Double  getAverage();
	public String  toJson();
}
