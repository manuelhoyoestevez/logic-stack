package mhe.compiler.model;

/**
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <C> Categorías léxicas
 * @param <T> Categorías semánticas
 */
public interface Symbol<C, T> extends Comparable<Symbol<C, T>> {
    String getName();
    SymbolType getType();
    AbstractSyntaxTree<T> getAST();
    Symbol<C, T> setAST(AbstractSyntaxTree<T> ast);
    Symbol<C, T> addToken(Token<C> token);
}
