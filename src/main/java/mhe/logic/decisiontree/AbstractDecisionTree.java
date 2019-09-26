package mhe.logic.decisiontree;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;

public class AbstractDecisionTree implements DecisionTree {

	@Override
	public List<String> getLiterals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecisionTree reduceBy(String literal, Boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecisionTree reduceBy(Map<String, Boolean> values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionTree toExpressionTree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TruthTable toTruthTable() {
		return this.toExpressionTree().toTruthTable();
	}

	@Override
	public DecisionTree toDecisionTree() {
		return this;
	}

	@Override
	public DecisionTree getSubDecisionTree(boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getUsedLiterals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLiteral() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isLeaf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getLeafValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getEntropy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getAverage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSerial() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<GraphVizLink> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int compareTo(GraphVizNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
