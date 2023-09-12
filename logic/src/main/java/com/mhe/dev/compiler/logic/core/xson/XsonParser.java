package com.mhe.dev.compiler.logic.core.xson;

import com.mhe.dev.compiler.lib.core.CompilerException;
import com.mhe.dev.compiler.lib.core.Lexer;
import com.mhe.dev.compiler.lib.core.MheLexicalCategory;
import com.mhe.dev.compiler.lib.core.MheLogger;
import com.mhe.dev.compiler.lib.core.Token;
import com.mhe.dev.compiler.logic.core.xson.impl.DefaultXsonValue;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * XsonParser.
 */
public class XsonParser
{
    private final MheLogger logger;
    private final Lexer<MheLexicalCategory> lexer;

    public XsonParser(MheLogger logger, Lexer<MheLexicalCategory> lexer)
    {
        this.logger = logger;
        this.lexer = lexer;
    }

    public Lexer<MheLexicalCategory> getLexer()
    {
        return lexer;
    }

    public XsonValue compile() throws CompilerException
    {
        getLexer().getNextTokenCategory();
        return compileV();
    }

    protected XsonValue compileV() throws CompilerException
    {
        XsonValue r;
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileV(): ");

        switch (currentToken.getCategory())
        {
            case RETURN:
            case IDENTIFIER:
                r = DefaultXsonValue.createXsonValue(currentToken.getLexeme());
                getLexer().matchToken(currentToken.getCategory());
                break;
            case STRING:
                r = DefaultXsonValue.createXsonValue(UtilString.parseString(currentToken.getLexeme()));
                getLexer().matchToken(MheLexicalCategory.STRING);
                break;
            case INTEGER:
                r = DefaultXsonValue.createXsonValue(Long.parseLong(currentToken.getLexeme()));
                getLexer().matchToken(MheLexicalCategory.INTEGER);
                break;
            case DECIMAL:
                r = DefaultXsonValue.createXsonValue(Double.parseDouble(currentToken.getLexeme()));
                getLexer().matchToken(MheLexicalCategory.DECIMAL);
                break;
            case BOOLEAN:
                r = DefaultXsonValue.createXsonValue(Boolean.parseBoolean(currentToken.getLexeme()));
                getLexer().matchToken(MheLexicalCategory.BOOLEAN);
                break;
            case LCORCH:
                getLexer().matchToken(MheLexicalCategory.LCORCH);
                List<XsonValue> list = new ArrayList<>();
                compileL(list);
                getLexer().matchToken(MheLexicalCategory.RCORCH);
                r = DefaultXsonValue.createXsonArray(list);
                break;
            case LKEY:
                getLexer().matchToken(MheLexicalCategory.LKEY);
                Map<String, XsonValue> map = new LinkedHashMap<>();
                compileO(map);
                getLexer().matchToken(MheLexicalCategory.RKEY);
                r = DefaultXsonValue.createXsonObject(map);
                break;
            default:
                String message = "Expected boolean, integer, decimal, string, array or object. "
                    + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileV: " + r.getValueClass());
        return r;
    }

    protected void compileL(List<XsonValue> list) throws CompilerException
    {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL(): ");

        switch (currentToken.getCategory())
        {
            case RETURN:
            case IDENTIFIER:
            case STRING:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
            case LCORCH:
            case LKEY:
                list.add(compileV());
                compileL0(list);
                break;
            case RCORCH:
                break;
            default:
                String message = "Expected boolean, integer, decimal, string, array, object or ']'. "
                    + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileL: ");
    }

    protected void compileL0(List<XsonValue> list) throws CompilerException
    {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL0(): ");

        switch (currentToken.getCategory())
        {
            case COLON:
                getLexer().matchToken(MheLexicalCategory.COLON);
                compileL(list);
                break;
            case RCORCH:
                break;
            default:
                String message = "Expected ',' or ']'. " + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileL0: ");
    }

    protected void compileO(Map<String, XsonValue> map) throws CompilerException
    {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileO(): ");

        switch (currentToken.getCategory())
        {
            case RETURN:
            case IDENTIFIER:
            case STRING:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                String key = compileC();
                getLexer().matchToken(MheLexicalCategory.TWOPOINT);
                XsonValue val = compileV();
                if (map.containsKey(key))
                {
                    String message = "Duplicated key: " + key;
                    logger.error(currentToken.getRow(), currentToken.getCol(), message);
                    throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
                }
                map.put(key, val);
                compileO0(map);
                break;
            case RKEY:
                break;
            default:
                String message = "Expected key to parse or '}'. " + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileO: ");
    }

    protected void compileO0(Map<String, XsonValue> map) throws CompilerException
    {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL0(): ");

        switch (currentToken.getCategory())
        {
            case COLON:
                getLexer().matchToken(MheLexicalCategory.COLON);
                compileO(map);
                break;
            case RKEY:
                break;
            default:
                String message = "Expected ',' or '}'. " + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileO0: ");
    }

    protected String compileC() throws CompilerException
    {
        String r;
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();

        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileC(): ");

        switch (currentToken.getCategory())
        {
            case RETURN:
            case IDENTIFIER:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                r = currentToken.getLexeme();
                getLexer().matchToken(currentToken.getCategory());
                break;
            case STRING:
                r = UtilString.parseString(currentToken.getLexeme());
                getLexer().matchToken(currentToken.getCategory());
                break;
            default:
                String message = "Invalid key: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileC: " + r);
        return r;
    }
}
