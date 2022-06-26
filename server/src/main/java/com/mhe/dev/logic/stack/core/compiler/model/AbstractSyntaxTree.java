package com.mhe.dev.logic.stack.core.compiler.model;

import com.mhe.dev.logic.stack.core.graphviz.GraphVizNode;
import java.util.List;

/**
 * Abstract Syntax Tree.
 *
 * @param <T> Semantic category
 * @author Manuel Hoyo Estévez
 */
public interface AbstractSyntaxTree<T> extends GraphVizNode
{
    T getType();

    List<AbstractSyntaxTree<T>> getChildren();

    String toJson(List<String> literalsOrder);
}
