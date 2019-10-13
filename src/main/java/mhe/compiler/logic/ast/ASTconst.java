package mhe.compiler.logic.ast;

public class ASTconst extends AST {

    public ASTconst(boolean v) {
        super(CONSTLOGI, v, v ? "1" : "0");
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
        return quotify("red");
    }

    @Override
    public String toJson() {
        return constJson(this.getValue());
    }

}
