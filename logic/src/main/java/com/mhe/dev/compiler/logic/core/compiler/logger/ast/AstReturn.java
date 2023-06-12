package com.mhe.dev.compiler.logic.core.compiler.logger.ast;

import com.mhe.dev.compiler.logic.core.compiler.logger.LogicSemanticCategory;
import com.mhe.dev.compiler.logic.core.compiler.logger.AbstractSyntaxTree;
import com.mhe.dev.compiler.logic.core.compiler.logger.NoLambdaAbstractSyntaxTree;
import java.util.List;

/**
 * AstReturn.
 */
public class AstReturn extends Ast implements NoLambdaAbstractSyntaxTree<LogicSemanticCategory>
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

    @Override
    public String getShape()
    {
        return "rectangle";
    }

    @Override
    public String getLabel()
    {
        return "return";
    }

    @Override
    public String getColor()
    {
        return "orange";
    }
}
