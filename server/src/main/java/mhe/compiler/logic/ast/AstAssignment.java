package mhe.compiler.logic.ast;

import java.util.List;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.AbstractSyntaxTree;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstAssignment extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    private final String name;

    public AstAssignment(String name, AbstractSyntaxTree<LogicSemanticCategory> e) {
        super(LogicSemanticCategory.ASIGLOGI);
        this.name = name;
        this.getChildren().add(e);
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return null;
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "AstAssignment " + name;
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
