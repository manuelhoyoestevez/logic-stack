package mhe.compiler.logic;

import mhe.graphviz.GraphVizNode;

public interface LogicFunctionCacheInterface extends GraphVizNode {
	public LogicFunctionInterface getLogicFunction();

	public boolean isCalculated();
	public LogicFunctionCacheInterface calculate();
	
	public boolean isExpanded();
	public LogicFunctionCacheInterface expand();
	
	public Integer getRowsCount();
	public Boolean isLeaf();
	public Boolean getLeafValue();
	public Double  getEntropy();
	public Double  getAverage();
	
	public String jsonTruthTable();
	public String jsonDecisionTree();
}
