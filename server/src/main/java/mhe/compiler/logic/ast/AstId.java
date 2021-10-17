package mhe.compiler.logic.ast;

import java.util.List;

import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstId extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    private final String name;

    public AstId(String name) {
        super(LogicSemanticCategory.LITLOGI);
        this.name = name;
    }

    @Override
    public String getShape() {
        return quote("square");
    }

    @Override
    public String getLabel() {
        return quote(name);
    }

    @Override
    public String getColor() {
        return quote("blue");
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return literalJson(name);
    }
}