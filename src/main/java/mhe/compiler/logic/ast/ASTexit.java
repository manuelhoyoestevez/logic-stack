package mhe.compiler.logic.ast;

public class ASTexit extends AST{

    public ASTexit() {
        super(EXITLOGI, true, null);
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
