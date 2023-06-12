package com.mhe.dev.logic.stack.core.compiler.model.impl;

import com.mhe.dev.logic.stack.core.compiler.model.Token;

/**
 * AbstractToken: Implementación de la estructura de datos que almacena la información de un token.
 *
 * @author Manuel Hoyo Estévez
 */
public class AbstractToken<C> implements Token<C>
{

    /**
     * Categoría léxica.
     */
    protected C cat;
    /**
     * Número de fila.
     */
    protected int row;
    /**
     * Número de columna.
     */
    protected int col;
    /**
     * Lexema.
     */
    protected String lex;

    /**
     * Constructor.
     *
     * @param t Categoría léxica
     * @param l Lexema
     * @param r Número de fila
     * @param c Número de columna
     */
    public AbstractToken(C t, String l, int r, int c)
    {
        cat = t;
        lex = l;
        col = c;
        row = r;
    }

    @Override
    public C getCategory()
    {
        return cat;
    }

    @Override
    public int getRow()
    {
        return row;
    }

    @Override
    public int getCol()
    {
        return col;
    }

    @Override
    public String getLexeme()
    {
        return lex;
    }

    @Override
    public String toString()
    {
        return "{ " + cat + ": '" + lex + "' }[" + row + ", " + col + "]";
    }
}
