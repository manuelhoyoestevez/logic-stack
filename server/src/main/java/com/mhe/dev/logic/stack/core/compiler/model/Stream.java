package com.mhe.dev.logic.stack.core.compiler.model;

import java.io.Reader;
import com.mhe.dev.logic.stack.core.compiler.exception.CompilerIoException;

/**
 * Encargado de iterar los caracteres del flujo de entrada, contar filas y columnas, y acumular la cadena leída.
 *
 * @author Manuel Hoyo Estévez
 */
public interface Stream {
    /**
     * Get Input Stream.
     *
     * @return Input Stream to get
     */
    Reader getReader();

    /**
     * Indiciador de finalización.
     *
     * @return true< si el puntero de lectura ha llegado al final del flujo de entrada
     */
    boolean isFinished();

    /**
     * Indicador del número de fila:  Determina la posición vertical en la que se encuentra el puntero de lectura
     * dentro del flujo de entrada.
     *
     * @return número de columna
     */
    int getRowNumber();

    /**
     * Indicador del número de columna: Determina la posición horizontal en la que se encuentra el puntero de lectura
     * dentro del flujo de entrada.
     *
     * @return número de columna
     */
    int getColNumber();

    /**
     * Carácter actual: Esta acción no avanza el puntero de lectura del flujo de entrada.
     *
     * @return Carácter que se encuentra en la posición del puntero
     */
    char getCurrentCharacter();

    /**
     * Carácter siguiente: Establece el nuevo Carácter actual leyendo del flujo de entrada y avanza el puntero de
     * lectura una posición.
     *
     * @return Carácter siguiente en el flujo de entrada
     * @throws CompilerIoException si hay errores de entrada / salida
     */
    char getNextCharacter() throws CompilerIoException;

    /**
     * Retroceder un posición: Retrocede una posición en el flujo de entrada.
     *
     * @return El carácter anterior
     * @throws CompilerIoException si hay errores de entrada / salida
     */
    char getBackCharacter() throws CompilerIoException;

    /**
     * Resetear buffer: Limpia el buffer de caracteres.
     */
    void resetLexeme();

    /**
     * Cadena acumulada en buffer: Conforme el puntero de lectura va avanzando, se van acumulando caracteres en
     * el buffer. Este método muestra el contenido de ese buffer.
     *
     * @return String con el contenido del buffer.
     */
    String getLexeme();
}
