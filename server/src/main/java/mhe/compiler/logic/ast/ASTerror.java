package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTerror extends AST{

    public ASTerror() {
        super(LogicSemanticCategory.ERRORLOGI, false, null);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "ERROR";
    }

    @Override
    public String getColor() {
        return "red";
    }
}
