package mhe.compiler.xson;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.logger.LogType;
import mhe.compiler.logger.Logger;
import mhe.compiler.mhe.MheLexicalCategory;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Token;
import mhe.xson.XsonArray;
import mhe.xson.XsonValue;
import mhe.xson.XsonValueType;
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
                r = new DefaultXsonObject();
                // CompileO
                this.getLexer().matchToken(MheLexicalCategory.RCORCH);

                break;
            case LKEY:
              	this.getLexer().matchToken(MheLexicalCategory.LKEY);
              	DefaultXsonArray s = new DefaultXsonArray();
              	r = s;
              	this.CompileL(s);
              	this.getLexer().matchToken(MheLexicalCategory.RKEY);
                break;


            case END:

                break;
            default:
                String[] y = { "", "$end" };
                this.getLogger().logError(
                        LogType.SYNTACTIC,
                        currentToken.getRow(),
                        currentToken.getCol(),
                        "CompileV: Se esperaba "
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
                "- CompileV: "
        )
        .decTabLevel();
        return r;
    }
    
    protected XsonValue CompileL(XsonArray xsonArray) throws CompilerException {
      XsonValue r = null;
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
          this.getLexer().matchToken(MheLexicalCategory.RCORCH);
          break;
        default:
          String[] y = { "" };
          this.getLogger().logError(
                  LogType.SYNTACTIC,
                  currentToken.getRow(),
                  currentToken.getCol(),
                  "CompileL: Se esperaba "
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
              "- CompileL: "
      )
      .decTabLevel();
      return r;
    }
    
    protected XsonValue CompileL0(XsonArray xsonArray) throws CompilerException {
      XsonValue r = null;
      Token<MheLexicalCategory> currentToken = this.getLexer().getCurrentToken();
      this
      .getLogger()
      .incTabLevel()
      .logMessage(
              LogType.SYNTACTIC,
              currentToken.getRow(),
              currentToken.getCol(),
              "+ CompileL0(): "
      );

      switch(currentToken.getCategory()){
        case COLON:
          break;
        case RCORCH:
          break;
        default:
          String[] y = { "" };
          this.getLogger().logError(
                  LogType.SYNTACTIC,
                  currentToken.getRow(),
                  currentToken.getCol(),
                  "CompileL0: Se esperaba "
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
              "- CompileL0: "
      )
      .decTabLevel();
      return r;
    }
      
}
