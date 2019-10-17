package mhe.compiler;

import java.util.LinkedList;
import java.util.List;

import mhe.graphviz.GraphVizNode;

public interface ASTInterface extends GraphVizNode {
    public boolean isLambda();

    public int getType();

    public String getName();

    public LinkedList<ASTInterface> getChildren();

    public ASTInterface getFirstChild();

    public ASTInterface getSecondChild();

    public String toJson(List<String> literalsOrder);
}
