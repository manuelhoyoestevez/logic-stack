package mhe.compiler;

import java.util.LinkedList;

import io.vertx.core.json.JsonObject;
import mhe.graphviz.GraphVizNode;

public interface ASTInterface extends GraphVizNode {
	public boolean isLambda();

	public int getType();

	public String getName();

	public LinkedList<ASTInterface> getChildren();

	//public LogicNodeInterface getLogicNode();

	public ASTInterface getFirstChild();

	public ASTInterface getSecondChild();

	public JsonObject toJson();
}
