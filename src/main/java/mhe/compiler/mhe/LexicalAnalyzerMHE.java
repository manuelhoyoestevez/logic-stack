package mhe.compiler.mhe;

import java.io.*;

import mhe.compiler.*;

public class LexicalAnalyzerMHE extends Lexer implements LexicalCategoryMHE{
	
	public LexicalAnalyzerMHE(StreamInterface stream) {
		super(stream);
	}
	
	@Override
	public int getErrorCategory() {
		return ERROR;
	}

	@Override
	public int getSkipCategory() {
		return SKIP;
	}

	@Override
	protected int compileToken() throws IOException{
		char c = this.getStream().getNextCharacter();
		
		if(isLetter(c)) {
			return CompileWord();
		}
		else if(isNumber(c)) {
			return CompileNumber();
		}
		else if(c == this.getStream().getErrorCategory()) {
			return ERROR;
		}
		else if(c == this.getStream().getEndCategory()) {
			 return END;
		}
		else switch(c){
			case '\n': 
			case '\t': 
			case '\r': 
			case ' ' : return SKIP;
			case '.' : return POINT;
			case ',' : return COLON;
			case ':' : return TWOPOINT;
			case ';' : return SEMICOLON;
			case '(' : return LPAREN;
			case ')' : return RPAREN;
			case '[' : return LCORCH;
			case ']' : return RCORCH;
			case '{' : return LKEY;
			case '}' : return RKEY;
			case '%' : return PERCENT;
			case '?' : return HOOK;
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
			default  : return ERROR;
		}
	}
	
	protected int CompilePlus() throws IOException{
		switch(this.getStream().getNextCharacter()){
			case '=': return PLUSEQ;
			case '+': return INC;
			default :
				this.getStream().getBackCharacter();
				return PLUS;
		}
	}
	
	protected int CompileMinus() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return MINUSEQ;
			case '-': return DEC;
			case '>': return IMPLRIGHT;
			default :
				this.getStream().getBackCharacter();
				return MINUS;
		}
	}
	
	protected int CompileStar() throws IOException{	
		switch(this.getStream().getNextCharacter()){
			case '=': return STAREQ;
			case '*': return POW;
			default :
				this.getStream().getBackCharacter();
				return STAR;
		}
	}
	
	protected int CompileDiv() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return DIVEQ;
			case '*': return CompileMultiCommA();
			case '/': return CompileUniComm();
			default :
				this.getStream().getBackCharacter();
				return DIV;
		}
	}

	protected int CompileBigger() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return BIGGEREQ;
			case '>': return MOVERIGHT;
			default :
				this.getStream().getBackCharacter();
				return BIGGER;
		}
	}

	protected int CompileSmaller() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return SMALLEREQ;
			case '<': return MOVELEFT;
			case '>': return IMPLDOUBLE;
			default :
				this.getStream().getBackCharacter();
				return SMALLER;
		}
	}

	protected int CompileEqual() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return EQUALEQ;
			default :
				this.getStream().getBackCharacter();
				return EQUAL;
		}
	}

	protected int CompileExclamation() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '=': return NOTEQUAL;
			default :
				this.getStream().getBackCharacter();
				return NOT;
		}
	}

	protected int CompileBar() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '|': return ORLOG;
			case '=': return BAREQ;
			default :
				this.getStream().getBackCharacter();
				return BAR;
		}
	}

	protected int CompileAmpersand() throws IOException {
		switch(this.getStream().getNextCharacter()){
			case '&': return ANDLOG;
			default :
				this.getStream().getBackCharacter();
				return AMPERSAND;
		}
	}

	protected int CompileCharacter() throws IOException{
		switch(this.getStream().getNextCharacter()){		
			case '\\': return this.CompileCharA();
			case '\n':
			case '\r':
			case '\'':
			case '\"': return ERROR;
			default  : return this.CompileCharD();
		}
	}
	
	protected int CompileCharA() throws IOException {
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
			default  : return ERROR;
				
		}
	}
	
	protected int CompileCharB() throws IOException{
		switch(this.getStream().getNextCharacter()){
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' : return this.CompileCharC();
			case '\'': return CHARACTER;
			default  : return ERROR;
		}
	}
	
	protected int CompileCharC() throws IOException{
		switch(this.getStream().getNextCharacter()){
			case '0' :
			case '1' :
			case '2' :
			case '3' :
			case '4' :
			case '5' :
			case '6' :
			case '7' : return this.CompileCharD();
			case '\'': return CHARACTER;
			default  : return ERROR;
		}
	}
	
	protected int CompileCharD() throws IOException{
		switch(this.getStream().getNextCharacter()){
			case '\'': return CHARACTER;
			default  : return ERROR;
		}
	}
	
	protected int CompileWord() throws IOException {
		char c;		
		do c = this.getStream().getNextCharacter();
		while(isLetter(c) || isNumber(c));
		this.getStream().getBackCharacter();
		return FindReserved(this.getStream().getLexeme());
	}

	protected int CompileNumber() throws IOException {
		int r;
		char c;
		do c = this.getStream().getNextCharacter();
		while(isNumber(c));
		if(c == '.'){
			do c = this.getStream().getNextCharacter();
			while(isNumber(c));
			r = DECIMAL;
		}
		else
			r = INTEGER;
		this.getStream().getBackCharacter();
		return r;
	}

	protected int FindReserved(String s) {
		if(s == null)
			return ERROR;
		else if(s.compareTo("token") == 0)
			return TOKEN;
		else if(s.compareTo("exit") == 0)
			return EXIT;
		else if(s.compareTo("show") == 0)
			return SHOW;
		else if(s.compareTo("load") == 0)
			return LOAD;
		else if(s.compareTo("save") == 0)
			return SAVE;
		else if(s.compareTo("list") == 0)
			return LIST;
		else if(s.compareTo("test") == 0)
			return TEST;
		else if(s.compareTo("return") == 0)
			return RETURN;
		else
			return IDENTIFIER;
	}
	
	protected int CompileString() throws IOException {
		char c;		
		do{
			c = this.getStream().getNextCharacter();
			if(c == '\\')
				this.getStream().getNextCharacter();
		}
		while(c != '\"' && c > 0 && c != this.getStream().getEndCategory());		
		return (c > 0 && c != this.getStream().getEndCategory()) ? STRING : ERROR;
	}

	protected int CompileUniComm() throws IOException {
		char c;
		do c = this.getStream().getNextCharacter();
		while(c != '\n' && c > 0 && c != this.getStream().getEndCategory());
		return (c > 0 && c != this.getStream().getEndCategory()) ? SKIP : ERROR;
	}
	
	protected int CompileMultiCommA() throws IOException{
		char c;
		do c = this.getStream().getNextCharacter();
		while(c != '*' && c > 0 && c != this.getStream().getEndCategory());		
		if(c == this.getStream().getEndCategory()) {
			return ERROR;
		}
		else switch(c){
			case '*': return CompileMultiCommB();
			default:  return ERROR;
		}
	}
	
	protected int CompileMultiCommB() throws IOException{
		char c;
		do c = this.getStream().getNextCharacter();
		while(c == '*' && c > 0 && c != this.getStream().getEndCategory());		
		if(c == this.getStream().getEndCategory()) {
			return ERROR;
		}
		else switch(c){
			case '/': return SKIP;
			default: return c > 0 ? CompileMultiCommA() : ERROR;
		}
	}
	
	
	public static void main(String[] args) {
		// Variables auxiliares
		TokenInterface t;
		String i = " 87.21 token id , : /*  wed  **/ 'i' \"hol\" 5  sdg -> <- ! ?";
		//String o = "salida.txt";

		try {
			
			StreamInterface stream = new Stream(new StringReader(i), new Logger());
			
			LexerInterface lexer = new LexicalAnalyzerMHE(stream);
			
			t = lexer.getNextToken();
			
			while(t.getCategory() != END && t.getCategory() != ERROR){
				System.out.println(t);
				t = lexer.getNextToken();
			}
			
			System.out.println("t = " + t);		
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
