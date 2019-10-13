package mhe.compiler.logic.ast;

public class ASTid extends AST {

    public ASTid(String n) {
        super(LITLOGI, true, n);
    }

    @Override
    public String getShape() {
        return quotify("square");
    }

    @Override
    public String getLabel() {
        return quotify(this.getName());
    }

    @Override
    public String getColor() {
        return quotify("blue");
    }

    @Override
    public String toJson() {
        return literalJson(this.getName());
    }
}
