package mhe.compiler.logic;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.MheLogger;
import mhe.compiler.logger.MheLoggerFactory;
import mhe.compiler.logic.ast.AstA;
import mhe.compiler.logic.ast.AstAssignment;
import mhe.compiler.logic.ast.AstC;
import mhe.compiler.logic.ast.AstConst;
import mhe.compiler.logic.ast.AstE;
import mhe.compiler.logic.ast.AstExit;
import mhe.compiler.logic.ast.AstId;
import mhe.compiler.logic.ast.AstN;
import mhe.compiler.logic.ast.AstO;
import mhe.compiler.logic.ast.AstP;
import mhe.compiler.logic.ast.AstReturn;
import mhe.compiler.logic.ast.AstSave;
import mhe.compiler.logic.ast.AstShow;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.LambdaAbstractSyntaxTree;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.Token;

public class LogicParser {
    private static final MheLogger logger = MheLoggerFactory.getLogger(LogicParser.class);

    private final Lexer<MheLexicalCategory> lexer;
    private final LogicSymbolMap logicSymbolMap;

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
        return logicSymbolMap;
    }

    public AbstractSyntaxTree<LogicSemanticCategory> Compile() throws CompilerException {
        lexer.getNextTokenCategory();
        return CompileP();
    }

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileP() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
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
                NoLambdaAbstractSyntaxTree<LogicSemanticCategory> s = CompileS();
                lexer.matchToken(MheLexicalCategory.SEMICOLON);
                r = new AstP(s, CompileP());
                break;
            case END:
                r = new AstP();
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

    protected NoLambdaAbstractSyntaxTree<LogicSemanticCategory> CompileS() throws CompilerException {
        String id;
        NoLambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileS(): "
        );

        switch(currentToken.getCategory()){
            case RETURN:
                lexer.matchToken(MheLexicalCategory.RETURN);
                r = new AstReturn(CompileE());
                break;
            case SHOW:
                lexer.matchToken(MheLexicalCategory.SHOW);
                id = logicSymbolMap.processShow(currentToken);
                lexer.matchToken(MheLexicalCategory.IDENTIFIER);
                r = new AstShow(id);
                break;
            case EXIT:
                lexer.matchToken(MheLexicalCategory.EXIT);
                r = new AstExit();
                break;
            case SAVE:
                lexer.matchToken(MheLexicalCategory.SAVE);
                // Comprobaciones de fichero
                id = lexer.getStream().getLexeme();
                lexer.matchToken(MheLexicalCategory.STRING);
                r = new AstSave(id);
                break;
            case IDENTIFIER:
                id = lexer.getStream().getLexeme();
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = logicSymbolMap.processAssignment(currentToken);
                lexer.matchToken(MheLexicalCategory.IDENTIFIER);
                lexer.matchToken(MheLexicalCategory.EQUAL);
                LambdaAbstractSyntaxTree<LogicSemanticCategory> e = CompileE();
                s.setAST(e);
                r = new AstAssignment(id,e);
                break;
            default:
                String[] y = {"return", "identificador", "show", "save", "exit"};
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileE() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = new AstE(CompileC(),CompileE0());
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileE0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileE0(): "
        );

        switch(currentToken.getCategory()){
            case IMPLDOUBLE:
                lexer.matchToken(MheLexicalCategory.IMPLDOUBLE);
                r = CompileE();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
                r = new AstE();
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileC() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = new AstC(CompileA(),CompileC0());
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileC0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileC0(): "
        );

        switch(currentToken.getCategory()){
            case IMPLRIGHT:
                lexer.matchToken(MheLexicalCategory.IMPLRIGHT);
                r = CompileC();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case IMPLDOUBLE:
                r = new AstC();
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileA() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = new AstA(CompileO(), CompileA0());
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileA0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileA0(): "
        );

        switch(currentToken.getCategory()){
            case AMPERSAND:
                lexer.matchToken(MheLexicalCategory.AMPERSAND);
                r = CompileA();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case IMPLDOUBLE:
            case IMPLRIGHT:
                r = new AstA();
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileO() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = new AstO(CompileN(),CompileO0());
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


    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileO0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileO0(): "
        );

        switch(currentToken.getCategory()){
            case BAR:
                lexer.matchToken(MheLexicalCategory.BAR);
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
                r = new AstO();
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
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = CompileL();
                break;
            case NOT:
                lexer.matchToken(MheLexicalCategory.NOT);
                r = new AstN(CompileN());
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
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileL(): "
        );

        switch(currentToken.getCategory()){
            case INTEGER:
                r = new AstConst(logicSymbolMap.processInteger(currentToken));
                lexer.matchToken(MheLexicalCategory.INTEGER);
                break;
            case IDENTIFIER:
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = logicSymbolMap.processIdentifier(currentToken);

                AbstractSyntaxTree<LogicSemanticCategory> aux = s.getAST();

                if(aux == null){
                    r = new AstId(lexer.getStream().getLexeme());
                    s.setAST(r);
                } else {
                    r = aux;
                }

                lexer.matchToken(MheLexicalCategory.IDENTIFIER);
                break;
            case LPAREN:
                lexer.matchToken(MheLexicalCategory.LPAREN);
                r = CompileE();
                lexer.matchToken(MheLexicalCategory.RPAREN);
                break;
            case LKEY:
                lexer.matchToken(MheLexicalCategory.LKEY);
                r = CompileX();
                lexer.matchToken(MheLexicalCategory.RKEY);
                break;
            case LCORCH:
                lexer.matchToken(MheLexicalCategory.LCORCH);
                r = CompileY();
                lexer.matchToken(MheLexicalCategory.RCORCH);
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileX() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

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
                r = new AstO(CompileE(), CompileX0());
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileX0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileX0(): "
        );

        switch(currentToken.getCategory()){
            case COLON:
                lexer.matchToken(MheLexicalCategory.COLON);
                r = CompileX();
                break;
            case RKEY:
            case RCORCH:
                r = new AstO();
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

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileY() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileY(): "
        );

        switch(currentToken.getCategory()){
            case LCORCH:
            case LKEY:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstA(CompileE(), CompileY0());
                break;
            default:
                String[] y = {"identificador", "entero", "(", "[", "{", "!"};
                String message =
                    "CompileY: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "- CompileY(): "
        );

        return r;
    }

    protected LambdaAbstractSyntaxTree<LogicSemanticCategory> CompileY0() throws CompilerException {
        LambdaAbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileY0(): "
        );

        switch(currentToken.getCategory()){
            case COLON:
                lexer.matchToken(MheLexicalCategory.COLON);
                r = CompileY();
                break;
            case RKEY:
            case RCORCH:
                r = new AstA();
                break;
            default:
                String[] y = { "]", "}", "," };
                String message =
                    "CompileY0: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken;

                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "- CompileY0(): "
        );

        return r;
    }
}
