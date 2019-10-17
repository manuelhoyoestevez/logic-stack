package mhe.compiler;

import java.util.Map;

public interface SymbolMapInterface<C> extends Map<String, SymbolInterface<C>> {
    SymbolInterface<C> getSymbolByName(String name);
}
