package mhe.compiler;

import java.util.Collection;

public interface SymbolInterface extends Comparable<SymbolInterface> {
	public String getName();
	public SymbolType getType();

	public ASTInterface getAST();
	public SymbolInterface setAST(ASTInterface ast);

	public Collection<TokenInterface> getTokens();
	public SymbolInterface addToken(TokenInterface token);
}
