package mhe.compiler;

/** Estructura de datos que almacena la información de un token
 * @author Manuel Hoyo Estévez
 */
public interface TokenInterface {
	/** Obtener Categoría léxica */
	public int getCategory();
	
	/** Obtener Número de fila */
	public int getRow();
	
	/** Obtener Número de columna */
	public int getCol();
	
	/** Obtener lexema */
	public String getLexeme();
}
