package mhe.compiler.logic;

import java.util.*;

import mhe.compiler.*;

public interface LogicSymbolMapInterface extends SymbolMapInterface {

	public boolean processInteger(TokenInterface t) throws Exception;
	
	public String processShow(TokenInterface t) throws Exception;

	public SymbolInterface processAssignement(TokenInterface t) throws Exception;
	
	public SymbolInterface processIdentifier(TokenInterface t) throws Exception;
	
	public List<String> getLiterals();
}