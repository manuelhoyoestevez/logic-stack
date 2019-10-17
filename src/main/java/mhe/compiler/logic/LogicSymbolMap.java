package mhe.compiler.logic;

import java.util.ArrayList;
import java.util.List;

import mhe.compiler.Loggable;
import mhe.compiler.LoggerInterface;
import mhe.compiler.Symbol;
import mhe.compiler.SymbolInterface;
import mhe.compiler.SymbolMap;
import mhe.compiler.SymbolType;
import mhe.compiler.TokenInterface;
import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.mhe.MheLexicalCategory;


public class LogicSymbolMap extends SymbolMap<MheLexicalCategory> implements LogicSymbolMapInterface, Loggable {
    private static final long serialVersionUID = -3779063876048548031L;
    private LoggerInterface logger;

    public LogicSymbolMap(LoggerInterface l){
        this.logger = l;
    }

    @Override
    public LoggerInterface getLogger() {
        return this.logger;
    }

    @Override
    public String processShow(TokenInterface<MheLexicalCategory> t) throws CompilerException {
        String s = t.getLexeme();
        SymbolInterface<MheLexicalCategory> r = getSymbolByName(s);
        if(r == null) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processShow(): no existe identificador");
        }
        return s;
    }

    @Override
    public SymbolInterface<MheLexicalCategory> processAssignement(TokenInterface<MheLexicalCategory> t) throws CompilerException {
        String s = t.getLexeme();
        SymbolInterface<MheLexicalCategory> r = getSymbolByName(s);

        if(r != null && r.getType() == SymbolType.LITERAL) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processAssignement(): el identificador es un literal y no se le puede asignar una expresion");
        }
        else{
            r = new Symbol<MheLexicalCategory>(t.getLexeme(), SymbolType.VARIABLE, null);
            this.put(t.getLexeme(), r);
        }
        r.addToken(t);
        return r;
    }


    @Override
    public SymbolInterface<MheLexicalCategory> processIdentifier(TokenInterface<MheLexicalCategory> t) throws CompilerException {
        SymbolInterface<MheLexicalCategory> r = getSymbolByName(t.getLexeme());
        if(r == null){
            r = new Symbol<MheLexicalCategory>(t.getLexeme(), SymbolType.LITERAL, null);
            this.put(t.getLexeme(), r);
        }
        else if(r.getAST() == null){
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processIdentifier(): el identificador no esta instanciado");
        }
        r.addToken(t);
        return r;
    }


    @Override
    public boolean processInteger(TokenInterface<MheLexicalCategory> t) throws CompilerException {
        int i = Integer.parseInt(t.getLexeme());
        if(i != 0 && i != 1) {
            this.getLogger().logError(LogType.SEMANTIC, t.getRow(), t.getCol(), "processInteger(): solo se admite 0 ó 1");
        }
        return i == 1;
    }

    @Override
    public List<String> getLiterals(){
        List<String> ret = new ArrayList<String>();

        for(SymbolInterface<MheLexicalCategory> symbol : this.values()) {
            if(symbol.getType() == SymbolType.LITERAL) {
                ret.add(symbol.getName());
            }
        }

        return ret;
    }
}
