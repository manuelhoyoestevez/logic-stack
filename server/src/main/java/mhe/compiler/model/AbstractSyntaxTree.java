package mhe.compiler.model;

import java.util.List;
import mhe.graphviz.GraphVizNode;

/**
 * Abstract Syntax Tree.
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <T> Semantic category
 */
public interface AbstractSyntaxTree<T> extends GraphVizNode {
    T getType();
    List<AbstractSyntaxTree<T>> getChildren();
    String toJson(List<String> literalsOrder);
}
