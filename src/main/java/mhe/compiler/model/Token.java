package mhe.compiler.model;

/**
 * Token data structure
 *
 * @author Manuel Hoyo Estévez
 *
 * @param <C> Lexical category
 */
public interface Token<C> {
	/**
	 * Obtener Categoría léxica
	 *
	 * @return
	 */
	public C getCategory();

	/**
	 * Obtener Número de fila
	 *
	 * @return Número de fila
	 */
	public int getRow();

	/**
	 * Obtener Número de columna
	 *
	 * @return Número de columna
	 */
	public int getCol();

	/**
	 * Obtener lexema
	 *
	 * @return Lexeema
	 */
	public String getLexeme();
}
