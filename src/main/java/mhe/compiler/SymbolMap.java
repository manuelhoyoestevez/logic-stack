package mhe.compiler;

import java.util.HashMap;

@SuppressWarnings("serial")
public class SymbolMap extends HashMap<String, SymbolInterface> implements SymbolMapInterface {

	@Override
	public SymbolInterface getSymbolByName(String name) {
		return this.get(name);
	}
	
	@Override
	public String toString(){
		return this.values().toString();
	}
}
