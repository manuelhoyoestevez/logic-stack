package mhe.logic.expressiontree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import mhe.graphviz.GraphVizDefaultLink;
import mhe.graphviz.GraphVizLink;
import mhe.graphviz.GraphVizNode;

import mhe.logic.DecisionTree;
import mhe.logic.ExpressionTree;
import mhe.logic.ExpressionTreeType;
import mhe.logic.LogicFunction;
import mhe.logic.TruthTable;

public class AbstractExpressionTree implements ExpressionTree {

	private boolean mode = false;
	private String name = null;
	private ExpressionTreeType type = null;
	private SortedSet<ExpressionTree> children = null;
	
	public AbstractExpressionTree(
			List<String> literals, 
			ExpressionTreeType type,
			boolean mode, 
			String literal, 
			SortedSet<ExpressionTree> children
	) {

		this.type = type;
		this.mode = mode;
		this.name = literal;
		this.children = children;
	}
	
	public static String quotify(String str) {
		return "\"" + str + "\"";
	}
	
	@Override
	public int getSerial() {
		return this.hashCode();
	}

	@Override
	public boolean getMode() {
		return this.mode;
	}
	
	@Override
	public String getLiteral() {
		return this.name;
	}
	
	@Override
	public ExpressionTreeType getType() {
		return this.type;
	}
	
	@Override
	public SortedSet<ExpressionTree> getChildren() {
		return this.children;
	}
	
	@Override
	public boolean isFinal() {
		return this.getChildren().isEmpty();
	}

	@Override
	public List<String> getLiterals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogicFunction reduceBy(String literal, Boolean value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ExpressionTree toExpressionTree() {
		return this;
	}

	@Override
	public TruthTable toTruthTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DecisionTree toDecisionTree() {
		return this.toTruthTable().toDecisionTree();
	}
	
	@Override
	public int compareTo(GraphVizNode gnode) {
		ExpressionTree node = (ExpressionTree) gnode;
		int ret = this.getExpression().compareTo(node.getExpression());

		if(ret == 0) {
			ret = this.hashCode() - gnode.hashCode();
		}

		return ret;
	}

	@Override
	public Collection<GraphVizLink> getLinks() {
		Collection<GraphVizLink> ret = new ArrayList<GraphVizLink>();
		
		for(ExpressionTree child : this.getChildren()) {
			ret.add(new GraphVizDefaultLink(this, child));
		}
		
		return ret;
	}
	
	@Override
	public boolean equivalent(ExpressionTree node) {
		return this.getExpression().compareTo(node.getExpression()) == 0;
	}

	@Override
	public String getShape() {
		switch(this.type) {
			case OPERATOR:
				return quotify(this.getChildren().isEmpty() ? "ellipse" : "ellipse");
			case LITERAL:
				return quotify("rectangle");
			case NOT:
				return quotify("ellipse");
		}
		return null;
	}

	@Override
	public String getLabel() {
		switch(this.type) {
			case OPERATOR:
				if(this.getChildren().isEmpty()) {
					if(this.getMode()) {
						return quotify("1"+ " " + this.getLiterals().toString());
					}
					else {
						return quotify("0"+ " " + this.getLiterals().toString());
					}
				}
				else {
					if(this.getMode()) {
						return quotify("&"+ " " + this.getLiterals().toString());
					}
					else {
						return quotify("|"+ " " + this.getLiterals().toString());
					}
				}
			case LITERAL:
				return quotify(this.getLiteral());
			case NOT:
				return quotify("!"+ " " + this.getLiterals().toString());
		}
		return null;
	}

	@Override
	public String getColor() {
		switch(this.type) {
			case OPERATOR:
				if(this.getChildren().isEmpty()) {
					if(this.getMode()) {
						return quotify("blue");
					}
					else {
						return quotify("red");
					}
				}
				else {
					if(this.getMode()) {
						return quotify("green");
					}
					else {
						return quotify("purple");
					}
				}
			case LITERAL:
				return quotify("black");
			case NOT:
				return quotify("orange");
		}
		return null;
	}

	@Override
	public ExpressionTree generateNot() {
		switch(this.getType()) {
			case NOT:
				return this.getChildren().first();
			
			case LITERAL:
				return new AbstractExpressionTree(
						this.getLiterals(),
						ExpressionTreeType.NOT,
						false,
						null,
						new TreeSet<ExpressionTree>()
				);
				
			case OPERATOR:
				SortedSet<ExpressionTree> newChildren = new TreeSet<ExpressionTree>();
				
				for(ExpressionTree child : this.getChildren()) {
					newChildren.add(
							new AbstractExpressionTree(
									child.getLiterals(),
									ExpressionTreeType.NOT,
									false,
									null,
									new TreeSet<ExpressionTree>()
							)
					); 
				}

				return new AbstractExpressionTree(
						this.getLiterals(),
						ExpressionTreeType.OPERATOR,
						!this.getMode(),
						null,
						newChildren
				);
		}
		return this;
	}

	@Override
	public ExpressionTree reduceBy(Map<String, Boolean> values) {
		ExpressionTree ret = null, child;
		List<String> newLiterals = new ArrayList<String>();
		Set<String> newUsedLiterals = new TreeSet<String>();

		LogicNodeSet newChildren = new LogicNodeSet();
		
		for(String lit : this.getLiterals()) {
			if(!values.containsKey(lit)) {
				newLiterals.add(lit);
			}
		}
		
		newReductions.putAll(values);
		newReductions.putAll(this.getAppliedReductions());
		
		switch(this.getType()) {
			case LITERAL:
				Boolean value = values.get(this.getName());
				if(value == null) {
					newUsedLiterals.add(this.getName());
					return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.LITERAL, false, this.getName(), newChildren);
				}
				else {
					return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, value, null, newChildren);
				}
			case NOT:
				child = (LogicNode) this.getChildren().first();
				
				switch(child.getType()) {
					case LITERAL:
						Boolean childValue = values.get(child.getName());
						if(childValue == null) {
							newUsedLiterals.add(child.getName());
							LogicNode lit = new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.LITERAL, false, child.getName(), newChildren);
							return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.NOT, false, null, new LogicNodeSet(lit));
						}
						else {
							return new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, !childValue, null, newChildren);
						}
					default:
						return child.not().reduceBy(values);
				}
			case OPERATOR:
				for(LogicNodeInterface c : this.getChildren()) {
					child = (LogicNode) c.reduceBy(values);
					
					newUsedLiterals.addAll(child.getUsedLiterals());
					
					switch(child.getType()) {
						case LITERAL:
						case NOT:
							newChildren.add(child);
							break;
						case OPERATOR:
							if(this.getMode() == child.getMode()) {
								for(LogicNodeInterface d : child.getChildren()) {
									newChildren.add(d);
								}
							}
							else if(child.isFinal()){
								return child;
							}
							else {
								newChildren.add(child);
							}
							break;
					}
				}

				return newChildren.size() == 1 ? (LogicNode) newChildren.first() : new LogicNode(newLiterals, newUsedLiterals, newReductions, LogicNodeType.OPERATOR, this.getMode(), null, newChildren);
		}
		return ret;
	}

	@Override
	public String getExpression() {
		String ret = "";
		switch(this.getType()) {
			case LITERAL: 
				ret += this.getLiteral();
				break;
				
			case NOT:
				ret += "!" + this.getChildren().first().getExpression();
				break;
				
			case OPERATOR:
				switch(this.getChildren().size()) {
					case 0:
						ret += this.getMode() ? "1" : "0";
						break;
					case 1:
						ret += this.getChildren().first().getExpression();
						break;
					default:
						boolean f = true;
						
						ret += this.getMode() ? "[" : "{";

						for(ExpressionTree child: this.getChildren()) {
							if(f) {
								f = false;
							}
							else {
								ret += ",";
							}
							ret += child.getExpression();
						}
						ret += this.getMode() ? "]" : "}";
				}
				break;
		}
		return ret;
	}
}
