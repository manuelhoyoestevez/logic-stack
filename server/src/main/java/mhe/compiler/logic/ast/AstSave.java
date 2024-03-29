package mhe.compiler.logic.ast;

import java.util.List;
import mhe.compiler.logic.LogicSemanticCategory;
import mhe.compiler.model.NoLambdaAbstractSyntaxTree;

/**
 * AstSave.
 */
public class AstSave extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    private final String name;

    public AstSave(String name) {
        super(LogicSemanticCategory.SAVELOGI);
        this.name = name;
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
        return "save " + name;
    }

    @Override
    public String getColor() {
        return "purple";
    }

}
