package mhe.compiler.logic.ast;

import java.util.List;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

public class AstExit extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    public AstExit() {
        super(LogicSemanticCategory.EXITLOGI);
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
        return "exit";
    }

    @Override
    public String getColor() {
        return "cyan";
    }
}
