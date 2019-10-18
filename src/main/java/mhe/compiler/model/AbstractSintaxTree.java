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
public interface AbstractSintaxTree<T> extends GraphVizNode {
    boolean isLambda();

    T getType();

    String getName();

    List<AbstractSintaxTree<T>> getChildren();

    AbstractSintaxTree<T> getFirstChild();

    AbstractSintaxTree<T> getSecondChild();

    String toJson(List<String> literalsOrder);
}
