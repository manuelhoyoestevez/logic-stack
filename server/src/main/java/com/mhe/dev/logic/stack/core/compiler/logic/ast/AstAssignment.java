package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.AbstractSyntaxTree;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;

/**
 * AstAssignment.
 */
public class AstAssignment extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory> {

    private final String id;

    /**
     * Constructor.
     *
     * @param id Variable
     * @param e Valor
     */
    public AstAssignment(String id, AbstractSyntaxTree<LogicSemanticCategory> e) {
        super(LogicSemanticCategory.ASIGLOGI);
        this.id = id;
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
        return "AstAssignment " + id;
    }

    @Override
    public String getColor() {
        return "orange";
    }
}
