package mhe.compiler.logic.ast;

public class ASTerror extends AST{

    public ASTerror() {
        super(ERRORLOGI, false, null);
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
