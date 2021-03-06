package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTsave extends AST{

    public ASTsave(String n) {
        super(LogicSemanticCategory.SAVELOGI, true, n);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "save " + this.getName();
    }

    @Override
    public String getColor() {
        return "purple";
    }

}
