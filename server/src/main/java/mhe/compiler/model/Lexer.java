package mhe.compiler.model;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;

/**
 * Lexical analyzer interface
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <C> Lexical category
 */
public interface Lexer<C> {

    /** Categoría léxica predefinida para tokens irrelevantes */
    C getSkipCategory();

    /** Categoría del siguiente token
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Categoría del siguiente token en el flujo de entrada
     * @throws CompilerIOException si existen errores de entrada / salida
     */
    C getNextTokenCategory() throws CompilerIOException;

    /** Estructura del siguiente token: <br>
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Estructura con todos los datos del token leído
     * @throws CompilerIOException si existen errores de entrada / salida
     */
    Token<C> getNextToken() throws CompilerIOException;

    /** Estructura del último token leído
     * @return Estructura con todos los datos del último token leído
     */
    Token<C> getCurrentToken();

    /** Stream para recuperar caracteres
     * @return Stream para recuperar caracteres
     */
    Stream getStream();

    /** Emparejar Token:<br>
     * Comprobar que la categoría del token actual se corresponde con la
     * categoría del token pasada.
     * @param  tokenCategory Categoría del token a emparejar
     * @throws CompilerException si hay errores
     */
    void matchToken(C tokenCategory) throws CompilerException;
}
