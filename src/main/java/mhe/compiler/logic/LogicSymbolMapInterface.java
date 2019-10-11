package mhe.compiler.logic;

import java.util.List;

import mhe.compiler.SymbolInterface;
import mhe.compiler.SymbolMapInterface;
import mhe.compiler.TokenInterface;
import mhe.compiler.exception.CompilerException;

public interface LogicSymbolMapInterface extends SymbolMapInterface {

	public boolean processInteger(TokenInterface t) throws CompilerException;

	public String processShow(TokenInterface t) throws CompilerException;

	public SymbolInterface processAssignement(TokenInterface t) throws CompilerException;

	public SymbolInterface processIdentifier(TokenInterface t) throws CompilerException;

	public List<String> getLiterals();
}