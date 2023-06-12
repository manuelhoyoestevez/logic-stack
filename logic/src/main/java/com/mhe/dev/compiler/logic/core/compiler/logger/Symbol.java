package com.mhe.dev.compiler.logic.core.compiler.logger;

/**
 * Symbol.
 *
 * @param <C> Categorías léxicas
 * @param <T> Categorías semánticas
 * @author Manuel Hoyo Estévez
 */
public interface Symbol<C, T> extends Comparable<Symbol<C, T>>
{
    String getName();

    Boolean isLiteral();

    AbstractSyntaxTree<T> getAst();

    Symbol<C, T> setAst(AbstractSyntaxTree<T> ast);
}
