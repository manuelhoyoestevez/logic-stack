package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.Token;
import java.util.List;

/**
 * LogicSymbolMap.
 */
public interface LogicSymbolMap
{
    boolean processInteger(Token<MheLexicalCategory> token) throws CompilerException;

    Symbol<MheLexicalCategory, LogicSemanticCategory> processAssignment(Token<MheLexicalCategory> token)
        throws CompilerException;

    Symbol<MheLexicalCategory, LogicSemanticCategory> processIdentifier(Token<MheLexicalCategory> token)
        throws CompilerException;

    List<String> getLiterals();
}
