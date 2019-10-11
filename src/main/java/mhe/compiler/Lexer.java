package mhe.compiler;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.LogType;

/** Clase abstracta que implmenta al interface LexicalAnalyzer
 * @author Manuel Hoyo Estévez
 */
public abstract class Lexer implements LexerInterface {
	/** */
	private StreamInterface stream = null;

	/** Identificador del último token leído */
	protected int currenttokencat;

	/** Reconocedor de tokens: <br>
	 * Inicia el automata programado que implementarán las clases derivadas
	 * para obtener los tokens
	 * @return identificador de la categoría léxica del siguiente token leído
	 * en el flujo de entrada
	 */
	protected abstract int compileToken() throws CompilerIOException;

	public Lexer(StreamInterface stream) {
		this.setStream(stream);
	}

	@Override
	public StreamInterface getStream() {
		return stream;
	}

	public LexerInterface setStream(StreamInterface stream) {
		this.stream = stream;
		return this;
	}

	@Override
	public LoggerInterface getLogger() {
		return this.stream != null ? this.stream.getLogger() : null;
	}

	@Override
	public int getErrorCategory() {
		return -1;
	}

	@Override
	public int getSkipCategory() {
		return 0;
	}

	/** carácter filtrable: <br>
	 * Determina si un carácter es filtrable
	 * @param a carácter
	 * @return true si es filtrable
	 */
	public static boolean isNull(char a){
        return a == '\t' || a == ' ' || a == 0 || a == '\n';
    }

	/** carácter numérico: <br>
	 * Determina si un carácter es numérico
	 * @param a carácter
	 * @return true si es numérico
	 */
    public static boolean isNumber(char a){
        return a >= '0' && a <= '9';
    }

    /** carácter alfabético: <br>
	 * Determina si un carácter es alfabético
	 * @param a carácter
	 * @return true si es alfabético
	 */
    public static boolean isLetter(char a){
        return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z');
    }

	@Override
	public int matchToken(int t) throws CompilerException {
		int r = this.getErrorCategory();
		String a = this.getStream().getLexeme();
		if(this.currenttokencat != t) {
			this.getLogger().logError(
					LogType.LEXICAL,
					this.getCurrentToken(),
					"Error sintáctico: Se esperaba token " + t +
						" en lugar de  " + this.getCurrentToken() + ": " + a + "."
			);
		}
		else{
			r = this.currenttokencat = this.getNextTokenCategory();
			this.getLogger().logMessage(
					LogType.LEXICAL,
					this.getCurrentToken(),
					"matchToken(" + t + ": " + a + ") reconocido el token " + t +
						" correctamente. El siguiente token es el " +
					this.currenttokencat + ": " + this.getStream().getLexeme() + "."
			);
		}
		return r;
	}

	@Override
	public int getCurrentTokenCategory() {
		return this.currenttokencat;
	}

	@Override
	public TokenInterface getCurrentToken() {
		return new Token(
				this.currenttokencat,
				this.getStream().getLexeme(),
				this.getStream().getRowNumber(),
				this.getStream().getColNumber()
		);
	}

	@Override
	public TokenInterface getNextToken() throws CompilerIOException {
		this.getNextTokenCategory();
		return this.getCurrentToken();
	}

	@Override
	public int getNextTokenCategory() throws CompilerIOException {
		this.getStream().resetLexeme();
		this.currenttokencat = this.compileToken();

		while(this.currenttokencat == this.getSkipCategory()){

			this.getLogger().logMessage(
					LogType.LEXICAL,
					this.getCurrentToken(),
					"Token omitido: " + this.getCurrentToken()
			);

			this.getStream().resetLexeme();
			this.currenttokencat = this.compileToken();
		}

		this.getLogger().logMessage(
				LogType.LEXICAL,
				this.getCurrentToken(),
				"Token obtenido: " + this.getCurrentToken()
		);

		return this.currenttokencat;
	}
}
