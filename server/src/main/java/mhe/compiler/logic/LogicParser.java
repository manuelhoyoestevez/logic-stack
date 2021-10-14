package mhe.compiler.logic;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.MheLogger;
import mhe.compiler.logger.MheLoggerFactory;
import mhe.compiler.logic.ast.AST;
import mhe.compiler.logic.ast.ASTa;
import mhe.compiler.logic.ast.ASTasig;
import mhe.compiler.logic.ast.ASTc;
import mhe.compiler.logic.ast.ASTe;
import mhe.compiler.logic.ast.ASTexit;
import mhe.compiler.logic.ast.ASTid;
import mhe.compiler.logic.ast.ASTn;
import mhe.compiler.logic.ast.ASTo;
import mhe.compiler.logic.ast.ASTp;
import mhe.compiler.logic.ast.ASTreturn;
import mhe.compiler.logic.ast.ASTsave;
import mhe.compiler.logic.ast.ASTshow;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.Token;

public class LogicParser {
    private static final MheLogger logger = MheLoggerFactory.getLogger(LogicParser.class);

    private final Lexer<MheLexicalCategory> lexer;
    private final LogicSymbolMap logicSymbolMap;

    public Lexer<MheLexicalCategory> getLexer() {
        return this.lexer;
    }

    public static String formatSymbolList(String[] list) {
        StringBuilder ret = new StringBuilder();
        boolean f = true;
        for(String symbol : list) {
            if(f) {
                f = false;
            }
            else {
                ret.append(", ");
            }
            ret.append("'").append(symbol).append("'");
        }
        return ret.toString();
    }

    public LogicParser(Lexer<MheLexicalCategory> lexer, LogicSymbolMap logicSymbolMap) {
        this.lexer = lexer;
        this.logicSymbolMap = logicSymbolMap;
    }

    public LogicSymbolMap getLogicSymbolMap() {
        return this.logicSymbolMap;
    }

    protected String getLexeme() {
        return this.getLexer().getStream().getLexeme();
    }

    public AbstractSyntaxTree<LogicSemanticCategory> Compile() throws CompilerException {
        this.getLexer().getNextTokenCategory();
        return this.CompileP();
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileP() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileP(): ");

        switch(currentToken.getCategory()){
            case IDENTIFIER:
            case EXIT:
            case SAVE:
            case SHOW:
            case RETURN:
                AbstractSyntaxTree<LogicSemanticCategory> s = this.CompileS();
                this.getLexer().matchToken(MheLexicalCategory.SEMICOLON);
                r = new ASTp(s, this.CompileP());
                break;
            case END:
                r = AST.ASTlambda;
                break;
            default:
                String[] y = {"identificador", "show", "save", "exit", "$end"};
                String message = "CompileP: Se esperaba " + formatSymbolList(y) + " en lugar de " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileP: ");
        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileS() throws CompilerException {
        String id;
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                id = this.logicSymbolMap.processShow(currentToken);
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
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = this.logicSymbolMap.processAssignment(currentToken);
                this.getLexer().matchToken(MheLexicalCategory.IDENTIFIER);
                this.getLexer().matchToken(MheLexicalCategory.EQUAL);
                AbstractSyntaxTree<LogicSemanticCategory> e = this.CompileE();
                s.setAST(e);
                r = new ASTasig(id,e);
                break;
            default:
                String[] y = {"identificador", "show", "save", "exit"};
                String message = "CompileS: Se esperaba " + formatSymbolList(y) + " en lugar de " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileS(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileE() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message = "CompileE: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileE(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileE0() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message = "CompileE0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileE0(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileC() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message = "CompileC: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileC(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileC0() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileC0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileC0(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileA() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileA: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileA(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileA0() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileA0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileA0(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileO() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileO: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileO(): "
        );

        return r;
    }


    protected AbstractSyntaxTree<LogicSemanticCategory> CompileO0() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileO0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileO0(): "
        );

        return r;
    }


    protected AbstractSyntaxTree<LogicSemanticCategory> CompileN() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileN: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- Compilen(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileL() throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileL(): "
        );

        switch(currentToken.getCategory()){
            case INTEGER:
                r = AST.constant(this.logicSymbolMap.processInteger(currentToken));
                this.getLexer().matchToken(MheLexicalCategory.INTEGER);
                break;
            case IDENTIFIER:
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = this.logicSymbolMap.processIdentifier(currentToken);

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
                String message =
                        "CompileL: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileL(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileX(boolean x) throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                    ? new ASTo(this.CompileE(),this.CompileX0(true))
                    : new ASTa(this.CompileE(),this.CompileX0(false));
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                String message =
                        "CompileX: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileX(): "
        );

        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> CompileX0(boolean x) throws CompilerException {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        logger.parser(
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
                String message =
                        "CompileX0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "- CompileX0(): "
        );

        return r;
    }
}
