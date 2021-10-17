package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstConst extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {
    private final boolean value;
    private final String name;

    public AstConst(boolean value) {
        super(LogicSemanticCategory.CONSTLOGI);
        this.value = value;
        this.name = value ? "1" : "0";
    }

    @Override
    public String getShape() {
        return "square";
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getColor() {
        return "red";
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return constJson(this.getValue());
    }

    public boolean getValue() {
        return value;
    }
}
