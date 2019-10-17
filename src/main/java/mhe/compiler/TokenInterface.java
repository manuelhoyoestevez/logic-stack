package mhe.compiler;

/** Estructura de datos que almacena la información de un token
 * @author Manuel Hoyo Estévez
 */
public interface TokenInterface<C> {
	/** Obtener Categoría léxica */
	public C getCategory();

	/** Obtener Número de fila */
	public int getRow();

	/** Obtener Número de columna */
	public int getCol();

	/** Obtener lexema */
	public String getLexeme();
}
