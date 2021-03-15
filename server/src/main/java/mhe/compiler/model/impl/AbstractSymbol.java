package mhe.compiler.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import mhe.compiler.model.AbstractSintaxTree;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.SymbolType;
import mhe.compiler.model.Token;

public class AbstractSymbol<C, T> implements Symbol<C, T> {
	private String name;

	private SymbolType type;

	private AbstractSintaxTree<T> ast;

	private Collection<Token<C>> tokens;

	public AbstractSymbol(String name, SymbolType type, AbstractSintaxTree<T> ast) {
		this.setName(name);
		this.setType(type);
		this.setAST(ast);
		this.tokens = new ArrayList<Token<C>>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Symbol<C, T> setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public SymbolType getType() {
		return this.type;
	}

	protected Symbol<C, T> setType(SymbolType type) {
		this.type = type;
		return this;
	}

	@Override
	public AbstractSintaxTree<T> getAST() {
		return this.ast;
	}

	@Override
	public Symbol<C, T> setAST(AbstractSintaxTree<T> ast) {
		this.ast = ast;
		return this;
	}

	@Override
	public Collection<Token<C>> getTokens(){
		return this.tokens;
	}

	@Override
	public Symbol<C, T> addToken(Token<C> token) {
		this.tokens.add(token);
		return this;
	}

	@Override
	public int compareTo(Symbol<C, T> other) {
		int ret = this.getName().compareTo(other.getName());
		if(ret == 0) {
			return this.hashCode() - other.hashCode();
		}
		else {
			return ret;
		}
	}

	@Override
	public String toString(){
		return this.getName() + ": " + this.getType(); //+ this.getAST().toString();
	}
}
