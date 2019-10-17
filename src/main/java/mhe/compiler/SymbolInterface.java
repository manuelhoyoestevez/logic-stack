package mhe.compiler;

import java.util.Collection;

public interface SymbolInterface<C> extends Comparable<SymbolInterface<C>> {
	public String getName();
	public SymbolType getType();

	public ASTInterface getAST();
	public SymbolInterface<C> setAST(ASTInterface ast);

	public Collection<TokenInterface<C>> getTokens();
	public SymbolInterface<C> addToken(TokenInterface<C> token);
}
