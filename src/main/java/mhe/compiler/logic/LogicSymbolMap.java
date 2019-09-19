package mhe.compiler.logic;

import java.util.*;

import mhe.compiler.*;
import mhe.compiler.logger.LogType;

@SuppressWarnings("serial")
public class LogicSymbolMap extends SymbolMap implements LogicSymbolMapInterface, Loggable {
	private LoggerInterface logger;
	
	public LogicSymbolMap(LoggerInterface l){
		this.logger = l;
	}
	
	@Override
	public LoggerInterface getLogger() {
		return this.logger;
	}
	
	@Override
	public String processShow(TokenInterface t) throws Exception {
		String s = t.getLexeme();
		SymbolInterface r = getSymbolByName(s);
		if(r == null) {
			this.getLogger().logError(LogType.SEMANTIC, t, "processShow(): no existe identificador");
		}
		return s;
	}

	@Override
	public SymbolInterface processAssignement(TokenInterface t) throws Exception {
		String s = t.getLexeme();
		SymbolInterface r = getSymbolByName(s);
		
		if(r != null && r.getType().compareToIgnoreCase("literal") == 0) {
			this.getLogger().logError(LogType.SEMANTIC, t, "processAssignement(): el identificador es un literal y no se le puede asignar una expresion");
		}
		else{
			r = new Symbol(t.getLexeme(), "variable", null);
			this.put(t.getLexeme(), r);
		}
		r.addToken(t);
		return r;
	}
	

	@Override
	public SymbolInterface processIdentifier(TokenInterface t) throws Exception {
		SymbolInterface r = getSymbolByName(t.getLexeme());
		if(r == null){
			r = new Symbol(t.getLexeme(), "literal", null);
			this.put(t.getLexeme(), r);
		}
		else if(r.getAST() == null){
			this.getLogger().logError(LogType.SEMANTIC, t, "processIdentifier(): el identificador no esta instanciado");
		}
		r.addToken(t);
		return r;
	}
	

	@Override
	public boolean processInteger(TokenInterface t) throws Exception{
		int i = Integer.parseInt(t.getLexeme());				
		if(i != 0 && i != 1) {
			this.getLogger().logError(LogType.SEMANTIC, t, "processInteger(): solo se admite 0 ó 1");
		}
		return i == 1;
	}
	
	public List<String> getLiterals(){
		List<String> ret = new ArrayList<String>();
		
		for(SymbolInterface symbol : this.values()) {
			if(symbol.getType().compareToIgnoreCase("literal") == 0) {
				ret.add(symbol.getName());
			}
		}
		
		return ret;
	}
}
