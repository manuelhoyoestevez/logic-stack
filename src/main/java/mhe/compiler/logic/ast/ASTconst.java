package mhe.compiler.logic.ast;

import java.util.List;

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
    public String toJson(List<String> literalsOrder) {
        return constJson(this.getValue());
    }

}
