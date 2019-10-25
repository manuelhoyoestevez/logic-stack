package mhe.compiler.xson;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Token;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
import mhe.xson.impl.DefaultXsonValue;

public class XsonParser {
    private Lexer<MheLexicalCategory> lexer;

    public Lexer<MheLexicalCategory> getLexer() {
        return this.lexer;
    }

    private Logger getLogger() {
        return this.getLexer().getLogger();
    }

    public static String formatSymbolList(String[] list) {
        String ret = "";
        boolean f = true;
        for(String symbol : list) {
            if(f) {
                f = false;
            }
            else {
                ret += ", ";
            }
            ret += "'" + symbol + "'";
        }
        return ret;
    }

    public XsonParser(Lexer<MheLexicalCategory> lexer) {
        this.lexer = lexer;
    }

    protected String getLexeme() {
        return this.getLexer().getStream().getLexeme();
    }

    public XsonValue Compile() throws CompilerException {
        //this.currenttokencat = this.getLexer().getNextTokenCategory();

        this.getLexer().getNextTokenCategory();
        return this.CompileV();
    }

    protected XsonValue CompileV() throws CompilerException {
        XsonValue r = null;
        Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();

        this
        .getLogger()
        .incTabLevel()
        .logMessage(
                LogType.SYNTACTIC,
                currentToken.getRow(),
                currentToken.getCol(),
                "+ CompileV(): "
        );

        switch(currentToken.getCategory()){
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
                break;
            case STRING:
                r = new DefaultXsonValue(currentToken.getLexeme(), XsonValueType.STRING);
                break;
            case INTEGER:
                r = new DefaultXsonValue(Integer.parseInt(currentToken.getLexeme()), XsonValueType.INTEGER);
                break;
            case DECIMAL:
                r = new DefaultXsonValue(Double.parseDouble(currentToken.getLexeme()), XsonValueType.DECIMAL);
                break;
            case BOOLEAN:
                r = new DefaultXsonValue(Boolean.parseBoolean(currentToken.getLexeme()), XsonValueType.BOOLEAN);
                break;
            case LCORCH:
                this.getLexer().matchToken(MheLexicalCategory.LCORCH);

                break;
            case LKEY:
                break;


            case END:

                break;
            default:
                String[] y = { "identificador", "show", "save", "exit", "$end" };
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileP: Se esperaba "
                        + formatSymbolList(y)
                        + " en lugar de "
                        + currentToken
                );
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
}
