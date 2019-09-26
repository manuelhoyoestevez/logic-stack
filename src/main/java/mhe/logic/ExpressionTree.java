package mhe.logic;

import java.util.Set;
import mhe.graphviz.GraphVizNode;

public interface ExpressionTree extends LogicFunction, GraphVizNode {
	public String getExpression();
	
	public ExpressionTree copy();

	public ExpressionTree generateNot();

	public ExpressionTreeType getType();

	public boolean getMode();

	public Set<ExpressionTree> getChildren();

	public boolean isFinal();
	
	/**
	 * @return Literal en la hoja o null
	 */
	public String getLiteral();

	public boolean equivalent(ExpressionTree expressionTree);
}
