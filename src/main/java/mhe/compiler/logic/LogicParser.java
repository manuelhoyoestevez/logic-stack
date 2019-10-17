package mhe.compiler.logic;

import mhe.compiler.ASTInterface;
import mhe.compiler.LexerInterface;
import mhe.compiler.LoggerInterface;
import mhe.compiler.SymbolInterface;
import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logic.ast.AST;
import mhe.compiler.logic.ast.ASTa;
import mhe.compiler.logic.ast.ASTasig;
import mhe.compiler.logic.ast.ASTc;
import mhe.compiler.logic.ast.ASTe;
import mhe.compiler.logic.ast.ASTerror;
import mhe.compiler.logic.ast.ASTexit;
import mhe.compiler.logic.ast.ASTid;
import mhe.compiler.logic.ast.ASTn;
import mhe.compiler.logic.ast.ASTo;
import mhe.compiler.logic.ast.ASTp;
import mhe.compiler.logic.ast.ASTreturn;
import mhe.compiler.logic.ast.ASTsave;
import mhe.compiler.logic.ast.ASTshow;
import mhe.compiler.mhe.MheLexicalCategory;

public class LogicParser implements LogicASTConstants {

    private LexerInterface<MheLexicalCategory> lexer;
    public LogicSymbolMapInterface lset;

    public LexerInterface<MheLexicalCategory> getLexer() {
        return this.lexer;
    }

    private LoggerInterface getLogger() {
        return this.getLexer().getLogger();
    }

    public static String formatSymbolList(String[] list) {
        String ret = "";
        for(String symbol : list) {
            ret += "'" + symbol + "', ";
        }
        return ret + "";
    }

    public LogicParser(LexerInterface<MheLexicalCategory> lexer, LogicSymbolMapInterface lset) {
        this.lexer = lexer;
        this.lset = lset;
    }

    public LogicParser(LexerInterface<MheLexicalCategory> lexer) {
        this(lexer, new LogicSymbolMap(lexer.getLogger()));
    }

    public LogicSymbolMapInterface getLogicSymbolMap() {
        return this.lset;
    }

    protected String getLexeme() {
        return this.getLexer().getStream().getLexeme();
    }

    public ASTInterface Compile() throws CompilerException {
        //this.currenttokencat = this.getLexer().getNextTokenCategory();

        this.getLexer().getNextTokenCategory();
        return this.CompileP();
    }

    protected ASTInterface CompileP() throws CompilerException {
        ASTInterface r;
        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "+ CompileP(): "
        );

        switch(this.getLexer().getCurrentTokenCategory()){
            case IDENTIFIER:
            case EXIT:
            case SAVE:
            case SHOW:
            case RETURN:
                ASTInterface s = this.CompileS();
                this.getLexer().matchToken(MheLexicalCategory.SEMICOLON);
                r = new ASTp(s, this.CompileP());
                break;
            case END:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"identificador", "show", "save", "exit", "$end"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileP: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + this.getLexer().getCurrentToken()
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "- CompileP: "
        )
        .decTabLevel();
        return r;
    }

    protected ASTInterface CompileS() throws CompilerException {
        String id;
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "+ CompileS(): "
        );

        switch(this.getLexer().getCurrentTokenCategory()){
            case RETURN:
                this.getLexer().matchToken(MheLexicalCategory.RETURN);
                r = new ASTreturn(this.CompileE());
                break;
            case SHOW:
                this.getLexer().matchToken(MheLexicalCategory.SHOW);
                id = this.lset.processShow(this.getLexer().getCurrentToken());
                this.getLexer().matchToken(MheLexicalCategory.IDENTIFIER);
                r = new ASTshow(id);
                break;
            case EXIT:
                this.getLexer().matchToken(MheLexicalCategory.EXIT);
                r = new ASTexit();
                break;
            case SAVE:
                this.getLexer().matchToken(MheLexicalCategory.SAVE);
                // Comprobaciones de fichero
                id = this.getLexeme();
                this.getLexer().matchToken(MheLexicalCategory.STRING);
                r = new ASTsave(id);
                break;
            case IDENTIFIER:
                id = this.getLexeme();
                SymbolInterface<MheLexicalCategory> s = this.lset.processAssignement(this.getLexer().getCurrentToken());
                this.getLexer().matchToken(MheLexicalCategory.IDENTIFIER);
                this.getLexer().matchToken(MheLexicalCategory.EQUAL);
                ASTInterface e = this.CompileE();
                s.setAST(e);
                r = new ASTasig(id,e);
                break;
            default:
                String[] y = {"identificador", "show", "save", "exit"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileS: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + this.getLexer().getCurrentToken()
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "- CompileS(): "
        )
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileE() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "+ CompileE(): "
        );

        switch(this.getLexer().getCurrentTokenCategory()){
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new ASTe(CompileC(),CompileE0());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileE: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + this.getLexer().getCurrentToken()
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "- CompileE(): "
        )
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileE0() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "+ CompileE0(): "
        );

        switch(this.getLexer().getCurrentTokenCategory()){
            case IMPLDOUBLE:
                this.getLexer().matchToken(MheLexicalCategory.IMPLDOUBLE);
            //    r = this.CompileE();
                r = new ASTe(CompileC(),CompileE0());
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"<>", ")", ";", "]", "}", "," };
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileE0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + this.getLexer().getCurrentToken()
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(),
                "- CompileE0(): "
        )
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileC() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileC(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new ASTc(this.CompileA(),this.CompileC0());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileC: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileC(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileC0() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileC0(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case IMPLRIGHT:
                this.getLexer().matchToken(MheLexicalCategory.IMPLRIGHT);
            //    r = this.CompileC();
                r = new ASTc(this.CompileA(),this.CompileC0());
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case IMPLDOUBLE:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"->", "<>", ")", ";", "]", "}", "," };
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileC0: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileC0(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileA() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileA(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new ASTa(this.CompileO(),this.CompileA0());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileA: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileA(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileA0() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileA0(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case AMPERSAND:
                this.getLexer().matchToken(MheLexicalCategory.AMPERSAND);
                r = CompileA();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case IMPLDOUBLE:
            case IMPLRIGHT:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"&", "->", "<>", ")", ";", "]", "}", "," };
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileA0: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileA0(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileO() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileO(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new ASTo(this.CompileN(),this.CompileO0());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileO: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileO(): ")
        .decTabLevel();

        return r;
    }


    protected ASTInterface CompileO0() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileO0(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case BAR:
                this.getLexer().matchToken(MheLexicalCategory.BAR);
                r = CompileO();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case IMPLDOUBLE:
            case IMPLRIGHT:
            case AMPERSAND:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"|", "&", "->", "<>", ")", ";", "]", "}", "," };
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileO0: Se esperaba " + y.toString() + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileO0(): ")
        .decTabLevel();

        return r;
    }


    protected ASTInterface CompileN() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileN(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
                r = this.CompileL();
                break;
            case NOT:
                this.getLexer().matchToken(MheLexicalCategory.NOT);
                r = new ASTn(this.CompileN());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileN: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- Compilen(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileL() throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileL(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case INTEGER:
                r = AST.constant(this.lset.processInteger(this.getLexer().getCurrentToken()));
                this.getLexer().matchToken(MheLexicalCategory.INTEGER);
                break;
            case IDENTIFIER:
                SymbolInterface<MheLexicalCategory> s = this.lset.processIdentifier(this.getLexer().getCurrentToken());

                r = s.getAST();

                if(r == null){
                    r = new ASTid(this.getLexeme());
                    s.setAST(r);
                }
                /*
                else if(!s.isLiteral()){
                    r = new ASTvar(s.getName());
                }
                */
                this.getLexer().matchToken(MheLexicalCategory.IDENTIFIER);
                break;
            case LPAREN:
                this.getLexer().matchToken(MheLexicalCategory.LPAREN);
                r = this.CompileE();
                this.getLexer().matchToken(MheLexicalCategory.RPAREN);
                break;
            case LKEY:
                this.getLexer().matchToken(MheLexicalCategory.LKEY);
                r = this.CompileX(true);
                this.getLexer().matchToken(MheLexicalCategory.RKEY);
                break;
            case LCORCH:
                this.getLexer().matchToken(MheLexicalCategory.LCORCH);
                r = this.CompileX(false);
                this.getLexer().matchToken(MheLexicalCategory.RCORCH);
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(),
                        "CompileL: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileL(): ")
        .decTabLevel();

        return r;
    }

    protected ASTInterface CompileX(boolean x) throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileX(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case LCORCH:
            case LKEY:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = x
                    ? new ASTo(this.CompileE(),this.CompileX0(x))
                    : new ASTa(this.CompileE(),this.CompileX0(x));
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileX: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileX(): ")
        .decTabLevel();

        return r;
    }


    protected ASTInterface CompileX0(boolean x) throws CompilerException {
        ASTInterface r;

        this
        .getLogger()
        .incTabLevel()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "+ CompileX0(): ");

        switch(this.getLexer().getCurrentTokenCategory()){
            case COLON:
                this.getLexer().matchToken(MheLexicalCategory.COLON);
                r = CompileX(x);
                break;
            case RKEY:
            case RCORCH:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = { "]", "}", "," };
                this.getLogger().logError(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                        this.getLexer().getCurrentToken().getCol(), "CompileX0: Se esperaba " + formatSymbolList(y) + " en lugar de " + this.getLexer().getCurrentToken());
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(LogType.SYNTACTIC, this.getLexer().getCurrentToken().getRow(),
                this.getLexer().getCurrentToken().getCol(), "- CompileX0(): ")
        .decTabLevel();

        return r;
    }
}
