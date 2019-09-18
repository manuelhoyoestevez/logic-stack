package mhe.compiler.logic.ast;

public class ASTlambda extends AST{

	public ASTlambda() {
		super(LAMBDALOGI, false, null);
	}
	
	@Override
	public boolean isLambda(){
		return true;
	}

	@Override
	public String getShape() {
		return "square";
	}

	@Override
	public String getLabel() {
		return "ASTlambda .";
	}

	@Override
	public String getColor() {
		return "black";
	}
}
