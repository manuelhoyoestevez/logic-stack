package mhe.compiler.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.Loggable;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.SymbolType;
import mhe.compiler.model.Token;
import mhe.compiler.model.impl.AbstractSymbol;


public class LogicSymbolHashMap implements LogicSymbolMap, Loggable {
    private Logger logger;
    private Map<String, Symbol<MheLexicalCategory, LogicSemanticCategory>> map;

    public LogicSymbolHashMap(Logger logger){
        this.logger = logger;
        this.map = new HashMap<String, Symbol<MheLexicalCategory, LogicSemanticCategory>>();
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public Symbol<MheLexicalCategory, LogicSemanticCategory> getSymbolByName(String symbolName) {
        return this.map.get(symbolName);
    }

    @Override
    public String processShow(Token<MheLexicalCategory> t) throws CompilerException {
        String s = t.getLexeme();
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(s);
        if(r == null) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processShow(): no existe identificador '"  + s + "'");
        }
        return s;
    }

    @Override
    public Symbol<MheLexicalCategory, LogicSemanticCategory> processAssignement(Token<MheLexicalCategory> t) throws CompilerException {
        String s = t.getLexeme();
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(s);

        if(r != null && r.getType() == SymbolType.LITERAL) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processAssignement(): el identificador '" + s + "' es un literal y no se le puede asignar una expresion");
        }
        else{
            r = new AbstractSymbol<MheLexicalCategory, LogicSemanticCategory>(t.getLexeme(), SymbolType.VARIABLE, null);
            this.map.put(t.getLexeme(), r);
        }
        r.addToken(t);
        return r;
    }


    @Override
    public Symbol<MheLexicalCategory, LogicSemanticCategory> processIdentifier(Token<MheLexicalCategory> t) throws CompilerException {
        Symbol<MheLexicalCategory, LogicSemanticCategory> r = this.map.get(t.getLexeme());
        if(r == null){
            r = new AbstractSymbol<MheLexicalCategory, LogicSemanticCategory>(t.getLexeme(), SymbolType.LITERAL, null);
            this.map.put(t.getLexeme(), r);
        }
        else if(r.getAST() == null){
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processIdentifier(): el identificador '" + t.getLexeme() + "' no esta instanciado");
        }
        r.addToken(t);
        return r;
    }


    @Override
    public boolean processInteger(Token<MheLexicalCategory> t) throws CompilerException {
        int i = Integer.parseInt(t.getLexeme());
        if(i != 0 && i != 1) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processInteger(): Entero no válido: '" + i + "'. Sólo se admite 0 ó 1");
        }
        return i == 1;
    }

    @Override
    public List<String> getLiterals(){
        List<String> ret = new ArrayList<String>();

        for(Symbol<MheLexicalCategory, LogicSemanticCategory> symbol : this.map.values()) {
            if(symbol.getType() == SymbolType.LITERAL) {
                ret.add(symbol.getName());
            }
        }

        return ret;
    }
}
