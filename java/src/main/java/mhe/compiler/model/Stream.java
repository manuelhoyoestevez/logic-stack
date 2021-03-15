package mhe.compiler.model;

import java.io.Reader;

import mhe.compiler.exception.CompilerIOException;

/**
 * Encargado de iterar los caracteres del flujo de entrada, contar filas y
 * columnas, y acumular la cadena leída.
 *
 * @author Manuel Hoyo Estévez
 */
public interface Stream extends Loggable {
    /**
     * Get Input Stream
     *
     * @return Input Stream to get
     *
     */
    Reader getReader();

    /**
     * Indiciador de finalización: <br>
     * 
     * @return <b>true</b> si el puntero de lectura ha llegado al final del flujo de
     *         entrada
     */
    boolean isFinished();

    /**
     * Indicador del número de fila: <br>
     * Determina la posición vertical en la que se encuentra el puntero de lectura
     * dentro del flujo de entrada
     * 
     * @returns número de columna
     */
    int getRowNumber();

    /**
     * Indicador del número de columna: <br>
     * Determina la posición horizontal en la que se encuentra el puntero de lectura
     * dentro del flujo de entrada
     * 
     * @returns número de columna
     */
    int getColNumber();

    /**
     * Carácter actual: <br>
     * Esta acción no avanza el puntero de lectura del flujo de entrada
     * 
     * @return Carácter que se encuentra en la posición del puntero
     */
    char getCurrentCharacter();

    /**
     * Carácter siguiente: <br>
     * Establece el nuevo Carácter actual leyendo del flujo de entrada y avanza el
     * puntero de lectura una posición
     * 
     * @return Carácter siguiente en el flujo de entrada
     * @throws CompilerIOException
     */
    char getNextCharacter() throws CompilerIOException;

    /**
     * Retroceder un posición: <br>
     * Retrocede una posición en el flujo de entrada
     * 
     * @return El caracter anterior
     * @throws CompilerIOException
     */
    char getBackCharacter() throws CompilerIOException;

    /**
     * Resetear buffer: <br>
     * Limpia el buffer de caracteres
     */
    void resetLexeme();

    /**
     * Cadena acumulada en buffer: <br>
     * Conforme el puntero de lecura va avanzando, se van acumulando caracteres en
     * el buffer. Este método muestra el contenido de ese buffer
     * 
     * @return String con el contenido del buffer.
     */
    String getLexeme();
}
