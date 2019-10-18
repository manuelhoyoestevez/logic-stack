package mhe.compiler.logic;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
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
import mhe.compiler.model.AbstractSintaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.Token;

public class LogicParser {

    private Lexer<MheLexicalCategory> lexer;
    private LogicSymbolMap lset;

    public Lexer<MheLexicalCategory> getLexer() {
        return this.lexer;
    }

    private Logger getLogger() {
        return this.getLexer().getLogger();
    }

    public static String formatSymbolList(String[] list) {
        String ret = "";
        for(String symbol : list) {
            ret += "'" + symbol + "', ";
        }
        return ret + "";
    }

    public LogicParser(Lexer<MheLexicalCategory> lexer, LogicSymbolMap lset) {
        this.lexer = lexer;
        this.lset = lset;
    }

    public LogicParser(Lexer<MheLexicalCategory> lexer) {
        this(lexer, new LogicSymbolHashMap(lexer.getLogger()));
    }

    public LogicSymbolMap getLogicSymbolMap() {
        return this.lset;
    }

    protected String getLexeme() {
        return this.getLexer().getStream().getLexeme();
    }

    public AbstractSintaxTree<LogicSemanticCategory> Compile() throws CompilerException {
        //this.currenttokencat = this.getLexer().getNextTokenCategory();

        this.getLexer().getNextTokenCategory();
        return this.CompileP();
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileP() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileP(): "
        );

        switch(currentToken.getCategory()){
            case IDENTIFIER:
            case EXIT:
            case SAVE:
            case SHOW:
            case RETURN:
                AbstractSintaxTree<LogicSemanticCategory> s = this.CompileS();
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
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileP: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileP: "
        )
        .decTabLevel();
        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileS() throws CompilerException {
        String id;
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileS(): "
        );

        switch(currentToken.getCategory()){
            case RETURN:
                this.getLexer().matchToken(MheLexicalCategory.RETURN);
                r = new ASTreturn(this.CompileE());
                break;
            case SHOW:
                this.getLexer().matchToken(MheLexicalCategory.SHOW);
                id = this.lset.processShow(currentToken);
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
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = this.lset.processAssignement(currentToken);
                this.getLexer().matchToken(MheLexicalCategory.IDENTIFIER);
                this.getLexer().matchToken(MheLexicalCategory.EQUAL);
                AbstractSintaxTree<LogicSemanticCategory> e = this.CompileE();
                s.setAST(e);
                r = new ASTasig(id,e);
                break;
            default:
                String[] y = {"identificador", "show", "save", "exit"};
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileS: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileS(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileE() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileE(): "
        );

        switch(currentToken.getCategory()){
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
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileE: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileE(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileE0() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileE0(): "
        );

        switch(currentToken.getCategory()){
            case IMPLDOUBLE:
                this.getLexer().matchToken(MheLexicalCategory.IMPLDOUBLE);
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
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileE0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileE0(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileC() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileC(): "
        );

        switch(currentToken.getCategory()){
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
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileC: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileC(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileC0() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileC0(): "
        );

        switch(currentToken.getCategory()){
            case IMPLRIGHT:
                this.getLexer().matchToken(MheLexicalCategory.IMPLRIGHT);
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileC0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileC0(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileA() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileA(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileA: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }

        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileA(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileA0() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileA0(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileA0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileA0(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileO() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileO(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileO: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileO(): "
        )
        .decTabLevel();

        return r;
    }


    protected AbstractSintaxTree<LogicSemanticCategory> CompileO0() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileO0(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileO0: Se esperaba "
                        + y.toString()
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileO0(): "
        )
        .decTabLevel();

        return r;
    }


    protected AbstractSintaxTree<LogicSemanticCategory> CompileN() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileN(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileN: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- Compilen(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileL() throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileL(): "
        );

        switch(currentToken.getCategory()){
            case INTEGER:
                r = AST.constant(this.lset.processInteger(currentToken));
                this.getLexer().matchToken(MheLexicalCategory.INTEGER);
                break;
            case IDENTIFIER:
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = this.lset.processIdentifier(currentToken);

                r = s.getAST();

                if(r == null){
                    r = new ASTid(this.getLexeme());
                    s.setAST(r);
                }

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
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileL: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileL(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileX(boolean x) throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileX(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileX: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileX(): "
        )
        .decTabLevel();

        return r;
    }

    protected AbstractSintaxTree<LogicSemanticCategory> CompileX0(boolean x) throws CompilerException {
        AbstractSintaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileX0(): "
        );

        switch(currentToken.getCategory()){
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
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileX0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
                r = new ASTerror();
        }
        this
        .getLogger()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileX0(): "
        )
        .decTabLevel();

        return r;
    }
}
