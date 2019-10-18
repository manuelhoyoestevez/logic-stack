package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTvar extends AST {

    public ASTvar(String n) {
        super(LogicSemanticCategory.VARLOGI, true, n);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "var " + this.getName();
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
