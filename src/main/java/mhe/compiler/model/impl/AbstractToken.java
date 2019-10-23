package mhe.compiler.model.impl;

import mhe.compiler.model.Token;

/** Implementación de la estructura de datos que almacena la información de un token
 * @author Manuel Hoyo Estévez
 */
public class AbstractToken<C> implements Token<C> {

	/** Categoría léxica */
	protected C cat;
	/** Número de fila */
	protected int row;
	/** Número de columna */
	protected int col;
	/** Lexema */
	protected String lex;

	/** Constructor
	 * @param t Categoría léxica
	 * @param l Lexemna
	 * @param r Número de fila
	 * @param c Número de columna
	 */
	public AbstractToken(C t, String l, int r, int c) {
		this.cat = t;
		this.lex = l;
		this.col = c;
		this.row = r;
	}

	@Override
	public C getCategory(){
		return this.cat;
	}

	@Override
	public int getRow(){
		return this.row;
	}

	@Override
	public int getCol(){
		return this.col;
	}

	@Override
	public String getLexeme(){
		return this.lex;
	}

	@Override
	public String toString(){
		return "{ " + this.cat + ": '" + this.lex + "' }[" + this.row + ", " + this.col + "]";
	}
}
