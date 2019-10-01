package mhe.logic.decisiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;
import mhe.logic.AbstractLogicFunction;
import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.TruthTable;

public class AbstractDecisionTree extends AbstractLogicFunction implements DecisionTree {

	private String literal;
	private Double average;
	private Double entropy;
	private DecisionTree zero;
	private DecisionTree one;
	
	public AbstractDecisionTree(List<String> literals, String literal, Double average, Double entropy, DecisionTree zero, DecisionTree one) {
		super(literals);
		this.literal = literal;
		this.average = average;
		this.entropy = entropy;
		this.zero = zero;
		this.one = one;
	}

	@Override
	public DecisionTree reduceBy(String literal, Boolean value) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put(literal, value);
		return (DecisionTree) this.reduceBy(map);
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
		return value ? this.one : this.zero;
	}

	@Override
	public String getLiteral() {
		return this.literal;
	}

	@Override
	public Boolean isLeaf() {
		return this.entropy == 0;
	}

	@Override
	public Boolean getLeafValue() {
		return this.isLeaf() ? this.getAverage() == 0.0 ? false : true : null;
	}

	@Override
	public Double getEntropy() {
		return this.entropy;
	}

	@Override
	public Double getAverage() {
		return this.average;
	}

	@Override
	public int getSerial() {
		return this.hashCode();
	}

	@Override
	public String getShape() {
		return "\"rectangle\"";
	}

	@Override
	public String getLabel() {
		return "\"" + (this.getLiteral() == null ? "" : this.getLiteral() + " (" + (Math.round(this.getEntropy() * 100.0) / 100.0) + "): ") + this.getAverage() + "\"";
	}

	@Override
	public String getColor() {
		return "\"" + (this.isLeaf() ? this.getLeafValue() ? "blue" : "red" : "black") + "\"";
	}

	@Override
	public int compareTo(GraphVizNode arg0) {
		return this.getSerial() - arg0.getSerial();
	}
	
	@Override
	public Collection<GraphVizLink> getLinks() {
		ArrayList<GraphVizLink> ret = new ArrayList<GraphVizLink>();
		
		if(this.zero != null) {
			ret.add(new GraphVizDefaultLink(this, this.zero, null, "\"" + this.getLiteral() + " = 0\"", null));
		}
		
		if(this.one != null) {
			ret.add(new GraphVizDefaultLink(this, this.one, null, "\"" + this.getLiteral() + " = 1\"", null));
		}
		
		return ret;
	}
	
	@Override
	public String toJsonString() {
		if(this.isLeaf()) {
			return this.getLeafValue() ? "true" : "false";
		}

		String ret = "{\"literal\":\"" + this.getLiteral() + "\"";
		ret+= ",\"expression\":\"" + this.getSerial() + "\"";
		ret+= ",\"entropy\":" + this.getEntropy();
		ret+= ",\"average\":" + this.getAverage();
		ret+= ",\"false\":" + this.zero.toJsonString();
		ret+= ",\"true\":" + this.one.toJsonString();
		return ret + "}";
	}
}
