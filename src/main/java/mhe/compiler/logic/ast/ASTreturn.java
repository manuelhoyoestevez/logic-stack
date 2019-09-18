package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;
import mhe.compiler.logic.LogicNodeInterface;

public class ASTreturn extends AST {

	public ASTreturn() {
		super(RETURNLOGI, true, null);
	}
	
	public ASTreturn(ASTInterface e) {
		this();
		this.getChildren().add(e);
	}
	
	@Override
	public LogicNodeInterface getLogicNode() {
		return this.getChildren().isEmpty() ? null : this.getFirstChild().getLogicNode();
	}

	@Override
	public String getShape() {
		return "rectangle";
	}

	@Override
	public String getLabel() {
		return "ASTreturn";
	}

	@Override
	public String getColor() {
		return "orange";
	}
}
