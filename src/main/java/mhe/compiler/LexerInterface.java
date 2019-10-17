package mhe.compiler;

import java.io.IOException;

import mhe.compiler.exception.CompilerException;
import mhe.compiler.exception.CompilerIOException;

/** Interface que define las operaciones de un analizador léxico
 * @author Manuel Hoyo Estévez
 */
public interface LexerInterface<C> extends Loggable {

    /** Categoría léxica predefinida para errores */
    public C getErrorCategory();

    /** Categoría léxica predefinida para tokens irrelevantes */
    public C getSkipCategory();

    /** Stream para recuperar catacteres
     * @return Stream para recuperar catacteres
     */
    public StreamInterface getStream();

    /** Categoría del siguiente token
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Categoría del siguiente token en el flujo de entrada
     * @throws IOException
     */
    public C getNextTokenCategory() throws CompilerIOException;

    /** Categoría del último token leído
     * @return Categoría del último token en el flujo de entrada
     */
    public C getCurrentTokenCategory();

    /** Estructura del siguiente token: <br>
     * Lee el flujo de entrada hasta reconocer un nuevo token
     * @return Estructura con todos los datos del token leido
     * @throws IOException
     */
    public TokenInterface<C> getNextToken() throws CompilerIOException;

    /** Estructura del último token leído
     * @return Estructura con todos los datos del último token leído
     */
    public TokenInterface<C> getCurrentToken();

    /** Emparejar Token:<br>
     * Comprobar que la categoría del token actual se corresponde con la
     * categoría del token pasada.
     * @param  token Categoría del token a emparejar
     * @return Categoría del siguiente token
     * @throws Exception si hay errores
     */
    public C matchToken(C token) throws CompilerException;
}
