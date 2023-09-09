package com.mhe.dev.compiler.logic.core.compiler.ast;

import com.mhe.dev.compiler.logic.core.compiler.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.LogicSemanticCategory;
import java.util.List;

/**
 * AstReturn.
 */
public class AstReturn extends Ast
{
    public AstReturn(AbstractSyntaxTree<LogicSemanticCategory> e)
    {
        super(LogicSemanticCategory.RETURNLOGI);
        this.getChildren().add(e);
    }

    @Override
    public String toJson(List<String> literalsOrder)
    {
        return this.getChildren().isEmpty() ? null : this.getFirstChild().toJson(literalsOrder);
    }
}
