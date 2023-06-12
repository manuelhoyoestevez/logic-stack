package com.mhe.dev.compiler.lib.core;

/**
 * Clase abstracta que implementa al interface Lexer.
 *
 * @author Manuel Hoyo Estévez
 */
public abstract class LexerAbstract<C> implements Lexer<C>
{
    /**
     * MheLogger.
     */
    private final MheLogger logger;

    /**
     * Stream.
     */
    private final Stream stream;

    /**
     * Identificador del último token leído.
     */
    protected C currentTokenCategory;

    public LexerAbstract(MheLogger logger, Stream stream)
    {
        this.logger = logger;
        this.stream = stream;
    }

    /**
     * Determina si un carácter es numérico.
     *
     * @param a carácter
     * @return true si es numérico
     */
    public static boolean isNumber(char a)
    {
        return a >= '0' && a <= '9';
    }

    /**
     * Determina si un carácter es alfabético.
     *
     * @param a carácter
     * @return true si es alfabético
     */
    public static boolean isLetter(char a)
    {
        return (a >= 'a' && a <= 'z') || (a >= 'A' && a <= 'Z');
    }

    /**
     * Inicia el automata programado que implementarán las clases derivadas para obtener los tokens.
     *
     * @return identificador de la categoría léxica del siguiente token leído en el flujo de entrada.
     */
    protected abstract C compileToken() throws CompilerIoException;

    @Override
    public Stream getStream()
    {
        return stream;
    }

    @Override
    public void matchToken(C t) throws CompilerException
    {
        String a = getStream().getLexeme();

        if (currentTokenCategory != t)
        {
            String message = "Error sintáctico: Se esperaba token " + t + " en lugar de  " + getCurrentToken() + ": "
                + a + ".";
            logger.error(getCurrentToken().getRow(), getCurrentToken().getCol(), message);
            throw new CompilerException(getCurrentToken().getRow(), getCurrentToken().getCol(), message, null);
        }

        currentTokenCategory = getNextTokenCategory();

        logger.lexer(
            getCurrentToken().getRow(),
            getCurrentToken().getCol(),
            "matchToken(" + t + ": " + a + ") reconocido el token "
                + t + " correctamente. El siguiente token es el "
                + currentTokenCategory + ": " + getStream().getLexeme() + "."
        );
    }

    @Override
    public Token<C> getCurrentToken()
    {
        return new TokenImpl<>(
            currentTokenCategory,
            getStream().getLexeme(),
            getStream().getRowNumber(),
            getStream().getColNumber()
        );
    }

    @Override
    public Token<C> getNextToken() throws CompilerIoException
    {
        getNextTokenCategory();
        return getCurrentToken();
    }

    @Override
    public C getNextTokenCategory() throws CompilerIoException
    {
        getStream().resetLexeme();
        currentTokenCategory = compileToken();

        while (currentTokenCategory == getSkipCategory())
        {

            logger.lexer(
                getCurrentToken().getRow(),
                getCurrentToken().getCol(),
                "Token omitido: " + getCurrentToken()
            );

            getStream().resetLexeme();
            currentTokenCategory = compileToken();
        }

        logger.lexer(
            getCurrentToken().getRow(),
            getCurrentToken().getCol(),
            "Token obtenido: " + getCurrentToken()
        );

        return currentTokenCategory;
    }
}
