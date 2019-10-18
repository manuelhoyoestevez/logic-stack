package mhe.compiler.logic.ast;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTexit extends AST{

    public ASTexit() {
        super(LogicSemanticCategory.EXITLOGI, true, null);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "exit";
    }

    @Override
    public String getColor() {
        return "cyan";
    }
}
