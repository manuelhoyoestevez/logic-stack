package mhe.compiler;

import java.util.Map;

public interface SymbolMapInterface extends Map<String, SymbolInterface> {

	SymbolInterface getSymbolByName(String name);
}
