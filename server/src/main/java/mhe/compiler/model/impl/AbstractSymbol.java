package mhe.compiler.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.Symbol;
import mhe.compiler.model.SymbolType;
import mhe.compiler.model.Token;

public class AbstractSymbol<C, T> implements Symbol<C, T> {
	private final String name;

	private final SymbolType type;

	private AbstractSyntaxTree<T> ast;

	private final Collection<Token<C>> tokens;

	public AbstractSymbol(String name, SymbolType type, AbstractSyntaxTree<T> ast) {
		this.name = name;
		this.type = type;
		this.setAST(ast);
		this.tokens = new ArrayList<>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public SymbolType getType() {
		return this.type;
	}

	@Override
	public AbstractSyntaxTree<T> getAST() {
		return this.ast;
	}

	@Override
	public Symbol<C, T> setAST(AbstractSyntaxTree<T> ast) {
		this.ast = ast;
		return this;
	}

	@Override
	public Symbol<C, T> addToken(Token<C> token) {
		this.tokens.add(token);
		return this;
	}

	@Override
	public int compareTo(Symbol<C, T> other) {
		int ret = this.getName().compareTo(other.getName());

		if(ret != 0) {
			return ret;
		}

		return this.hashCode() - other.hashCode();
	}

	@Override
	public String toString(){
		return this.getName() + ": " + this.getType();
	}
}
