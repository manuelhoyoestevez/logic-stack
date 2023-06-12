package com.mhe.dev.logic.stack.core.compiler.model;

import com.mhe.dev.logic.stack.core.compiler.exception.CompilerException;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;

/**
 * Lexical analyzer interface.
 *
 * @param <C> Lexical category
 * @author Manuel Hoyo Estévez
 */
public interface Lexer<C>
{

    /**
     * Categoría léxica predefinida para tokens irrelevantes.
     */
    C getSkipCategory();

    /**
     * Categoría del siguiente token.
     * Lee el flujo de entrada hasta reconocer un nuevo token.
     *
     * @return Categoría del siguiente token en el flujo de entrada
     * @throws CompilerIoException si existen errores de entrada / salida
     */
    C getNextTokenCategory() throws CompilerIoException;

    /**
     * Estructura del siguiente token: Lee el flujo de entrada hasta reconocer un nuevo token.
     *
     * @return Estructura con todos los datos del token leído
     * @throws CompilerIoException si existen errores de entrada / salida
     */
    Token<C> getNextToken() throws CompilerIoException;

    /**
     * Estructura del último token leído.
     *
     * @return Estructura con todos los datos del último token leído
     */
    Token<C> getCurrentToken();

    /**
     * Stream para recuperar caracteres.
     *
     * @return Stream para recuperar caracteres
     */
    Stream getStream();

    /**
     * Emparejar Token: Comprobar que la categoría del token actual se corresponde con la categoría del token pasada.
     *
     * @param tokenCategory Categoría del token a emparejar
     * @throws CompilerException si hay errores
     */
    void matchToken(C tokenCategory) throws CompilerException;
}
