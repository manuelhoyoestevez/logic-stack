package com.mhe.dev.logic.stack.core.compiler.logic;

import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.compiler.mhe.MheLexicalCategory;
import com.mhe.dev.logic.stack.core.compiler.model.Symbol;
import com.mhe.dev.logic.stack.core.compiler.model.Token;

/**
 * LogicSymbolMap.
 */
public interface LogicSymbolMap {
    boolean processInteger(Token<MheLexicalCategory> token) throws CompilerException;

    String processShow(Token<MheLexicalCategory> token) throws CompilerException;

    Symbol<MheLexicalCategory, LogicSemanticCategory> processAssignment(Token<MheLexicalCategory> token)
            throws CompilerException;

    Symbol<MheLexicalCategory, LogicSemanticCategory> processIdentifier(Token<MheLexicalCategory> token)
            throws CompilerException;

    List<String> getLiterals();
}
