package mhe.compiler.xson;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
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

public class XsonParser {
    private Lexer<MheLexicalCategory> lexer;

    public Lexer<MheLexicalCategory> getLexer() {
        return this.lexer;
    }

    private Logger getLogger() {
        return this.getLexer().getLogger();
    }

    public XsonParser(Lexer<MheLexicalCategory> lexer) {
        this.lexer = lexer;
    }

    public XsonValue Compile() throws CompilerException {
        this.getLexer().getNextTokenCategory();
        return this.CompileV();
    }

    protected XsonValue CompileV() throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileV(): ");

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
            this.getLexer().matchToken(currentToken.getCategory());
            break;
        case STRING:
            r = new DefaultXsonValue(UtilString.parseString(currentToken.getLexeme()), XsonValueType.STRING);
            this.getLexer().matchToken(MheLexicalCategory.STRING);
            break;
        case INTEGER:
            r = new DefaultXsonValue(Integer.parseInt(currentToken.getLexeme()), XsonValueType.INTEGER);
            this.getLexer().matchToken(MheLexicalCategory.INTEGER);
            break;
        case DECIMAL:
            r = new DefaultXsonValue(Double.parseDouble(currentToken.getLexeme()), XsonValueType.DECIMAL);
            this.getLexer().matchToken(MheLexicalCategory.DECIMAL);
            break;
        case BOOLEAN:
            r = new DefaultXsonValue(Boolean.parseBoolean(currentToken.getLexeme()), XsonValueType.BOOLEAN);
            this.getLexer().matchToken(MheLexicalCategory.BOOLEAN);
            break;
        case LCORCH:
            this.getLexer().matchToken(MheLexicalCategory.LCORCH);
            DefaultXsonArray s = new DefaultXsonArray();
            this.CompileL(s);
            this.getLexer().matchToken(MheLexicalCategory.RCORCH);
            r = s;
            break;
        case LKEY:
            this.getLexer().matchToken(MheLexicalCategory.LKEY);
            r = new DefaultXsonObject();
            DefaultXsonObject x = new DefaultXsonObject();
            this.CompileO(x);
            this.getLexer().matchToken(MheLexicalCategory.RKEY);
            r = x;
            break;
        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Expected boolean, integer, decimal, string, array or object. " + "Found: " + currentToken);
        }

        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "- CompileV: " + r.getType()).decTabLevel();
        return r;
    }

    protected XsonValue CompileL(XsonArray xsonArray) throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();
        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileL(): ");

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
            xsonArray.add(this.CompileV());
            this.CompileL0(xsonArray);
            break;
        case RCORCH:
            break;
        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Expected boolean, integer, decimal, string, array, object or ']'. " + "Found: " + currentToken);
        }
        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(), "- CompileL: ")
                .decTabLevel();
        return r;
    }

    protected XsonValue CompileL0(XsonArray xsonArray) throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();
        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileL0(): ");

        switch (currentToken.getCategory()) {
        case COLON:
            this.getLexer().matchToken(MheLexicalCategory.COLON);
            r = this.CompileL(xsonArray);
            break;
        case RCORCH:
            break;
        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Exprected ',' or ']'. " + "Found: " + currentToken);
        }
        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(), "- CompileL0: ")
                .decTabLevel();
        return r;
    }

    protected XsonValue CompileO(XsonObject xsonObject) throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();
        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileO(): ");

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
            String key = this.CompileC();
            this.getLexer().matchToken(MheLexicalCategory.TWOPOINT);
            XsonValue val = this.CompileV();
            try {
                xsonObject.put(key, val);
            } catch (DuplicatedKeyException ex) {
                this.getLogger().logError(LogType.SEMANTIC, currentToken.getRow(), currentToken.getCol(),
                        "Duplicated key: " + ex.getKey());
            }
            this.CompileO0(xsonObject);
            break;
        case RKEY:
            break;
        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Expected key to parse or '}'. " + "Found: " + currentToken);
        }
        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(), "- CompileO: ")
                .decTabLevel();
        return r;
    }

    protected XsonValue CompileO0(XsonObject xsonObject) throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();
        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileL0(): ");

        switch (currentToken.getCategory()) {
        case COLON:
            this.getLexer().matchToken(MheLexicalCategory.COLON);
            r = this.CompileO(xsonObject);
            break;
        case RKEY:
            break;
        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Exprected ',' or '}'. " + "Found: " + currentToken);
        }
        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(), "- CompileO0: ")
                .decTabLevel();
        return r;
    }

    protected String CompileC() throws CompilerException {
        String r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this.getLogger().incTabLevel().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                "+ CompileC(): ");

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
            this.getLexer().matchToken(currentToken.getCategory());
            break;
        case STRING:
            r = UtilString.unescape_perl_string(currentToken.getLexeme());
            this.getLexer().matchToken(currentToken.getCategory());
            break;

        default:
            this.getLogger().logError(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(),
                    "Invalid key: " + currentToken);
        }
        this.getLogger().logMessage(LogType.SYNTACTIC, currentToken.getRow(), currentToken.getCol(), "- CompileC: " + r)
                .decTabLevel();
        return r;
    }

}
