package mhe.compiler;

import java.util.HashMap;

public class SymbolMap<C> extends HashMap<String, SymbolInterface<C>> implements SymbolMapInterface<C> {
    private static final long serialVersionUID = -3464268263174234579L;

    @Override
    public SymbolInterface<C> getSymbolByName(String name) {
        return this.get(name);
    }

    @Override
    public String toString(){
        return this.values().toString();
    }
}
