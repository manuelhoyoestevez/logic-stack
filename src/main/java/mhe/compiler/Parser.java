package mhe.compiler;

public abstract class Parser implements ParserInterface {

	private LexerInterface lexer;
	
	public Parser(LexerInterface lexer) {
		this.setLexer(lexer);
	}
	
	@Override
	public LexerInterface getLexer() {
		return this.lexer;
	}
	
	public ParserInterface setLexer(LexerInterface lexer) {
		this.lexer = lexer;
		return this;
	}

	@Override
	public LoggerInterface getLogger() {
		return this.lexer != null ? this.lexer.getLogger() : null;
	}
}
