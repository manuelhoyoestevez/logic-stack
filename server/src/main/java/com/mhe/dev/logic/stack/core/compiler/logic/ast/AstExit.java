package com.mhe.dev.logic.stack.core.compiler.logic.ast;

import java.util.List;
import com.mhe.dev.logic.stack.core.compiler.logic.LogicSemanticCategory;
import com.mhe.dev.logic.stack.core.compiler.model.NoLambdaAbstractSyntaxTree;

/**
 * AstExit.
 */
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
