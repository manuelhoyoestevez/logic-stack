package mhe.compiler;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.LogType;

/** Clase abstracta que implmenta al interface LexicalAnalyzer
 * @author Manuel Hoyo Estévez
 */
public abstract class Lexer<C> implements LexerInterface<C> {
    /** */
    private StreamInterface stream = null;

    /** Identificador del último token leído */
    protected C currenttokencat;

    /** Reconocedor de tokens: <br>
     * Inicia el automata programado que implementarán las clases derivadas
     * para obtener los tokens
     * @return identificador de la categoría léxica del siguiente token leído
     * en el flujo de entrada
     */
    protected abstract C compileToken() throws CompilerIOException;

    public Lexer(StreamInterface stream) {
        this.setStream(stream);
    }

    @Override
    public StreamInterface getStream() {
        return stream;
    }

    public LexerInterface<C> setStream(StreamInterface stream) {
        this.stream = stream;
        return this;
    }

    @Override
    public LoggerInterface getLogger() {
        return this.stream != null ? this.stream.getLogger() : null;
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
    public C matchToken(C t) throws CompilerException {
        C r = this.getErrorCategory();
        String a = this.getStream().getLexeme();
        if(this.currenttokencat != t) {
            this.getLogger().logError(
                    LogType.LEXICAL,
                    this.getCurrentToken().getRow(),
                    this.getCurrentToken().getCol(),
                    "Error sintáctico: Se esperaba token " + t +
                        " en lugar de  " + this.getCurrentToken() + ": " + a + "."
            );
        }
        else{
            r = this.currenttokencat = this.getNextTokenCategory();
            this.getLogger().logMessage(
                    LogType.LEXICAL,
                    this.getCurrentToken().getRow(),
                    this.getCurrentToken().getCol(),
                    "matchToken(" + t + ": " + a + ") reconocido el token " + t +
                        " correctamente. El siguiente token es el " +
                    this.currenttokencat + ": " + this.getStream().getLexeme() + "."
            );
        }
        return r;
    }

    @Override
    public C getCurrentTokenCategory() {
        return this.currenttokencat;
    }

    @Override
    public TokenInterface<C> getCurrentToken() {
        return new Token<C>(
                this.currenttokencat,
                this.getStream().getLexeme(),
                this.getStream().getRowNumber(),
                this.getStream().getColNumber()
        );
    }

    @Override
    public TokenInterface<C> getNextToken() throws CompilerIOException {
        this.getNextTokenCategory();
        return this.getCurrentToken();
    }

    @Override
    public C getNextTokenCategory() throws CompilerIOException {
        this.getStream().resetLexeme();
        this.currenttokencat = this.compileToken();

        while(this.currenttokencat == this.getSkipCategory()){

            this.getLogger().logMessage(
                    LogType.LEXICAL,
                    this.getCurrentToken().getRow(),
                    this.getCurrentToken().getCol(),
                    "Token omitido: " + this.getCurrentToken()
            );

            this.getStream().resetLexeme();
            this.currenttokencat = this.compileToken();
        }

        this.getLogger().logMessage(
                LogType.LEXICAL,
                this.getCurrentToken().getRow(),
                this.getCurrentToken().getCol(),
                "Token obtenido: " + this.getCurrentToken()
        );

        return this.currenttokencat;
    }
}
