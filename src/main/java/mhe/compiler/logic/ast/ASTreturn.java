package mhe.compiler.logic.ast;

import mhe.compiler.ASTInterface;

public class ASTreturn extends AST {

    public ASTreturn() {
        super(RETURNLOGI, true, null);
    }

    public ASTreturn(ASTInterface e) {
        this();
        this.getChildren().add(e);
    }

    @Override
    public String toJson() {
        return this.getChildren().isEmpty() ? null : this.getFirstChild().toJson();
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
