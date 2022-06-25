package com.mhe.dev.logic.stack.core.compiler.model;

import java.util.List;
import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;

/**
 * Abstract Syntax Tree.
 *
 * @param <T> Semantic category
 * @author Manuel Hoyo Estévez
 */
public interface AbstractSyntaxTree<T> extends GraphVizNode {
    T getType();

    List<AbstractSyntaxTree<T>> getChildren();

    String toJson(List<String> literalsOrder);
}
