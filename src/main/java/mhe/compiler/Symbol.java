package mhe.compiler;

import java.util.ArrayList;
import java.util.Collection;

public class Symbol implements SymbolInterface {
	private String name;
	
	private String type;
	
	private ASTInterface ast;
	
	private Collection<TokenInterface> tokens;
	
	public Symbol(String name, String type, ASTInterface ast) {
		this.setName(name);
		this.setType(type);
		this.setAST(ast);
		this.tokens = new ArrayList<TokenInterface>();
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public SymbolInterface setName(String name) {
		this.name = name;
		return this;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	@Override
	public SymbolInterface setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public ASTInterface getAST() {
		return this.ast;
	}
	
	@Override
	public SymbolInterface setAST(ASTInterface ast) {
		this.ast = ast;
		return this;
	}
	
	@Override
	public Collection<TokenInterface> getTokens(){
		return this.tokens;
	}
	
	@Override
	public SymbolInterface addToken(TokenInterface token) {
		this.tokens.add(token);
		return this;
	}
	
	@Override
	public int compareTo(SymbolInterface other) {
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
