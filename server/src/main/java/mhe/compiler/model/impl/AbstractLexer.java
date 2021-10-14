package mhe.compiler.model.impl;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;
import mhe.compiler.logger.MheLogger;
import mhe.compiler.logger.MheLoggerFactory;
import mhe.compiler.model.Lexer;
import mhe.compiler.model.Stream;
import mhe.compiler.model.Token;

/** Clase abstracta que implementa al interface LexicalAnalyzer
 * @author Manuel Hoyo Estévez
 */
public abstract class AbstractLexer<C> implements Lexer<C> {
    private static final MheLogger logger = MheLoggerFactory.getLogger(AbstractLexer.class);

    /** */
    private final Stream stream;

    /** Identificador del último token leído */
    protected C currentTokenCategory;

    /** Reconocedor de tokens: <br>
     * Inicia el automata programado que implementarán las clases derivadas
     * para obtener los tokens
     * @return identificador de la categoría léxica del siguiente token leído
     * en el flujo de entrada
     */
    protected abstract C compileToken() throws CompilerIOException;

    public AbstractLexer(Stream stream) {
        this.stream = stream;
    }

    @Override
    public Stream getStream() {
        return stream;
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
    public void matchToken(C t) throws CompilerException {
        String a = this.getStream().getLexeme();

        if (this.currentTokenCategory != t) {
            String message = "Error sintáctico: Se esperaba token " + t + " en lugar de  " + this.getCurrentToken() + ": " + a + ".";
            logger.error(this.getCurrentToken().getRow(), this.getCurrentToken().getCol(), message);
            throw new CompilerException(this.getCurrentToken().getRow(), this.getCurrentToken().getCol(), message, null);
        }

        this.currentTokenCategory = this.getNextTokenCategory();

        logger.lexer(
                this.getCurrentToken().getRow(),
                this.getCurrentToken().getCol(),
                "matchToken(" + t + ": " + a + ") reconocido el token " + t +
                    " correctamente. El siguiente token es el " +
                this.currentTokenCategory + ": " + this.getStream().getLexeme() + "."
        );
    }

    @Override
    public Token<C> getCurrentToken() {
        return new AbstractToken<>(
                this.currentTokenCategory,
                this.getStream().getLexeme(),
                this.getStream().getRowNumber(),
                this.getStream().getColNumber()
        );
    }

    @Override
    public Token<C> getNextToken() throws CompilerIOException {
        this.getNextTokenCategory();
        return this.getCurrentToken();
    }

    @Override
    public C getNextTokenCategory() throws CompilerIOException {
        this.getStream().resetLexeme();
        this.currentTokenCategory = this.compileToken();

        while(this.currentTokenCategory == this.getSkipCategory()){

            logger.lexer(
                    this.getCurrentToken().getRow(),
                    this.getCurrentToken().getCol(),
                    "Token omitido: " + this.getCurrentToken()
            );

            this.getStream().resetLexeme();
            this.currentTokenCategory = this.compileToken();
        }

        logger.lexer(
                this.getCurrentToken().getRow(),
                this.getCurrentToken().getCol(),
                "Token obtenido: " + this.getCurrentToken()
        );

        return this.currentTokenCategory;
    }
}
