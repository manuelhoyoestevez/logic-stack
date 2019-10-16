package mhe.compiler;

import java.io.IOException;
import java.io.Reader;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;

/** Encargado de iterar los caracteres del flujo de entrada,
 * contar filas y columnas, y acumular la cadena leída.
 *
 * @author Manuel Hoyo Estévez
 */
public interface StreamInterface extends Loggable {

    /** Carácter inidicador de error */
    public char getErrorCategory();

    /** Carácter indicador de fin de cadena */
    public char getEndCategory();

    /**  Get Input Stream
     * @return Input Stream to get
     */
    public Reader getReader();

    /** Indiciador de finalización: <br>
     * @return <b>true</b> si el puntero de lectura ha llegado al final
     * del flujo de entrada
     */
    public boolean isFinished();

    /** Indicador del número de fila: <br>
     * Determina la posición vertical en la que se encuentra el puntero
     * de lectura dentro del flujo de entrada
     * @returns número de columna
     */
    public int getRowNumber();

    /** Indicador del número de columna: <br>
     * Determina la posición horizontal en la que se encuentra el puntero
     * de lectura dentro del flujo de entrada
     * @returns número de columna
     */
    public int getColNumber();

    /** Carácter actual: <br>
     * Esta acción no avanza el puntero de lectura del flujo de entrada
     * @return Carácter que se encuentra en la posición del puntero
     */
    public char getCurrentCharacter();

    /** Carácter siguiente: <br>
     * Establece el nuevo Carácter actual leyendo del flujo de entrada
     * y avanza el puntero de lectura una posición
     * @return Carácter siguiente en el flujo de entrada
     * @throws IOException
     */
    public char getNextCharacter() throws CompilerIOException;

    /** Emparejar carácter: <br>
     * Comprueba si el carácter pasado es igual al actual.
     * Si lo es avanza el puntero de lectura una posición en el flujo de entrada
     * y establece el nuevo carácter actual. <br>
     * Si no lo es, no avanza el puntero y de vuelve la constante CHRERROR
     * @param c Carácter que debe ser igual al actual
     * @return Carácter siguiente en el flujo de entrada o CHRERROR
     * @throws Exception
     */
    public char matchCharacter(char c) throws CompilerException;

    /** Retroceder un posición: <br>
     Retrocede una posición en el flujo de entrada
     * @return El caracter anterior
     * @throws IOException
     */
    public char getBackCharacter() throws CompilerIOException;

    /** Resetear buffer: <br>
     * Limpia el buffer de caracteres
     */
    public void resetLexeme();

    /** Cadena acumulada en buffer: <br>
     * Conforme el puntero de lecura va avanzando, se van acumulando caracteres en
     * el buffer. Este método muestra el contenido de ese buffer
     * @return String con el contenido del buffer.
     */
    public String getLexeme();
}
