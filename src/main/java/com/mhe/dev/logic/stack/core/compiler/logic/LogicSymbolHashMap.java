package com.mhe.dev.logic.stack.core.compiler.logic;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.compiler.logger.MheLogger;
import com.mhe.dev.logic.stack.core.compiler.logger.MheLoggerFactory;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexicalCategory;
import com.mhe.dev.logic.stack.core.compiler.model.Symbol;
import com.mhe.dev.logic.stack.core.compiler.model.SymbolType;
import com.mhe.dev.logic.stack.core.compiler.model.Token;
import com.mhe.dev.logic.stack.core.compiler.model.impl.AbstractSymbol;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LogicSymbolMap implementation.
 */
public class LogicSymbolHashMap implements LogicSymbolMap
{
    private static final MheLogger logger = MheLoggerFactory.getLogger(LogicSymbolHashMap.class);

    private final Map<String, Symbol<MheLexicalCategory, LogicSemanticCategory>> map;

    public LogicSymbolHashMap()
    {
        this.map = new HashMap<>();
    }

    @Override
    public String processShow(Token<MheLexicalCategory> t) throws CompilerException
    {
        String s = t.getLexeme();
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(s);
        if (r == null)
        {
            String message = "Semantic error: processShow(): no existe identificador '" + s + "'";
            logger.error(t.getRow(), t.getCol(), message);
            throw new CompilerException(t.getRow(), t.getCol(), message, null);
        }
        return s;
    }

    @Override
    public Symbol<MheLexicalCategory, LogicSemanticCategory> processAssignment(Token<MheLexicalCategory> t)
        throws CompilerException
    {
        String s = t.getLexeme();
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(s);

        if (r != null && r.isLiteral())
        {
            String message = "Semantic error: processAssignment(): el identificador '"
                + s + "' es un literal y no se le puede asignar una expresión";
            logger.error(t.getRow(), t.getCol(), message);
            throw new CompilerException(t.getRow(), t.getCol(), message, null);
        }

        r = new AbstractSymbol<>(t.getLexeme(), SymbolType.VARIABLE, null);
        this.map.put(t.getLexeme(), r);

        return r;
    }

    @Override
    public Symbol<MheLexicalCategory, LogicSemanticCategory> processIdentifier(Token<MheLexicalCategory> t)
        throws CompilerException
    {
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(t.getLexeme());
        if (r == null)
        {
            r = new AbstractSymbol<>(t.getLexeme(), SymbolType.LITERAL, null);
            this.map.put(t.getLexeme(), r);
        } else if (r.getAst() == null)
        {
            String message = "processIdentifier(): el identificador '"
                + t.getLexeme() + "' no esta instanciado";
            logger.error(t.getRow(), t.getCol(), message);
            throw new CompilerException(t.getRow(), t.getCol(), message, null);
        }
        return r;
    }

    @Override
    public boolean processInteger(Token<MheLexicalCategory> t) throws CompilerException
    {
        int i = Integer.parseInt(t.getLexeme());
        if (i != 0 && i != 1)
        {
            String message = "processInteger(): Entero no válido: '" + i + "'. Sólo se admite 0 ó 1";
            logger.error(t.getRow(), t.getCol(), message);
            throw new CompilerException(t.getRow(), t.getCol(), message, null);
        }
        return i == 1;
    }

    @Override
    public List<String> getLiterals()
    {
        return this.map
            .values()
            .stream()
            .filter(Symbol::isLiteral)
            .map(Symbol::getName)
            .collect(Collectors.toList());
    }
}
