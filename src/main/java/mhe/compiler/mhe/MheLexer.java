package mhe.compiler.mhe;

import java.io.StringReader;

import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.DefaultLogger;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.Token;
import mhe.compiler.model.impl.AbstractLexer;
import mhe.compiler.model.impl.AbstractStream;

/**
 * Analizador léxico
 *
 * @author Manuel Hoyo Estévez
 *
 */
public class MheLexer extends AbstractLexer<MheLexicalCategory> {

    public MheLexer(Stream stream) {
        super(stream);
    }

    @Override
    public MheLexicalCategory getErrorCategory() {
        return MheLexicalCategory.ERROR;
    }

    @Override
    public MheLexicalCategory getSkipCategory() {
        return MheLexicalCategory.SKIP;
    }

    @Override
    protected MheLexicalCategory compileToken() throws CompilerIOException {
        char c = this.getStream().getNextCharacter();

        if(isLetter(c)) {
            return CompileWord();
        }
        else if(isNumber(c)) {
            return CompileNumber();
        }
        else if(c == this.getStream().getErrorCategory()) {
            return MheLexicalCategory.ERROR;
        }
        else if(c == this.getStream().getEndCategory()) {
             return MheLexicalCategory.END;
        } else {
            switch(c){
                case '\n':
                case '\t':
                case '\r':
                case ' ' : return MheLexicalCategory.SKIP;
                case '.' : return MheLexicalCategory.POINT;
                case ',' : return MheLexicalCategory.COLON;
                case ':' : return MheLexicalCategory.TWOPOINT;
                case ';' : return MheLexicalCategory.SEMICOLON;
                case '(' : return MheLexicalCategory.LPAREN;
                case ')' : return MheLexicalCategory.RPAREN;
                case '[' : return MheLexicalCategory.LCORCH;
                case ']' : return MheLexicalCategory.RCORCH;
                case '{' : return MheLexicalCategory.LKEY;
                case '}' : return MheLexicalCategory.RKEY;
                case '%' : return MheLexicalCategory.PERCENT;
                case '?' : return MheLexicalCategory.HOOK;
                case '+' : return this.CompilePlus();
                case '-' : return this.CompileMinus();
                case '*' : return this.CompileStar();
                case '/' : return this.CompileDiv();
                case '&' : return this.CompileAmpersand();
                case '|' : return this.CompileBar();
                case '!' : return this.CompileExclamation();
                case '=' : return this.CompileEqual();
                case '<' : return this.CompileSmaller();
                case '>' : return this.CompileBigger();
                case '\'': return this.CompileCharacter();
                case '\"': return this.CompileString();
                default  : return MheLexicalCategory.ERROR;
            }
        }
    }

    protected MheLexicalCategory CompilePlus() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.PLUSEQ;
            case '+': return MheLexicalCategory.INC;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.PLUS;
        }
    }

    protected MheLexicalCategory CompileMinus() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.MINUSEQ;
            case '-': return MheLexicalCategory.DEC;
            case '>': return MheLexicalCategory.IMPLRIGHT;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.MINUS;
        }
    }

    protected MheLexicalCategory CompileStar() throws CompilerIOException{
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.STAREQ;
            case '*': return MheLexicalCategory.POW;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.STAR;
        }
    }

    protected MheLexicalCategory CompileDiv() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.DIVEQ;
            case '*': return CompileMultiCommA();
            case '/': return CompileUniComm();
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.DIV;
        }
    }

    protected MheLexicalCategory CompileBigger() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.BIGGEREQ;
            case '>': return MheLexicalCategory.MOVERIGHT;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.BIGGER;
        }
    }

    protected MheLexicalCategory CompileSmaller() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.SMALLEREQ;
            case '<': return MheLexicalCategory.MOVELEFT;
            case '>': return MheLexicalCategory.IMPLDOUBLE;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.SMALLER;
        }
    }

    protected MheLexicalCategory CompileEqual() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.EQUALEQ;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.EQUAL;
        }
    }

    protected MheLexicalCategory CompileExclamation() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '=': return MheLexicalCategory.NOTEQUAL;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.NOT;
        }
    }

    protected MheLexicalCategory CompileBar() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '|': return MheLexicalCategory.ORLOG;
            case '=': return MheLexicalCategory.BAREQ;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.BAR;
        }
    }

    protected MheLexicalCategory CompileAmpersand() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '&': return MheLexicalCategory.ANDLOG;
            default :
                this.getStream().getBackCharacter();
                return MheLexicalCategory.AMPERSAND;
        }
    }

    protected MheLexicalCategory CompileCharacter() throws CompilerIOException{
        switch(this.getStream().getNextCharacter()){
            case '\\': return this.CompileCharA();
            case '\n':
            case '\r':
            case '\'':
            case '\"': return MheLexicalCategory.ERROR;
            default  : return this.CompileCharD();
        }
    }

    protected MheLexicalCategory CompileCharA() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '0' :
            case '1' :
            case '2' :
            case '3' : return this.CompileCharB();
            case '4' :
            case '5' :
            case '6' :
            case '7' : return this.CompileCharC();
            case 'n' :
            case 'r' :
            case '\\':
            case '\'':
            case '\"': return this.CompileCharD();
            default  : return MheLexicalCategory.ERROR;

        }
    }

    protected MheLexicalCategory CompileCharB() throws CompilerIOException {
        switch(this.getStream().getNextCharacter()){
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' : return this.CompileCharC();
            case '\'': return MheLexicalCategory.CHARACTER;
            default  : return MheLexicalCategory.ERROR;
        }
    }

    protected MheLexicalCategory CompileCharC() throws CompilerIOException{
        switch(this.getStream().getNextCharacter()){
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' : return this.CompileCharD();
            case '\'': return MheLexicalCategory.CHARACTER;
            default  : return MheLexicalCategory.ERROR;
        }
    }

    protected MheLexicalCategory CompileCharD() throws CompilerIOException{
        switch(this.getStream().getNextCharacter()){
            case '\'': return MheLexicalCategory.CHARACTER;
            default  : return MheLexicalCategory.ERROR;
        }
    }

    protected MheLexicalCategory CompileWord() throws CompilerIOException {
        char c;
        do {
            c = this.getStream().getNextCharacter();
        } while(isLetter(c) || isNumber(c));
        this.getStream().getBackCharacter();
        return FindReserved(this.getStream().getLexeme());
    }

    protected MheLexicalCategory CompileNumber() throws CompilerIOException {
        MheLexicalCategory r;
        char c;
        do {
            c = this.getStream().getNextCharacter();
        } while(isNumber(c));
        if(c == '.'){
            do {
                c = this.getStream().getNextCharacter();
            } while(isNumber(c));
            r = MheLexicalCategory.DECIMAL;
        } else {
            r = MheLexicalCategory.INTEGER;
        }
        this.getStream().getBackCharacter();
        return r;
    }

    protected MheLexicalCategory FindReserved(String s) {
        if(s == null) {
            return MheLexicalCategory.ERROR;
        } else if(s.compareTo("token") == 0) {
            return MheLexicalCategory.TOKEN;
        } else if(s.compareTo("exit") == 0) {
            return MheLexicalCategory.EXIT;
        } else if(s.compareTo("show") == 0) {
            return MheLexicalCategory.SHOW;
        } else if(s.compareTo("load") == 0) {
            return MheLexicalCategory.LOAD;
        } else if(s.compareTo("save") == 0) {
            return MheLexicalCategory.SAVE;
        } else if(s.compareTo("list") == 0) {
            return MheLexicalCategory.LIST;
        } else if(s.compareTo("test") == 0) {
            return MheLexicalCategory.TEST;
        } else if(s.compareTo("return") == 0) {
            return MheLexicalCategory.RETURN;
        } else {
            return MheLexicalCategory.IDENTIFIER;
        }
    }

    protected MheLexicalCategory CompileString() throws CompilerIOException {
        char c;
        do{
            c = this.getStream().getNextCharacter();
            if(c == '\\') {
                this.getStream().getNextCharacter();
            }
        }
        while(c != '\"' && c > 0 && c != this.getStream().getEndCategory());
        return (c > 0 && c != this.getStream().getEndCategory()) ? MheLexicalCategory.STRING : MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory CompileUniComm() throws CompilerIOException {
        char c;
        do {
            c = this.getStream().getNextCharacter();
        } while(c != '\n' && c > 0 && c != this.getStream().getEndCategory());
        return (c > 0 && c != this.getStream().getEndCategory()) ? MheLexicalCategory.SKIP : MheLexicalCategory.ERROR;
    }

    protected MheLexicalCategory CompileMultiCommA() throws CompilerIOException{
        char c;
        do {
            c = this.getStream().getNextCharacter();
        } while(c != '*' && c > 0 && c != this.getStream().getEndCategory());
        if(c == this.getStream().getEndCategory()) {
            return MheLexicalCategory.ERROR;
        } else {
            switch(c){
                case '*': return CompileMultiCommB();
                default:  return MheLexicalCategory.ERROR;
            }
        }
    }

    protected MheLexicalCategory CompileMultiCommB() throws CompilerIOException{
        char c;
        do {
            c = this.getStream().getNextCharacter();
        } while(c == '*' && c > 0 && c != this.getStream().getEndCategory());
        if(c == this.getStream().getEndCategory()) {
            return MheLexicalCategory.ERROR;
        } else {
            switch(c){
                case '/': return MheLexicalCategory.SKIP;
                default: return c > 0 ? CompileMultiCommA() : MheLexicalCategory.ERROR;
            }
        }
    }


    public static void main(String[] args) {
        // Variables auxiliares
        Token<MheLexicalCategory> t;
        String i = " 87.21 token id , : /*  wed  **/ 'i' \"hol\" 5  sdg -> <- ! ?";
        //String o = "salida.txt";

        try {

            Stream stream = new AbstractStream(new StringReader(i), new DefaultLogger());

            Lexer<MheLexicalCategory> lexer = new MheLexer(stream);

            t = lexer.getNextToken();

            while(t.getCategory() != MheLexicalCategory.END && t.getCategory() != MheLexicalCategory.ERROR){
                System.out.println(t);
                t = lexer.getNextToken();
            }

            System.out.println("t = " + t);
        }
        catch (CompilerIOException e) {
            e.printStackTrace();
        }
    }
}
