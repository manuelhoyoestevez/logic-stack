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
	 * @return Categoría léxica
	 */
	C getCategory();

	/**
	 * Obtener Número de fila
	 *
	 * @return Número de fila
	 */
	int getRow();

	/**
	 * Obtener Número de columna
	 *
	 * @return Número de columna
	 */
	int getCol();

	/**
	 * Obtener lexema
	 *
	 * @return Lexema
	 */
	String getLexeme();
}
