package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;

/**
 * AstReturn.
 */
public class AstReturn extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    public AstReturn(AbstractSyntaxTree<LogicSemanticCategory> e) {
        super(LogicSemanticCategory.RETURNLOGI);
        this.getChildren().add(e);
    }

    @Override
    public String toJson(List<String> literalsOrder) {
        return this.getChildren().isEmpty() ? null : this.getFirstChild().toJson(literalsOrder);
    }

    @Override
    public String getShape() {
        return "rectangle";
    }

    @Override
    public String getLabel() {
        return "return";
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
