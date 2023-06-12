package com.mhe.dev.compiler.logic.core.compiler.logger;

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
