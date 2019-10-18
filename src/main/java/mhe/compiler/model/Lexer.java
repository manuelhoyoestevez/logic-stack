package mhe.compiler.model;

import java.io.IOException;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;

/**
 * Lexical analizer interface
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <C> Lexical category
 */
public interface Lexer<C> extends Loggable {

    /** Categoría léxica predefinida para errores */
    C getErrorCategory();

    /** Categoría léxica predefinida para tokens irrelevantes */
    C getSkipCategory();

    /** Categoría del siguiente token
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Categoría del siguiente token en el flujo de entrada
     * @throws IOException
     */
    C getNextTokenCategory() throws CompilerIOException;

    /** Estructura del siguiente token: <br>
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Estructura con todos los datos del token leido
     * @throws IOException
     */
    Token<C> getNextToken() throws CompilerIOException;

    /** Estructura del último token leído
     * @return Estructura con todos los datos del último token leído
     */
    Token<C> getCurrentToken();

    /** Stream para recuperar catacteres
     * @return Stream para recuperar catacteres
     */
    Stream getStream();

    /** Emparejar Token:<br>
     * Comprobar que la categoría del token actual se corresponde con la
     * categoría del token pasada.
     * @param  tokenCategory Categoría del token a emparejar
     * @return Categoría del siguiente token
     * @throws Exception si hay errores
     */
    C matchToken(C tokenCategory) throws CompilerException;
}
