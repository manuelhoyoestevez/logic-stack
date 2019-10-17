package mhe.compiler.logic;

import java.util.List;

import mhe.compiler.SymbolInterface;
import mhe.compiler.SymbolMapInterface;
import mhe.compiler.TokenInterface;
import mhe.compiler.exception.CompilerException;
import mhe.compiler.mhe.MheLexicalCategory;

public interface LogicSymbolMapInterface extends SymbolMapInterface<MheLexicalCategory> {

	public boolean processInteger(TokenInterface<MheLexicalCategory> t) throws CompilerException;

	public String processShow(TokenInterface<MheLexicalCategory> t) throws CompilerException;

	public SymbolInterface<MheLexicalCategory> processAssignement(TokenInterface<MheLexicalCategory> t) throws CompilerException;

	public SymbolInterface<MheLexicalCategory> processIdentifier(TokenInterface<MheLexicalCategory> t) throws CompilerException;

	public List<String> getLiterals();
}