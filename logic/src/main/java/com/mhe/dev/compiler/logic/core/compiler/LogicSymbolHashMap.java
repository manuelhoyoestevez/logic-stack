package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.Token;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LogicSymbolMap implementation.
 */
public class LogicSymbolHashMap implements LogicSymbolMap
{
    private final MheLogger logger;
    private final Map<String, Symbol<MheLexicalCategory, LogicSemanticCategory>> map;

    public LogicSymbolHashMap(MheLogger logger)
    {
        this.logger = logger;
        this.map = new HashMap<>();
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
