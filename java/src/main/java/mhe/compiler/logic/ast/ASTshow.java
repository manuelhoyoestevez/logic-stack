package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTshow extends AST{

    public ASTshow(String n) {
        super(LogicSemanticCategory.SHOWLOGI,true, n);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "show " + this.getName();
    }

    @Override
    public String getColor() {
        return "green";
    }
}
