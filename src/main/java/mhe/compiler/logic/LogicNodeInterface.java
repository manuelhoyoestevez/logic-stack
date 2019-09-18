package mhe.compiler.logic;

import java.util.*;

import mhe.graphviz.GraphVizNode;

public interface LogicNodeInterface extends LogicFunctionInterface, GraphVizNode {

	public LogicNodeType getType();

	public String getName();

	public boolean getMode();

	public SortedSet<LogicNodeInterface> getChildren();

	public boolean addChild(LogicNodeInterface child);

	public boolean isFinal();

	public boolean equivalent(LogicNodeInterface n);
}
