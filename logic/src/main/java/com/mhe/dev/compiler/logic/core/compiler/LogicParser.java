package com.mhe.dev.compiler.logic.core.compiler;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.Token;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstA;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstAssignment;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstC;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstConst;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstE;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstId;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstN;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstO;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstP;
import com.mhe.dev.compiler.logic.core.compiler.ast.AstReturn;

/**
 * LogicParser.
 */
public class LogicParser
{
    private final MheLogger logger;
    private final Lexer<MheLexicalCategory> lexer;
    private final LogicSymbolMap logicSymbolMap;

    /**
     * Constructor.
     *
     * @param logger Logger
     * @param lexer  Lexer
     * @param logicSymbolMap LogicSymbolMap
     */
    public LogicParser(MheLogger logger, Lexer<MheLexicalCategory> lexer, LogicSymbolMap logicSymbolMap)
    {
        this.logger = logger;
        this.lexer = lexer;
        this.logicSymbolMap = logicSymbolMap;
    }

    private static String formatSymbolList(String[] list)
    {
        StringBuilder ret = new StringBuilder();
        boolean f = true;
        for (String symbol : list)
        {
            if (f)
            {
                f = false;
            } else
            {
                ret.append(", ");
            }
            ret.append("'").append(symbol).append("'");
        }
        return ret.toString();
    }

    public LogicSymbolMap getLogicSymbolMap()
    {
        return logicSymbolMap;
    }

    public AbstractSyntaxTree<LogicSemanticCategory> compile() throws CompilerException
    {
        lexer.getNextTokenCategory();
        return compileP();
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> compileP() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileP(): "
        );

        switch (currentToken.getCategory())
        {
            case IDENTIFIER:
            case RETURN:
                AbstractSyntaxTree<LogicSemanticCategory> s = compileS();
                lexer.matchToken(MheLexicalCategory.SEMICOLON);
                r = new AstP(s, compileP());
                break;
            case END:
                r = new AstP();
                break;
            default:
                String[] y = {"identificador", "return", "$end"};
                String message = "CompileP: Se esperaba " + formatSymbolList(y) + " en lugar de " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileP: ");
        return r;
    }

    protected AbstractSyntaxTree<LogicSemanticCategory> compileS() throws CompilerException
    {
        String id;
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileS(): "
        );

        switch (currentToken.getCategory())
        {
            case RETURN:
                lexer.matchToken(MheLexicalCategory.RETURN);
                r = new AstReturn(compileE());
                break;
            case IDENTIFIER:
                id = lexer.getStream().getLexeme();
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = logicSymbolMap.processAssignment(currentToken);
                lexer.matchToken(MheLexicalCategory.IDENTIFIER);
                lexer.matchToken(MheLexicalCategory.EQUAL);
                AbstractSyntaxTree<LogicSemanticCategory> e = compileE();
                s.setAst(e);
                r = new AstAssignment(id, e);
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileE() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileE(): "
        );

        switch (currentToken.getCategory())
        {
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstE(compileC(), compileE0());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileE0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileE0(): "
        );

        switch (currentToken.getCategory())
        {
            case EQUALEQ:
                lexer.matchToken(MheLexicalCategory.EQUALEQ);
                r = compileE();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
                r = new AstE();
                break;
            default:
                String[] y = {"==", ")", ";", "]", "}", ","};
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileC() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileC(): "
        );

        switch (currentToken.getCategory())
        {
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstC(compileA(), compileC0());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileC0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileC0(): "
        );

        switch (currentToken.getCategory())
        {
            case IMPLRIGHT:
                lexer.matchToken(MheLexicalCategory.IMPLRIGHT);
                r = compileC();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case EQUALEQ:
                r = new AstC();
                break;
            default:
                String[] y = {"->", "==", ")", ";", "]", "}", ","};
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileA() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileA(): "
        );

        switch (currentToken.getCategory())
        {
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstA(compileO(), compileA0());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileA0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileA0(): "
        );

        switch (currentToken.getCategory())
        {
            case AMPERSAND:
                lexer.matchToken(MheLexicalCategory.AMPERSAND);
                r = compileA();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case EQUALEQ:
            case IMPLRIGHT:
                r = new AstA();
                break;
            default:
                String[] y = {"&", "->", "==", ")", ";", "]", "}", ","};
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileO() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileO(): "
        );

        switch (currentToken.getCategory())
        {
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstO(compileN(), compileO0());
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


    protected AbstractSyntaxTree<LogicSemanticCategory> compileO0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileO0(): "
        );

        switch (currentToken.getCategory())
        {
            case BAR:
                lexer.matchToken(MheLexicalCategory.BAR);
                r = compileO();
                break;
            case COLON:
            case RKEY:
            case RCORCH:
            case SEMICOLON:
            case RPAREN:
            case EQUALEQ:
            case IMPLRIGHT:
            case AMPERSAND:
                r = new AstO();
                break;
            default:
                String[] y = {"|", "&", "->", "==", ")", ";", "]", "}", ","};
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


    protected AbstractSyntaxTree<LogicSemanticCategory> compileN() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileN(): "
        );

        switch (currentToken.getCategory())
        {
            case LKEY:
            case LCORCH:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
                r = compileL();
                break;
            case NOT:
                lexer.matchToken(MheLexicalCategory.NOT);
                r = new AstN(compileN());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileL() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileL(): "
        );

        switch (currentToken.getCategory())
        {
            case INTEGER:
                r = new AstConst(logicSymbolMap.processInteger(currentToken));
                lexer.matchToken(MheLexicalCategory.INTEGER);
                break;
            case IDENTIFIER:
                Symbol<MheLexicalCategory, LogicSemanticCategory> s = logicSymbolMap.processIdentifier(currentToken);

                AbstractSyntaxTree<LogicSemanticCategory> aux = s.getAst();

                if (aux == null)
                {
                    r = new AstId(lexer.getStream().getLexeme());
                    s.setAst(r);
                } else
                {
                    r = aux;
                }

                lexer.matchToken(MheLexicalCategory.IDENTIFIER);
                break;
            case LPAREN:
                lexer.matchToken(MheLexicalCategory.LPAREN);
                r = compileE();
                lexer.matchToken(MheLexicalCategory.RPAREN);
                break;
            case LKEY:
                lexer.matchToken(MheLexicalCategory.LKEY);
                r = compileX();
                lexer.matchToken(MheLexicalCategory.RKEY);
                break;
            case LCORCH:
                lexer.matchToken(MheLexicalCategory.LCORCH);
                r = compileY();
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileX() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileX(): "
        );

        switch (currentToken.getCategory())
        {
            case LCORCH:
            case LKEY:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstO(compileE(), compileX0());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileX0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileX0(): "
        );

        switch (currentToken.getCategory())
        {
            case COLON:
                lexer.matchToken(MheLexicalCategory.COLON);
                r = compileX();
                break;
            case RKEY:
            case RCORCH:
                r = new AstO();
                break;
            default:
                String[] y = {"]", "}", ","};
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileY() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileY(): "
        );

        switch (currentToken.getCategory())
        {
            case LCORCH:
            case LKEY:
            case INTEGER:
            case IDENTIFIER:
            case LPAREN:
            case NOT:
                r = new AstA(compileE(), compileY0());
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

    protected AbstractSyntaxTree<LogicSemanticCategory> compileY0() throws CompilerException
    {
        AbstractSyntaxTree<LogicSemanticCategory> r;
        Token<MheLexicalCategory> currentToken = lexer.getCurrentToken();

        logger.parser(
            currentToken.getRow(),
            currentToken.getCol(),
            "+ CompileY0(): "
        );

        switch (currentToken.getCategory())
        {
            case COLON:
                lexer.matchToken(MheLexicalCategory.COLON);
                r = compileY();
                break;
            case RKEY:
            case RCORCH:
                r = new AstA();
                break;
            default:
                String[] y = {"]", "}", ","};
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
