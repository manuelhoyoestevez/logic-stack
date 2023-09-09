package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.Token;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AbstractSymbol.
 *
 * @param <C> Categorías léxicas
 * @param <T> Categorías semánticas
 * @author Manuel Hoyo Estévez
 */
public class AbstractSymbol<C, T> implements Symbol<C, T>
{
    private final String name;

    private final SymbolType type;
    private final Collection<Token<C>> tokens;
    private AbstractSyntaxTree<T> ast;

    /**
     * Constructor.
     *
     * @param name Name
     * @param type Type
     * @param ast  Abstract Syntax Tree
     */
    public AbstractSymbol(String name, SymbolType type, AbstractSyntaxTree<T> ast)
    {
        this.name = name;
        this.type = type;
        this.setAst(ast);
        this.tokens = new ArrayList<>();
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Boolean isLiteral()
    {
        return type == SymbolType.LITERAL;
    }

    @Override
    public AbstractSyntaxTree<T> getAst()
    {
        return this.ast;
    }

    @Override
    public Symbol<C, T> setAst(AbstractSyntaxTree<T> ast)
    {
        this.ast = ast;
        return this;
    }

    @Override
    public int compareTo(Symbol<C, T> other)
    {
        int ret = this.getName().compareTo(other.getName());

        if (ret != 0)
        {
            return ret;
        }

        return this.hashCode() - other.hashCode();
    }

    @Override
    public String toString()
    {
        return name + ": " + type;
    }
}
