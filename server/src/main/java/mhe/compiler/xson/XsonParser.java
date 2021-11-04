package mhe.compiler.xson;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.MheLogger;
import mhe.compiler.logger.MheLoggerFactory;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.mhe.UtilString;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Token;
import mhe.xson.XsonArray;
import mhe.xson.XsonObject;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.exception.DuplicatedKeyException;
import mhe.xson.impl.DefaultXsonArray;
import mhe.xson.impl.DefaultXsonObject;
import mhe.xson.impl.DefaultXsonValue;

/**
 * XsonParser.
 */
public class XsonParser {
    private static final MheLogger logger = MheLoggerFactory.getLogger(XsonParser.class);

    private final Lexer<MheLexicalCategory> lexer;

    public XsonParser(Lexer<MheLexicalCategory> lexer) {
        this.lexer = lexer;
    }

    public Lexer<MheLexicalCategory> getLexer() {
        return lexer;
    }

    public XsonValue compile() throws CompilerException {
        getLexer().getNextTokenCategory();
        return compileV();
    }

    protected XsonValue compileV() throws CompilerException {
        XsonValue r;
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileV(): ");

        switch (currentToken.getCategory()) {
            case EXIT:
            case LOAD:
            case SAVE:
            case SHOW:
            case LIST:
            case TEST:
            case BLANK:
            case ALPHABET:
            case COMMENT:
            case TOKEN:
            case RETURN:
            case GRAMMAR:
            case SEMANTIC:
            case LAMBDA:
            case IDENTIFIER:
                r = new DefaultXsonValue(currentToken.getLexeme(), XsonValueType.STRING);
                getLexer().matchToken(currentToken.getCategory());
                break;
            case STRING:
                r = new DefaultXsonValue(UtilString.parseString(currentToken.getLexeme()), XsonValueType.STRING);
                getLexer().matchToken(MheLexicalCategory.STRING);
                break;
            case INTEGER:
                r = new DefaultXsonValue(Integer.parseInt(currentToken.getLexeme()), XsonValueType.INTEGER);
                getLexer().matchToken(MheLexicalCategory.INTEGER);
                break;
            case DECIMAL:
                r = new DefaultXsonValue(Double.parseDouble(currentToken.getLexeme()), XsonValueType.DECIMAL);
                getLexer().matchToken(MheLexicalCategory.DECIMAL);
                break;
            case BOOLEAN:
                r = new DefaultXsonValue(Boolean.parseBoolean(currentToken.getLexeme()), XsonValueType.BOOLEAN);
                getLexer().matchToken(MheLexicalCategory.BOOLEAN);
                break;
            case LCORCH:
                getLexer().matchToken(MheLexicalCategory.LCORCH);
                DefaultXsonArray s = new DefaultXsonArray();
                compileL(s);
                getLexer().matchToken(MheLexicalCategory.RCORCH);
                r = s;
                break;
            case LKEY:
                getLexer().matchToken(MheLexicalCategory.LKEY);
                DefaultXsonObject x = new DefaultXsonObject();
                compileO(x);
                getLexer().matchToken(MheLexicalCategory.RKEY);
                r = x;
                break;
            default:
                String message = "Expected boolean, integer, decimal, string, array or object. "
                        + "Found: " + currentToken;
                logger.error(currentToken.getRow(), currentToken.getCol(), message);
                throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, null);
        }
        logger.parser(currentToken.getRow(), currentToken.getCol(), "- CompileV: " + r.getType());
        return r;
    }

    protected void compileL(XsonArray xsonArray) throws CompilerException {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL(): ");

        switch (currentToken.getCategory()) {
            case EXIT:
            case LOAD:
            case SAVE:
            case SHOW:
            case LIST:
            case TEST:
            case BLANK:
            case ALPHABET:
            case COMMENT:
            case TOKEN:
            case RETURN:
            case GRAMMAR:
            case SEMANTIC:
            case LAMBDA:
            case IDENTIFIER:
            case STRING:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
            case LCORCH:
            case LKEY:
                xsonArray.add(compileV());
                compileL0(xsonArray);
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

    protected void compileL0(XsonArray xsonArray) throws CompilerException {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL0(): ");

        switch (currentToken.getCategory()) {
            case COLON:
                getLexer().matchToken(MheLexicalCategory.COLON);
                compileL(xsonArray);
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

    protected void compileO(XsonObject xsonObject) throws CompilerException {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileO(): ");

        switch (currentToken.getCategory()) {
            case EXIT:
            case LOAD:
            case SAVE:
            case SHOW:
            case LIST:
            case TEST:
            case BLANK:
            case ALPHABET:
            case COMMENT:
            case TOKEN:
            case RETURN:
            case GRAMMAR:
            case SEMANTIC:
            case LAMBDA:
            case IDENTIFIER:
            case STRING:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                String key = compileC();
                getLexer().matchToken(MheLexicalCategory.TWOPOINT);
                XsonValue val = compileV();
                try {
                    xsonObject.put(key, val);
                } catch (DuplicatedKeyException ex) {
                    String message = "Duplicated key: " + ex.getKey();
                    logger.error(currentToken.getRow(), currentToken.getCol(), message);
                    throw new CompilerException(currentToken.getRow(), currentToken.getCol(), message, ex);
                }
                compileO0(xsonObject);
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

    protected void compileO0(XsonObject xsonObject) throws CompilerException {
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();
        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileL0(): ");

        switch (currentToken.getCategory()) {
            case COLON:
                getLexer().matchToken(MheLexicalCategory.COLON);
                compileO(xsonObject);
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

    protected String compileC() throws CompilerException {
        String r;
        Token<MheLexicalCategory> currentToken = getLexer().getCurrentToken();

        logger.parser(currentToken.getRow(), currentToken.getCol(), "+ CompileC(): ");

        switch (currentToken.getCategory()) {
            case EXIT:
            case LOAD:
            case SAVE:
            case SHOW:
            case LIST:
            case TEST:
            case BLANK:
            case ALPHABET:
            case COMMENT:
            case TOKEN:
            case RETURN:
            case GRAMMAR:
            case SEMANTIC:
            case LAMBDA:
            case IDENTIFIER:
            case INTEGER:
            case DECIMAL:
            case BOOLEAN:
                r = currentToken.getLexeme();
                getLexer().matchToken(currentToken.getCategory());
                break;
            case STRING:
                r = UtilString.unescape_perl_string(currentToken.getLexeme());
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
