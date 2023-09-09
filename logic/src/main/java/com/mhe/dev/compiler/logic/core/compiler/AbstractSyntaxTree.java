package com.mhe.dev.compiler.logic.core.compiler;

import java.util.List;

/**
 * Abstract Syntax Tree.
 *
 * @param <T> Semantic category
 * @author Manuel Hoyo Estévez
 */
public interface AbstractSyntaxTree<T>
{
    T getType();

    List<AbstractSyntaxTree<T>> getChildren();

    String toJson(List<String> literalsOrder);
}
