package com.mhe.dev.logic.stack.core.compiler.model;

/**
 * LambdaAbstractSyntaxTree.
 *
 * @param <T> Semantic category
 * @author Manuel Hoyo Estévez
 */
public interface LambdaAbstractSyntaxTree<T> extends AbstractSyntaxTree<T>
{
    boolean isNotLambda();
}
