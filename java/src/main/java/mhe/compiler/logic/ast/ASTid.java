package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;

public class ASTid extends AST {

    public ASTid(String n) {
        super(LogicSemanticCategory.LITLOGI, true, n);
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
    public String toJson(List<String> literalsOrder) {
        return literalJson(this.getName());
    }
}
