package mhe.compiler.model;

import java.util.Collection;

/**
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <C> Caregorías lexicas
 * @param <T> Categortías semánticas
 */
public interface Symbol<C, T> extends Comparable<Symbol<C, T>> {
    String getName();
    SymbolType getType();

    AbstractSintaxTree<T> getAST();
    Symbol<C, T> setAST(AbstractSintaxTree<T> ast);

    Collection<Token<C>> getTokens();
    Symbol<C, T> addToken(Token<C> token);
}

