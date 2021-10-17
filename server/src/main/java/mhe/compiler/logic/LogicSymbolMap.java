package mhe.compiler.logic;

import java.util.List;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.Token;

public interface LogicSymbolMap {
    boolean processInteger(Token<MheLexicalCategory> token) throws CompilerException;
    String processShow(Token<MheLexicalCategory> token) throws CompilerException;
    Symbol<MheLexicalCategory, LogicSemanticCategory> processAssignment(Token<MheLexicalCategory> token) throws CompilerException;
    Symbol<MheLexicalCategory, LogicSemanticCategory> processIdentifier(Token<MheLexicalCategory> token) throws CompilerException;
    List<String> getLiterals();
}
