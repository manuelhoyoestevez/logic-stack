package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTLambda extends AST {
    public final static ASTLambda LAMBDA = new ASTLambda();

    private ASTLambda() {
        super(LogicSemanticCategory.LAMBDALOGI, false, null);
    }

    @Override
    public boolean isNotLambda(){
        return false;
    }

    @Override
    public String getShape() {
        return "square";
    }

    @Override
    public String getLabel() {
        return "ASTLambda .";
    }

    @Override
    public String getColor() {
        return "black";
    }
}
