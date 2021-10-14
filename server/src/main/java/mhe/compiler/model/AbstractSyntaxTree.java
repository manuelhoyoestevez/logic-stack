package mhe.compiler.model;

import java.util.List;

import mhe.graphviz.GraphVizNode;

/**
 * Abstract Syntax Tree
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <T> Semantic category
 */
public interface AbstractSyntaxTree<T> extends GraphVizNode {
    boolean isNotLambda();
    T getType();
    String getName();
    List<AbstractSyntaxTree<T>> getChildren();
    AbstractSyntaxTree<T> getFirstChild();
    AbstractSyntaxTree<T> getSecondChild();
    String toJson(List<String> literalsOrder);
}
