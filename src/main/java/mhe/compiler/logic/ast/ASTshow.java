package mhe.compiler.logic.ast;

public class ASTshow extends AST{

    public ASTshow(String n) {
        super(SHOWLOGI,true, n);
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
